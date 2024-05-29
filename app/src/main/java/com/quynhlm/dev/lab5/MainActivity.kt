package com.quynhlm.dev.lab5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.quynhlm.dev.lab5.ui.theme.Lab5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab5Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginApp()
                }
            }
        }
    }
}


@Preview(showBackground = true , device = "id:pixel_4a")
@Composable
fun LoginApp() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                LoginScreen()
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4a")
@Composable
fun CategoryApp() {
    var categoryes = listOf(
        "Fiction",
        "Mystery",
        "Science Fiction",
        "Fantasy",
        "Adventure",
        "Historical",
        "Horror",
        "Romance"
    )

    val suggestions = listOf(
        "Biography", "Cookbook", "Poetry",
        "Self-help", "Thriller"
    )

    var selectedCategories by remember {
        mutableStateOf(setOf<String>())
    } // Tạo 1 state lưu mảng selected

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Choose a category:", style =
            MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        AssistChip(
            onClick = { /* Do something */ },
            label = { Text("Need help?") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Hiển thị arr 1
        CategoryChips(categoryes, selectedCategories, onCategorySelected = { category ->
            //Nếu tồn tại rồi thì xóa
            selectedCategories = if (selectedCategories.contains(category))
                selectedCategories - category
            else //Chưa tồn tại thì thêm
                selectedCategories + category
        })
        Spacer(modifier = Modifier.height(16.dp))
        //Hien thị Arr 2
        SuggestionChips(suggestions, selectedCategories,
            onSuggestionSelected = { suggestion ->
                selectedCategories = selectedCategories + suggestion
            })
        //Click thì add category vào arr selected
        if (selectedCategories.isNotEmpty()) { // nếu mà không rỗng
            Spacer(modifier = Modifier.height(16.dp))
            SelectedCategoriesChips(selectedCategories, onCategoryRemoved =
            { category ->
                selectedCategories = selectedCategories - category
            })
            //nếu click vào nút xóa thì xoá category khởi arr
            Spacer(modifier = Modifier.height(4.dp))
            //Nút clear
            AssistChip(
                onClick = { selectedCategories = setOf() }, //Clear thì set arr selected == empty
                label = {
                    Text(
                        "Clear selections",

                        style = TextStyle(
                            color = Color.White, fontWeight =

                            FontWeight.Bold
                        )
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor
                    = Color.DarkGray
                ),
                border = AssistChipDefaults.assistChipBorder(
                    borderColor =
                    Color.Black
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChips(
    categories: List<String>,
    selectedCategories: Set<String>,
    onCategorySelected: (String) -> Unit
) {
    Text("Choose categories:", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {// scroll horizontal
        categories.forEach { category -> // Duyệt qua array
            FilterChip(
                selected = selectedCategories.contains(category),
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun SuggestionChips(
    suggestions: List<String>,
    selectedCategories: Set<String>,
    onSuggestionSelected: (String) -> Unit
) {
    Text("Suggestions:", style = MaterialTheme.typography.titleMedium)
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        suggestions.forEach { suggestion ->
            val isSelected = selectedCategories.contains(suggestion) // Kiểm tra category đã tồn tại chưa
            val chipColors = if (isSelected) {
                SuggestionChipDefaults.suggestionChipColors(
                    containerColor = Color.LightGray
                )
            } else {
                SuggestionChipDefaults.suggestionChipColors()
            }
            SuggestionChip(
                onClick = { onSuggestionSelected(suggestion) },
                label = { Text(suggestion) },
                colors = chipColors,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedCategoriesChips(selectedCategories: Set<String>,
                            onCategoryRemoved: (String) -> Unit) {
    Text(
        "Selected categories:", style =
        MaterialTheme.typography.titleMedium
    )
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        selectedCategories.forEach { selectedCategory ->
            InputChip(
                selected = true,
                onClick = { },
                label = { Text(selectedCategory) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Deselect",
                        modifier = Modifier
                            .clickable {
                                onCategoryRemoved(selectedCategory)
                            }
                            .size(18.dp)
                    )
                },
                modifier = Modifier.padding(end = 8.dp),
            )
        }
    }
}

    @Composable
fun LoginScreen() {
    var context = LocalContext.current
    var username by remember {
        mutableStateOf("")
    }//State lưu trữ trạng thái username
    var password by remember {
        mutableStateOf("")
    }//State lưu trữ trạng thái username
    var isRemember by remember {
        mutableStateOf(false)
    }//State lưu trữ trạng thái switch
    var showDialog by remember { mutableStateOf(false) }//State lưu trữ trạng thái show
    var dialogMessage by remember { mutableStateOf("") } //State lưu trữ trạng thái message

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null
            )
            OutlinedTextField(
                label = { Text(text = "Username") },
                placeholder = { Text(text = "Username") },
                value = username,
                onValueChange = { username = it })
            OutlinedTextField(
                label = { Text(text = "Password") },
                placeholder = { Text(text = "Password") },
                value = password,
                onValueChange = { password = it })
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(checked = isRemember, onCheckedChange = { isRemember = !isRemember })
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Remember Me ?")
            }

            // if show Diaglog == true -> show dialog
            // else -> hide diglog
            if (showDialog) {
                DialogComponent(
                    onConfirmation = { showDialog = false },
                    dialogTitle = "Notification",
                    dialogMessage = dialogMessage //hiển thị giá trị state message
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (username.isEmpty() || password.isEmpty()) {
                            dialogMessage = "Pealse don't leave when empty"
                        } else {
                            if (username.equals("admin") && password.equals("123")) {
                                dialogMessage = "Login Successfully"
                            } else {
                                dialogMessage = "Login not Successfully"
                            }
                        }
                        showDialog = true // Set state diaglog == true
                    }
                    .padding(start = 20.dp, end = 20.dp)
                    .background(Color.Black, shape = RoundedCornerShape(20.dp))
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Login", color = Color.White)
            }
        }
}

@Composable
fun DialogComponent(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogMessage: String,
) {
    Dialog(onDismissRequest = {}) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(dialogTitle, style =
                MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(20.dp))
                Text(dialogMessage, style =
                MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onConfirmation,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    )
                ) {
                    Text("Okay")
                }
            }
        }
    }
}
@Composable
fun LightSwitch() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val isChecked = remember { mutableStateOf(false) }
        if (isChecked.value) { // == true hiển thị light ON
            Image(
                painter = painterResource(id = R.drawable.lighton),
                contentDescription = "Light is On",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        } else { // == false hiển thị là light Off
            Image(
                painter = painterResource(
                    id =
                    R.drawable.light
                ),
                contentDescription = "Light is Off",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Switch(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it }, // Khi click chuyển tràng thái switch
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Green,
                uncheckedThumbColor = Color.Gray,
                checkedTrackColor = Color.Green.copy(alpha = 0.5f),
                uncheckedTrackColor = Color.Gray.copy(alpha =
                0.5f),
                checkedBorderColor = Color.Green.copy(alpha =
                0.75f),
            )
        )
    }
}


@Composable
fun GreetingPreview() {
    Lab5Theme {

    }
}