package com.example.game_bugs_android

import android.os.Bundle
import android.widget.CalendarView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.game_bugs_android.ui.theme.Game_bugs_androidTheme
import androidx.compose.material3.ExposedDropdownMenuDefaults


// Структура данных игрока
data class PlayerData(
    val fio: String,
    val gender: String,
    val course: String,
    val difficulty: Int,
    val birthDate: String,
    val zodiacSign: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Game_bugs_androidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegistrationForm(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationForm(modifier: Modifier = Modifier) {
    // Переменные состояния
    var fio by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var selectedCourse by remember { mutableStateOf("1 курс") }
    var difficultyLevel by remember { mutableFloatStateOf(1f) }
    var selectedDate by remember { mutableStateOf("") }
    var zodiacSign by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var showCalendar by remember { mutableStateOf(false) }

    val genderOptions = listOf("Мужской", "Женский", "Существо")
    val courses = listOf("1 курс", "2 курс", "3 курс", "4 курс", "ОТЧИСЛЕН")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Регистрация игрока",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // ФИО
        OutlinedTextField(
            value = fio,
            onValueChange = { fio = it },
            label = { Text("ФИО") },
            placeholder = { Text("Введите ваше ФИО") },
            modifier = Modifier.fillMaxWidth()
        )

        // Пол
        Text("Пол:", fontWeight = FontWeight.Medium)
        Column {
            genderOptions.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedGender == option),
                        onClick = { selectedGender = option }
                    )
                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        // Курс
        Text("Курс:", fontWeight = FontWeight.Medium)
        Box {
            OutlinedButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(selectedCourse)
                    Icon(
                        painter = painterResource(
                            if (expanded) android.R.drawable.arrow_up_float
                            else android.R.drawable.arrow_down_float
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course) },
                        onClick = {
                            selectedCourse = course
                            expanded = false
                        }
                    )
                }
            }
        }
        // Уровень сложности
        Text("Уровень сложности: ${difficultyLevel.toInt()}", fontWeight = FontWeight.Medium)
        Slider(
            value = difficultyLevel,
            onValueChange = { difficultyLevel = it },
            valueRange = 0f..2f,
            steps = 1,
            modifier = Modifier.fillMaxWidth()
        )

        // Дата рождения
        Text("Дата рождения:", fontWeight = FontWeight.Medium)

        // Кнопка для показа/скрытия календаря
        OutlinedButton(
            onClick = { showCalendar = !showCalendar },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (selectedDate.isEmpty()) "Выберите дату рождения" else "Дата: $selectedDate"
            )
        }

        // CalendarView
        if (showCalendar) {
            AndroidView(
                factory = { context ->
                    CalendarView(context).apply {
                        setOnDateChangeListener { _, year, month, dayOfMonth ->
                            selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                            zodiacSign = calculateZodiacSign(dayOfMonth, month + 1)
                            showCalendar = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }

        // Знак зодиака
        if (zodiacSign.isNotEmpty()) {
            Text("Знак зодиака:", fontWeight = FontWeight.Medium)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.photo),
                    contentDescription = "Знак зодиака",
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = zodiacSign,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Кнопка для показа данных
        Button(
            onClick = { showResult = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Показать данные")
        }

        // Вывод данных
        if (showResult) {
            val playerData = PlayerData(
                fio = fio,
                gender = selectedGender,
                course = selectedCourse,
                difficulty = difficultyLevel.toInt(),
                birthDate = selectedDate,
                zodiacSign = zodiacSign
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Данные игрока:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("ФИО: ${playerData.fio}")
                    Text("Пол: ${playerData.gender}")
                    Text("Курс: ${playerData.course}")
                    Text("Уровень сложности: ${playerData.difficulty}")
                    Text("Дата рождения: ${playerData.birthDate}")
                    if (playerData.zodiacSign.isNotEmpty()) {
                        Text("Знак зодиака: ${playerData.zodiacSign}")
                    }
                }
            }
        }
    }
}

fun calculateZodiacSign(day: Int, month: Int): String {
    return when (month) {
        1 -> if (day < 20) "Козерог" else "Водолей"
        2 -> if (day < 19) "Водолей" else "Рыбы"
        3 -> if (day < 21) "Рыбы" else "Овен"
        4 -> if (day < 20) "Овен" else "Телец"
        5 -> if (day < 21) "Телец" else "Близнецы"
        6 -> if (day < 21) "Близнецы" else "Рак"
        7 -> if (day < 23) "Рак" else "Лев"
        8 -> if (day < 23) "Лев" else "Дева"
        9 -> if (day < 23) "Дева" else "Весы"
        10 -> if (day < 23) "Весы" else "Скорпион"
        11 -> if (day < 22) "Скорпион" else "Стрелец"
        12 -> if (day < 22) "Стрелец" else "Козерог"
        else -> "Неизвестно"
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Game_bugs_androidTheme {
        RegistrationForm()
    }
}
