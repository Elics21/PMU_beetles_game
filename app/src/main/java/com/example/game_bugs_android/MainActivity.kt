package com.example.game_bugs_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game_bugs_android.ui.theme.Game_bugs_androidTheme

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

        //ФИО
        var fio by remember { mutableStateOf("") }

        OutlinedTextField(
            value = fio,
            onValueChange = { fio = it },
            label = { Text("ФИО") },
            placeholder = { Text("Введите ваше ФИО") },
            modifier = Modifier.fillMaxWidth()
        )

        //Пол
        var selectedGender by remember { mutableStateOf("") }
        val genderOptions = listOf("Мужской", "Женский")

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
        var selectedCourse by remember { mutableStateOf("1 курс") }
        var expanded by remember { mutableStateOf(false) }
        val courses = listOf("1 курс", "2 курс", "3 курс", "4 курс")

        Text("Курс:", fontWeight = FontWeight.Medium)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedCourse,
                onValueChange = { },
                label = { Text("Выберите курс") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
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
        var difficultyLevel by remember { mutableFloatStateOf(1f) }

        Text("Уровень сложности: ${difficultyLevel.toInt()}", fontWeight = FontWeight.Medium)
        Slider(
            value = difficultyLevel,
            onValueChange = { difficultyLevel = it },
            valueRange = 1f..5f,
            steps = 3, // 4 промежуточные позиции между 1 и 5
            modifier = Modifier.fillMaxWidth()
        )

        // Дата рождения
        var selectedDate by remember { mutableStateOf("") }

        Text("Дата рождения:", fontWeight = FontWeight.Medium)
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { selectedDate = it },
            label = { Text("дд/мм/гггг") },
            placeholder = { Text("Например: 15/03/2000") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Game_bugs_androidTheme {
        RegistrationForm()
    }
}
