package com.example.data

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {
    private const val TAG = "GeminiService"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    /**
     * Generates text content using the Gemini 3.5 Flash model.
     * Takes the prompt and optional system instructions.
     */
    suspend fun generateContent(prompt: String, systemInstruction: String? = null): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            Log.e(TAG, "API Key not found in BuildConfig", e)
            ""
        }

        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Error: API Key is missing. Please configure GEMINI_API_KEY in the Secrets panel."
        }

        try {
            // Build request JSON
            val root = JSONObject()
            
            // Contents array
            val contentsArray = JSONArray()
            val contentObj = JSONObject()
            val partsArray = JSONArray()
            val partObj = JSONObject()
            partObj.put("text", prompt)
            partsArray.put(partObj)
            contentObj.put("parts", partsArray)
            contentsArray.put(contentObj)
            root.put("contents", contentsArray)

            // Optional System Instruction
            if (!systemInstruction.isNullOrBlank()) {
                val systemInstructionObj = JSONObject()
                val systemPartsArray = JSONArray()
                val systemPartObj = JSONObject()
                systemPartObj.put("text", systemInstruction)
                systemPartsArray.put(systemPartObj)
                systemInstructionObj.put("parts", systemPartsArray)
                root.put("systemInstruction", systemInstructionObj)
            }

            // Generation config
            val configObj = JSONObject()
            configObj.put("temperature", 0.7)
            root.put("generationConfig", configObj)

            val requestBodyString = root.toString()
            Log.d(TAG, "Request payload: $requestBodyString")

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = requestBodyString.toRequestBody(mediaType)

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string()
                Log.d(TAG, "Response code: ${response.code}, body: $bodyString")

                if (!response.isSuccessful) {
                    return@withContext "API Call failed with code ${response.code}: ${response.message}\nDetail: $bodyString"
                }

                if (bodyString.isNullOrBlank()) {
                    return@withContext "Error: Empty response from Gemini API"
                }

                val jsonResponse = JSONObject(bodyString)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates == null || candidates.length() == 0) {
                    return@withContext "Error: No candidates returned in response."
                }

                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.optJSONObject("content")
                if (content == null) {
                    return@withContext "Error: Candidate lacks content."
                }

                val parts = content.optJSONArray("parts")
                if (parts == null || parts.length() == 0) {
                    return@withContext "Error: Content lacks parts."
                }

                val textResponse = parts.getJSONObject(0).optString("text", "")
                if (textResponse.isEmpty()) {
                    return@withContext "Error: Part lacks text."
                }

                textResponse
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during API Call", e)
            "Network Error: ${e.localizedMessage ?: "Unknown error"}. Check internet connection or API keys."
        }
    }
}
