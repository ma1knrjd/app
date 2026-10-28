package com.example.telestar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TelegramBlue = androidx.compose.ui.graphics.Color(0xFF3390EC)
private val PageBg = androidx.compose.ui.graphics.Color(0xFFF2F4F7)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { TeleStarApp() } }
    }
}

data class DemoChat(val name: String, val message: String, val avatar: String, val time: String)

@Composable
fun TeleStarApp() {
    var page by remember { mutableStateOf("Чаты") }
    var showAdmin by remember { mutableStateOf(false) }
    var displayName by remember { mutableStateOf("Пользователь") }
    var username by remember { mutableStateOf("user_demo") }
    var phone by remember { mutableStateOf("+880 000 000 000") }
    var stars by remember { mutableIntStateOf(100000) }
    var premium by remember { mutableStateOf(false) }
    var giftCount by remember { mutableIntStateOf(3) }
    var selectedChat by remember { mutableStateOf<DemoChat?>(null) }
    var draft by remember { mutableStateOf("") }
    val messages = remember { mutableStateMapOf<String, MutableList<String>>() }
    val chats = listOf(
        DemoChat("Избранное", "Заметки для себя", "⭐", "сейчас"),
        DemoChat("Demo User", "Привет! Это демо-чат", "👤", "14:25"),
        DemoChat("Группа тестирования", "Добро пожаловать", "👥", "вчера")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(if (selectedChat != null) selectedChat!!.name else if (showAdmin) "Админ-панель" else page,
                            fontWeight = FontWeight.Bold)
                        if (!showAdmin && selectedChat == null) Text("TeleStar • демо", fontSize = 11.sp)
                    }
                },
                navigationIcon = {
                    if (selectedChat != null || showAdmin) {
                        Text("‹", fontSize = 32.sp, modifier = Modifier.clickable {
                            selectedChat = null; showAdmin = false
                        }.padding(horizontal = 12.dp))
                    }
                },
                actions = {
                    if (!showAdmin && selectedChat == null) {
                        Text("⚙", fontSize = 22.sp, modifier = Modifier.clickable { page = "Настройки" }.padding(12.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TelegramBlue, titleContentColor = androidx.compose.ui.graphics.Color.White, actionIconContentColor = androidx.compose.ui.graphics.Color.White)
            )
        },
        bottomBar = {
            if (selectedChat == null && !showAdmin) {
                NavigationBar {
                    listOf("Чаты", "Контакты", "Профиль", "Настройки").forEach { item ->
                        NavigationBarItem(selected = page == item, onClick = { page = item },
                            icon = { Text(when(item) { "Чаты" -> "💬"; "Контакты" -> "👥"; "Профиль" -> "👤"; else -> "⚙" }) },
                            label = { Text(item) })
                    }
                }
            }
        },
        containerColor = PageBg
    ) { padding ->
        when {
            showAdmin -> AdminScreen(
                Modifier.padding(padding), displayName, { displayName = it }, username, { username = it },
                phone, { phone = it }, stars, { stars = it }, premium, { premium = it },
                giftCount, { giftCount = it }
            )
            selectedChat != null -> ChatScreen(
                Modifier.padding(padding), selectedChat!!,
                messages[selectedChat!!.name] ?: emptyList(),
                draft, { draft = it }, {
                    val key = selectedChat!!.name
                    val current = messages[key]?.toMutableList() ?: mutableListOf()
                    current.add(draft.trim())
                    messages[key] = current
                    draft = ""
                }
            )
            page == "Чаты" -> LazyColumn(Modifier.padding(padding).fillMaxSize()) {
                item { NoticeCard("Демо-режим", "Сообщения пока хранятся только на этом устройстве.") }
                items(chats) { chat ->
                    Row(
                        Modifier.fillMaxWidth().clickable { selectedChat = chat }.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Avatar(chat.avatar)
                        Column(Modifier.weight(1f).padding(start = 12.dp)) {
                            Text(chat.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            Text(chat.message, color = androidx.compose.ui.graphics.Color.Gray, fontSize = 14.sp)
                        }
                        Text(chat.time, color = androidx.compose.ui.graphics.Color.Gray, fontSize = 11.sp)
                    }
                    HorizontalDivider()
                }
            }
            page == "Контакты" -> Column(Modifier.padding(padding).padding(16.dp)) {
                Text("Контакты", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                chats.drop(1).forEach { Row(Modifier.fillMaxWidth().clickable { selectedChat = it }.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Avatar(it.avatar); Text(it.name, Modifier.padding(start = 12.dp))
                } }
            }
            page == "Профиль" -> Column(Modifier.padding(padding).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Avatar("👤", 76)
                Spacer(Modifier.height(12.dp))
                Text(displayName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("@$username", color = androidx.compose.ui.graphics.Color.Gray)
                Text(phone, modifier = Modifier.padding(8.dp))
                Text(if (premium) "⭐ Premium активен" else "Premium не активен")
                Text("Баланс: $stars ⭐")
                Text("Подарков: $giftCount 🎁")
            }
            page == "Настройки" -> Column(Modifier.padding(padding).padding(16.dp)) {
                Text("Настройки", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                SettingRow("Профиль", "Имя, номер и имя пользователя") { page = "Профиль" }
                SettingRow("Звёзды и подарки", "$stars ⭐ • $giftCount 🎁") { page = "Магазин" }
                SettingRow("Premium", if (premium) "Активен" else "Не активен") { premium = !premium }
                SettingRow("Админ-панель", "Демо-управление аккаунтом") { showAdmin = true }
                Text("В этой версии номер — отображаемое поле, не подтверждённая SIM.", color = androidx.compose.ui.graphics.Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 16.dp))
            }
            page == "Магазин" -> Column(Modifier.padding(padding).padding(16.dp)) {
                Text("Магазин", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Баланс: $stars ⭐")
                listOf("🎁 Подарок", "🌹 Роза", "💎 Коллекционный предмет").forEach { item ->
                    SettingRow(item, "Цена: 100 ⭐") {
                        if (stars >= 100) { stars -= 100; giftCount += 1 }
                    }
                }
            }
        }
    }
}

@Composable
fun Avatar(text: String, size: Int = 52) {
    Box(Modifier.size(size.dp).background(TelegramBlue.copy(alpha = .15f), CircleShape), contentAlignment = Alignment.Center) {
        Text(text, fontSize = (size / 2).sp)
    }
}

@Composable
fun NoticeCard(title: String, body: String) {
    Card(Modifier.fillMaxWidth().padding(12.dp), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.padding(14.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(body, fontSize = 13.sp, color = androidx.compose.ui.graphics.Color.Gray)
        }
    }
}

@Composable
fun SettingRow(title: String, subtitle: String, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(vertical = 5.dp).clickable(onClick = onClick), shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(subtitle, fontSize = 12.sp, color = androidx.compose.ui.graphics.Color.Gray)
            }
            Text("›", fontSize = 24.sp)
        }
    }
}

