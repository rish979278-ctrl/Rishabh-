package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.ui.viewmodel.BharatScreen
import com.example.ui.viewmodel.BharatViewModel
import com.example.ui.viewmodel.SimulatedFile
import com.example.ui.viewmodel.ChatMessage
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BharatAppContent(viewModel: BharatViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val context = LocalContext.current
    val connectionType = viewModel.getNetworkInfo(context)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // National Emblem style Chakra Wheel icon
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(BharatNavy, CircleShape)
                                .border(1.5.dp, BharatSaffron, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SettingsSuggest,
                                contentDescription = "Chakra Logo",
                                tint = BharatSaffron,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = if (currentScreen == BharatScreen.DASHBOARD) "Bharat AI" else currentScreen.name.replace("_", " "),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 20.sp
                        )
                    }
                },
                navigationIcon = {
                    if (currentScreen != BharatScreen.DASHBOARD) {
                        IconButton(
                            onClick = { viewModel.navigateTo(BharatScreen.DASHBOARD) },
                            modifier = Modifier.testTag("back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to dashboard",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                actions = {
                    // Small active connection tag
                    Row(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .background(
                                if (connectionType.contains("Disconnected")) Color(0x22FF5555) else Color(0x22138808),
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                1.dp,
                                if (connectionType.contains("Disconnected")) Color.Red else BharatGreen,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    if (connectionType.contains("Disconnected")) Color.Red else BharatGreen,
                                    CircleShape
                                )
                        )
                        Text(
                            text = if (connectionType.contains("Wi-Fi")) "Wi-Fi" else if (connectionType.contains("Cellular")) "Cellular" else "Offline",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (connectionType.contains("Disconnected")) Color.Red else BharatGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            if (currentScreen != BharatScreen.DASHBOARD) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = currentScreen == BharatScreen.AI_ASSISTANT,
                        onClick = { viewModel.navigateTo(BharatScreen.AI_ASSISTANT) },
                        label = { Text("AI Assist") },
                        icon = { Icon(Icons.Default.Chat, contentDescription = "AI") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == BharatScreen.DATA_RECOVERY,
                        onClick = { viewModel.navigateTo(BharatScreen.DATA_RECOVERY) },
                        label = { Text("Recover") },
                        icon = { Icon(Icons.Default.RestoreFromTrash, contentDescription = "Recovery") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == BharatScreen.SOFTWARE_INFO,
                        onClick = { viewModel.navigateTo(BharatScreen.SOFTWARE_INFO) },
                        label = { Text("Software") },
                        icon = { Icon(Icons.Default.DeveloperMode, contentDescription = "Software") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == BharatScreen.NETWORKING,
                        onClick = { viewModel.navigateTo(BharatScreen.NETWORKING) },
                        label = { Text("Networking") },
                        icon = { Icon(Icons.Default.NetworkCheck, contentDescription = "Networking") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == BharatScreen.EDUCATION_TOPPER,
                        onClick = { viewModel.navigateTo(BharatScreen.EDUCATION_TOPPER) },
                        label = { Text("Topper") },
                        icon = { Icon(Icons.Default.School, contentDescription = "Education") }
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                BharatScreen.DASHBOARD -> DashboardScreen(viewModel)
                BharatScreen.AI_ASSISTANT -> AIAssistantScreen(viewModel)
                BharatScreen.DATA_RECOVERY -> DataRecoveryScreen(viewModel)
                BharatScreen.SOFTWARE_INFO -> MobileSoftwareScreen(viewModel)
                BharatScreen.NETWORKING -> MobileNetworkingScreen(viewModel)
                BharatScreen.EDUCATION_TOPPER -> EducationTopperScreen(viewModel)
            }
        }
    }
}

@Composable
fun DashboardScreen(viewModel: BharatViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp, top = 8.dp)
    ) {
        // Banner Item
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_bharat_ai_banner),
                        contentDescription = "Bharat AI Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay to make text highly readable
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xCC000000)),
                                    startY = 50f
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "B H A R A T",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BharatSaffron,
                                letterSpacing = 3.sp
                            )
                            Text(
                                text = "•",
                                color = Color.White,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "A I   H U B",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BharatGreen,
                                letterSpacing = 3.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Next-Gen Mobile Diagnostics & Topper Strategies",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Welcome Motto
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "Motto Icon",
                        tint = BharatGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "Satyameva Jayate",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = BharatSaffron
                        )
                        Text(
                            text = "Empowering devices with AI diagnostics and minds with toppers' strategies.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Feature Grid Heading
        item {
            Text(
                text = "Explore AI-Driven Diagnostics & Tools",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // 1. AI Tool
        item {
            DashboardFeatureCard(
                title = "Bharat AI Assistant",
                description = "Ask questions, troubleshoot device errors, generate custom recovery scripts, or draft study strategies with Gemini AI.",
                icon = Icons.Default.Chat,
                accentColor = BharatSaffron,
                testTag = "btn_ai_assistant",
                onClick = { viewModel.navigateTo(BharatScreen.AI_ASSISTANT) }
            )
        }

        // 2. Data Recovery
        item {
            DashboardFeatureCard(
                title = "Data Recovery Suite",
                description = "Perform deep simulated scans on storage caches to find lost photos, videos, contacts, and utilize recovery manuals.",
                icon = Icons.Default.RestoreFromTrash,
                accentColor = BharatGreen,
                testTag = "btn_data_recovery",
                onClick = { viewModel.navigateTo(BharatScreen.DATA_RECOVERY) }
            )
        }

        // 3. Mobile Software
        item {
            DashboardFeatureCard(
                title = "Mobile Software Diagnostics",
                description = "Inspect real-time device specifications, system parameters, optimization recommendations, and Android developer hacks.",
                icon = Icons.Default.DeveloperMode,
                accentColor = BharatNavy,
                testTag = "btn_software_info",
                onClick = { viewModel.navigateTo(BharatScreen.SOFTWARE_INFO) }
            )
        }

        // 4. Mobile Networking
        item {
            DashboardFeatureCard(
                title = "Mobile Networking Suite",
                description = "Identify active transport connection, run active ping latency tests, and measure real connection speed in Mbps.",
                icon = Icons.Default.NetworkCheck,
                accentColor = BharatGreen,
                testTag = "btn_networking",
                onClick = { viewModel.navigateTo(BharatScreen.NETWORKING) }
            )
        }

        // 5. Topper Ideas
        item {
            DashboardFeatureCard(
                title = "Education Topper Hub",
                description = "Learn ranker hacks (Active Recall, Pomodoro revisions), and build fully custom chronological exam timetables.",
                icon = Icons.Default.School,
                accentColor = BharatNavy,
                testTag = "btn_education_topper",
                onClick = { viewModel.navigateTo(BharatScreen.EDUCATION_TOPPER) }
            )
        }
    }
}

@Composable
fun DashboardFeatureCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(accentColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun AIAssistantScreen(viewModel: BharatViewModel) {
    val chatHistory by viewModel.chatHistory.collectAsState()
    val isChatLoading by viewModel.isChatLoading.collectAsState()
    val listState = rememberLazyListState()
    var textInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val recommendationPills = listOf(
        "Recover WhatsApp chats",
        "Fix mobile Wi-Fi latency",
        "How to study like a topper",
        "Explain bootloop repair"
    )

    // Autoscroll chat
    LaunchedEffect(chatHistory.size) {
        if (chatHistory.isNotEmpty()) {
            listState.animateScrollToItem(chatHistory.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Chat Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = BharatSaffron.copy(alpha = 0.08f)),
            border = BorderStroke(1.dp, BharatSaffron.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Memory,
                    contentDescription = "AI Chip",
                    tint = BharatSaffron
                )
                Column {
                    Text(
                        text = "Bharat AI (Powered by Gemini 3.5 Flash)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Inputs are evaluated securely with specialized diagnostic directives.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Suggestion Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                recommendationPills.forEach { pill ->
                    SuggestionChip(
                        onClick = {
                            textInput = ""
                            viewModel.sendChatMessage(pill)
                        },
                        label = { Text(pill, fontSize = 11.sp) },
                        modifier = Modifier.testTag("pill_${pill.replace(" ", "_")}")
                    )
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(chatHistory) { message ->
                ChatBubble(message)
            }
            if (isChatLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = BharatSaffron
                                )
                                Text(
                                    text = "Bharat AI is analyzing...",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Input Field Area
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("Ask anything...") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_text_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Send,
                            keyboardType = KeyboardType.Text
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (textInput.isNotBlank()) {
                                    viewModel.sendChatMessage(textInput)
                                    textInput = ""
                                    focusManager.clearFocus()
                                }
                            }
                        )
                    )

                    IconButton(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                viewModel.sendChatMessage(textInput)
                                textInput = ""
                                focusManager.clearFocus()
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(BharatSaffron, CircleShape)
                            .testTag("chat_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send prompt",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Adheres to security standard encryption",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Clear History",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BharatNavy,
                        modifier = Modifier
                            .clickable { viewModel.clearChat() }
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleColor = if (message.isUser) {
        BharatNavy.copy(alpha = 0.12f)
    } else {
        Color.White
    }
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val bubbleShape = if (message.isUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }
    val borderStroke = if (message.isUser) {
        BorderStroke(1.dp, BharatNavy.copy(alpha = 0.2f))
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            shape = bubbleShape,
            border = borderStroke,
            modifier = Modifier.widthIn(max = 290.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = if (message.isUser) "You" else "Bharat AI",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun DataRecoveryScreen(viewModel: BharatViewModel) {
    var activeTab by remember { mutableStateOf(0) } // 0: Scanner, 1: Technical Manuals
    val isScanning by viewModel.isScanning.collectAsState()
    val scanProgress by viewModel.scanProgress.collectAsState()
    val scannedFiles by viewModel.scannedFiles.collectAsState()
    val recoveredIds by viewModel.recoveredIds.collectAsState()
    var selectedCategory by remember { mutableStateOf("Photos") }

    // Advisor input fields
    var phoneModel by remember { mutableStateOf("") }
    var issueText by remember { mutableStateOf("") }
    val isAdvisorLoading by viewModel.isRecoveryAdvisorLoading.collectAsState()
    val advisorResponse by viewModel.recoveryAdvisorResponse.collectAsState()

    val categories = listOf("Photos", "Videos", "Contacts", "WhatsApp", "Documents")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = activeTab) {
            Tab(selected = activeTab == 0, onClick = { activeTab = 0 }) {
                Text("Simulated Scanner", modifier = Modifier.padding(14.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Tab(selected = activeTab == 1, onClick = { activeTab = 1 }) {
                Text("AI Advisory Guides", modifier = Modifier.padding(14.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        if (activeTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Description Card
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BharatGreen.copy(alpha = 0.08f)),
                        border = BorderStroke(1.dp, BharatGreen.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Simulated Deep Storage Scan",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = BharatGreen
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "This utility performs a localized deep sectors diagnostic. It identifies cached metadata, thumb caches, and temporary directories to extract previously deleted data markers.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                // Category Selection
                item {
                    Text("Select Target Resource Group", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    val scrollState = androidx.compose.foundation.rememberScrollState()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scrollState),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { cat ->
                            val isSelected = selectedCategory == cat
                            val color = if (isSelected) BharatGreen else MaterialTheme.colorScheme.surfaceVariant
                            val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(color)
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(cat, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Scan Trigger
                item {
                    Button(
                        onClick = { viewModel.runDataRecoveryScan(selectedCategory) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_trigger_deep_scan"),
                        colors = ButtonDefaults.buttonColors(containerColor = BharatGreen),
                        enabled = !isScanning
                    ) {
                        if (isScanning) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Analyzing Sectors... ${(scanProgress * 100).toInt()}%")
                        } else {
                            Icon(Icons.Default.Search, contentDescription = "Scan")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Deep Scan storage for $selectedCategory")
                        }
                    }
                }

                // Scanning Progress bar
                if (isScanning) {
                    item {
                        LinearProgressIndicator(
                            progress = { scanProgress },
                            modifier = Modifier.fillMaxWidth(),
                            color = BharatGreen,
                            trackColor = BharatGreen.copy(alpha = 0.2f)
                        )
                    }
                }

                // Scan Results Header
                if (scannedFiles.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Located Deleted Metadata (${scannedFiles.size} items)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Success: 100% Extractable",
                                fontSize = 11.sp,
                                color = BharatGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Scanned items list
                    items(scannedFiles) { file ->
                        val isRecovered = recoveredIds.contains(file.id)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            if (isRecovered) BharatGreenLight else BharatSaffronLight,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isRecovered) Icons.Default.CheckCircle else Icons.Default.InsertDriveFile,
                                        contentDescription = "File Status",
                                        tint = if (isRecovered) BharatGreen else BharatSaffron
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = file.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Size: ${file.size} • Path: ${file.originalPath}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Button(
                                    onClick = { viewModel.recoverFile(file.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isRecovered) BharatGreen else BharatNavy
                                    ),
                                    enabled = !isRecovered,
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.testTag("recover_file_${file.id}")
                                ) {
                                    Text(
                                        text = if (isRecovered) "Recovered" else "Recover",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                } else if (!isScanning) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inbox,
                                contentDescription = "No scan data",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "No Scanned Records loaded",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Select resource group and run deep scan to inspect deleted sectors.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        } else {
            // AI advisory guides tab
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text("Ask Senior AI Advisor", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Inputs are analyzed by our Gemini model to craft specific technical terminal commands, ADB pulls, or recommended open-source recovery firmware custom-suited to your brand.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    OutlinedTextField(
                        value = phoneModel,
                        onValueChange = { phoneModel = it },
                        label = { Text("Mobile Model / Brand (e.g. Poco X3, Samsung S21)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("advisor_phone_model"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }

                item {
                    OutlinedTextField(
                        value = issueText,
                        onValueChange = { issueText = it },
                        label = { Text("Issue details (e.g. Cleared WhatsApp database, SD card unreadable)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("advisor_issue_text"),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                item {
                    Button(
                        onClick = { viewModel.askRecoveryAdvisor(phoneModel, issueText) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_trigger_advisor"),
                        colors = ButtonDefaults.buttonColors(containerColor = BharatNavy),
                        enabled = !isAdvisorLoading && phoneModel.isNotBlank() && issueText.isNotBlank()
                    ) {
                        if (isAdvisorLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Consulting Gemini Expert...")
                        } else {
                            Icon(Icons.Default.Help, contentDescription = "Ask")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate AI Technical Manual")
                        }
                    }
                }

                if (advisorResponse.isNotBlank()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Receipt, contentDescription = "Report", tint = BharatNavy)
                                    Text("Technical Recovery Manual", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Divider(modifier = Modifier.padding(vertical = 12.dp))
                                Text(
                                    text = advisorResponse,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MobileSoftwareScreen(viewModel: BharatViewModel) {
    var activeSubTab by remember { mutableStateOf(0) } // 0: Specs, 1: Optimizer & Developer Guide
    val isOptimizing by viewModel.isOptimizing.collectAsState()
    val cacheOptimized by viewModel.cacheOptimized.collectAsState()
    val trashSize by viewModel.simulatedTrashSize.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = activeSubTab) {
            Tab(selected = activeSubTab == 0, onClick = { activeSubTab = 0 }) {
                Text("Specs Inspector", modifier = Modifier.padding(14.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Tab(selected = activeSubTab == 1, onClick = { activeSubTab = 1 }) {
                Text("Optimizer & Guides", modifier = Modifier.padding(14.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        if (activeSubTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Real-time Device Specifications",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = BharatNavy
                    )
                    Text(
                        text = "The values displayed below are directly extracted from your active hardware build configurations.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Spec rows using real Build variables
                item { SpecRow(label = "Manufacturer", value = android.os.Build.MANUFACTURER) }
                item { SpecRow(label = "Model Name", value = android.os.Build.MODEL) }
                item { SpecRow(label = "Hardware Board", value = android.os.Build.BOARD) }
                item { SpecRow(label = "Brand", value = android.os.Build.BRAND) }
                item { SpecRow(label = "Device ID", value = android.os.Build.ID) }
                item { SpecRow(label = "Android Version", value = "Android ${android.os.Build.VERSION.RELEASE} (API ${android.os.Build.VERSION.SDK_INT})") }
                item { SpecRow(label = "CPU Architecture", value = android.os.Build.SUPPORTED_ABIS.firstOrNull() ?: "Unknown") }
                item { SpecRow(label = "Build Fingerprint", value = android.os.Build.FINGERPRINT) }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Optimizer block
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Junk Cache & Temp Files", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(
                                        text = if (cacheOptimized) "Storage fully cleared" else "Unnecessary files occupy storage space.",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = trashSize,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (cacheOptimized) BharatGreen else BharatSaffron
                                )
                            }
                            Divider(modifier = Modifier.padding(vertical = 12.dp))
                            Button(
                                onClick = { viewModel.runCacheOptimization() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("btn_optimize_software"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (cacheOptimized) BharatGreen else BharatNavy
                                ),
                                enabled = !isOptimizing && !cacheOptimized
                            ) {
                                if (isOptimizing) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Cleaning storage system...")
                                } else {
                                    Icon(
                                        imageVector = if (cacheOptimized) Icons.Default.CheckCircle else Icons.Default.Speed,
                                        contentDescription = "Optimize"
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(if (cacheOptimized) "System Fully Optimized!" else "Optimize Storage & Clear Cache")
                                }
                            }
                        }
                    }
                }

                // Tutorial guide
                item {
                    Text("Essential Developer Hacks", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Follow these instructions to unlock system options for advanced networking debugging and ADB recoveries.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    DeveloperStepCard(
                        stepNumber = "1",
                        title = "Enable Developer Options",
                        description = "Navigate to Settings -> About Phone -> tap 'Build Number' exactly seven (7) times sequentially. A prompt will say 'You are now a developer!'"
                    )
                }

                item {
                    DeveloperStepCard(
                        stepNumber = "2",
                        title = "Unlocking USB Debugging",
                        description = "Open newly added Settings -> System -> Developer Options. Scroll to find 'USB Debugging' and switch it ON. This allows ADB terminal bridges."
                    )
                }

                item {
                    DeveloperStepCard(
                        stepNumber = "3",
                        title = "Fastboot & Bootloader",
                        description = "To solve core crash or bootloop loops, hold Power + Volume Down while booting to activate fastboot. Connect to PC and run 'fastboot devices' in terminal."
                    )
                }
            }
        }
    }
}

@Composable
fun SpecRow(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.End, modifier = Modifier.weight(1f).padding(start = 16.dp))
        }
    }
}

@Composable
fun DeveloperStepCard(stepNumber: String, title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(BharatNavy, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(stepNumber, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 16.sp)
            }
        }
    }
}

@Composable
fun MobileNetworkingScreen(viewModel: BharatViewModel) {
    val pingLogs by viewModel.pingLogs.collectAsState()
    val isPingTesting by viewModel.isPingTesting.collectAsState()
    val speedResult by viewModel.speedTestResult.collectAsState()
    val isSpeedTesting by viewModel.isSpeedTesting.collectAsState()
    val context = LocalContext.current
    val liveConnection = viewModel.getNetworkInfo(context)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Dynamic transport state
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BharatGreen.copy(alpha = 0.08f)),
                border = BorderStroke(1.dp, BharatGreen.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(Icons.Default.Wifi, contentDescription = "Wifi", tint = BharatGreen, modifier = Modifier.size(36.dp))
                    Column {
                        Text("Active Interface Transport", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(liveConnection, fontSize = 16.sp, fontWeight = FontWeight.Black, color = BharatGreen)
                    }
                }
            }
        }

        // Active Sockets Ping Tool
        item {
            Text("Real-Time Sockets Ping Tester", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ping resolves genuine DNS connection speeds and transmits ICMP reachability checks or high-fidelity fallback requests to key Indian CDN gateways.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Button(
                onClick = { viewModel.runPingTest() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_trigger_ping"),
                colors = ButtonDefaults.buttonColors(containerColor = BharatNavy),
                enabled = !isPingTesting
            ) {
                if (isPingTesting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Resolving Server Nodes...")
                } else {
                    Icon(Icons.Default.NetworkCheck, contentDescription = "Ping")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Run Live Ping Test")
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "CONSOLE STDOUT LOGS",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Bold
                    )
                    Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 6.dp))
                    pingLogs.forEach { log ->
                        Text(
                            text = "> $log",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = if (log.contains("FAILED")) Color(0xFFEF4444) else Color(0xFF10B981),
                            modifier = Modifier.padding(vertical = 2.dp),
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Real speed tester
        item {
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            Text("Bandwidth Speed Estimator", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Downloads a tiny network payload from secure servers to dynamically calculate active download bandwidth speed.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = speedResult,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (speedResult.contains("Complete")) BharatGreen else MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.runSpeedTest() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_trigger_speedtest"),
                        colors = ButtonDefaults.buttonColors(containerColor = BharatGreen),
                        enabled = !isSpeedTesting
                    ) {
                        if (isSpeedTesting) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Benchmarking Bandwidth...")
                        } else {
                            Icon(Icons.Default.TrendingUp, contentDescription = "Speed")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Measure Bandwidth (Mbps)")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EducationTopperScreen(viewModel: BharatViewModel) {
    var activeSubPage by remember { mutableStateOf(0) } // 0: Revision Hacks, 1: Schedule Planner
    val schedule by viewModel.plannerSchedule.collectAsState()
    val isGenerating by viewModel.isGeneratingSchedule.collectAsState()
    var subjectsInput by remember { mutableStateOf("") }
    var targetHours by remember { mutableStateOf(6) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = activeSubPage) {
            Tab(selected = activeSubPage == 0, onClick = { activeSubPage = 0 }) {
                Text("Topper Hacks", modifier = Modifier.padding(14.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Tab(selected = activeSubPage == 1, onClick = { activeSubPage = 1 }) {
                Text("AI Study Planner", modifier = Modifier.padding(14.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        if (activeSubPage == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text("India's competitive Exams Revision Hacks", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BharatNavy)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Toppers do not study differently, they study strategically. Integrate these key ranker ideas into your daily review schedule.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    TopperHackCard(
                        title = "Active Recall Block",
                        motto = "Retrieval > Recognition",
                        description = "Do not just highlight notes. Close the book and write down everything you remember on a blank sheet, then verify. This activates synaptic neural connections far stronger."
                    )
                }

                item {
                    TopperHackCard(
                        title = "Spaced Repetitive Intervals",
                        motto = "Combatting the Forgetting Curve",
                        description = "Review concepts at expanding mathematical intervals: Day 1, Day 3, Day 7, Day 14. This permanently anchors variables into long-term cortex storage."
                    )
                }

                item {
                    TopperHackCard(
                        title = "Feynman Technique",
                        motto = "Teach to Learn",
                        description = "Explain complex formulas or equations to a hypothetical 10-year old child. If you struggle or use heavy jargon, it reveals exact conceptual gaps."
                    )
                }

                item {
                    TopperHackCard(
                        title = "The Pomodoro Revision Hack",
                        motto = "Interval High Focus",
                        description = "Study with absolute focus for 25 minutes, then force a 5-minute physical walk. Repeat 4 times then take a longer 30-minute rest. Keeps stamina peaks constant."
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text("AI Custom study Blueprint Builder", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Enter your custom targets below. Gemini AI will create a comprehensive, hour-by-hour chronological time-management table integrated with topper habits.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    OutlinedTextField(
                        value = subjectsInput,
                        onValueChange = { subjectsInput = it },
                        label = { Text("Subjects to study (e.g. Physics, Chemistry, Math)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("planner_subjects_input"),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                item {
                    Column {
                        Text("Daily Targets study: $targetHours Hours", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Slider(
                            value = targetHours.toFloat(),
                            onValueChange = { targetHours = it.toInt() },
                            valueRange = 2f..16f,
                            steps = 13,
                            colors = SliderDefaults.colors(
                                thumbColor = BharatNavy,
                                activeTrackColor = BharatNavy
                            ),
                            modifier = Modifier.testTag("planner_hours_slider")
                        )
                    }
                }

                item {
                    Button(
                        onClick = { viewModel.generateTopperSchedule(subjectsInput, targetHours) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_trigger_planner"),
                        colors = ButtonDefaults.buttonColors(containerColor = BharatNavy),
                        enabled = !isGenerating && subjectsInput.isNotBlank()
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Structuring Time Matrix...")
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "Schedule")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate AI Topper Study Plan")
                        }
                    }
                }

                if (schedule.isNotBlank()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = "Plan", tint = BharatNavy)
                                    Text("Hourly Time-Matrix Schedule", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Divider(modifier = Modifier.padding(vertical = 12.dp))
                                Text(
                                    text = schedule,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopperHackCard(title: String, motto: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BharatNavy)
                Box(
                    modifier = Modifier
                        .background(BharatSaffronLight, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(motto, fontSize = 11.sp, color = BharatSaffron, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 18.sp)
        }
    }
}