@Composable
fun AdminScreen(
    modifier: Modifier, name: String, setName: (String) -> Unit,
    username: String, setUsername: (String) -> Unit,
    phone: String, setPhone: (String) -> Unit,
    stars: Int, setStars: (Int) -> Unit,
    premium: Boolean, setPremium: (Boolean) -> Unit,
    gifts: Int, setGifts: (Int) -> Unit
) {
    var nameField by remember(name) { mutableStateOf(name) }
    var userField by remember(username) { mutableStateOf(username) }
    var phoneField by remember(phone) { mutableStateOf(phone) }
    var amount by remember { mutableStateOf("100000") }
    LazyColumn(modifier.fillMaxSize().padding(16.dp)) {
        item { NoticeCard("Админ-панель (демо)", "Изменения действуют только внутри приложения на этом устройстве.") }
        item { Text("Профиль", fontSize = 19.sp, fontWeight = FontWeight.Bold) }
        item { OutlinedTextField(nameField, { nameField = it }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(userField, { userField = it }, label = { Text("@username без @") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(phoneField, { phoneField = it }, label = { Text("Отображаемый номер") }, modifier = Modifier.fillMaxWidth()) }
        item { Button(onClick = { setName(nameField); setUsername(userField.removePrefix("@")); setPhone(phoneField) }, modifier = Modifier.fillMaxWidth()) { Text("Сохранить профиль") } }
        item { HorizontalDivider(Modifier.padding(vertical = 12.dp)) }
        item { Text("Экономика", fontSize = 19.sp, fontWeight = FontWeight.Bold) }
        item { Text("Баланс: $stars ⭐ • Подарков: $gifts 🎁") }
        item { OutlinedTextField(amount, { amount = it.filter(Char::isDigit).take(9) }, label = { Text("Сколько звёзд выдать") }, modifier = Modifier.fillMaxWidth()) }
        item { Button(onClick = { setStars(stars + (amount.toIntOrNull() ?: 0)) }, modifier = Modifier.fillMaxWidth()) { Text("Выдать звёзды") } }
        item { Button(onClick = { setGifts(gifts + 1) }, modifier = Modifier.fillMaxWidth()) { Text("Выдать подарок 🎁") } }
        item { Button(onClick = { setPremium(!premium) }, modifier = Modifier.fillMaxWidth()) { Text(if (premium) "Отключить Premium" else "Включить Premium") } }
    }
}

@Composable
fun ChatScreen(
    modifier: Modifier, chat: DemoChat, initialMessages: List<String>,
    draft: String, setDraft: (String) -> Unit, send: () -> Unit
) {
    Column(modifier.fillMaxSize()) {
        NoticeCard("Локальный демо-чат", "Сообщения не отправляются в официальный Telegram и не синхронизируются между устройствами.")
        LazyColumn(Modifier.weight(1f).fillMaxWidth().padding(12.dp)) {
            item { Text(chat.message, modifier = Modifier.padding(8.dp).background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(12.dp)).padding(12.dp)) }
            items(initialMessages) { msg ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text(msg, modifier = Modifier.padding(5.dp).background(TelegramBlue.copy(alpha = .18f), RoundedCornerShape(12.dp)).padding(10.dp))
                }
            }
        }
        Row(Modifier.fillMaxWidth().background(androidx.compose.ui.graphics.Color.White).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(draft, setDraft, modifier = Modifier.weight(1f), placeholder = { Text("Сообщение") }, maxLines = 3)
            TextButton(onClick = send, enabled = draft.isNotBlank()) { Text("➤") }
        }
    }
}
