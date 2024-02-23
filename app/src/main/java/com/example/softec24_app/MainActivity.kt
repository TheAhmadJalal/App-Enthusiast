package com.example.softec24_app

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.softec24_app.ui.theme.Softec24_APPTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    private val auth by  lazy{
        Firebase.auth
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Softec24_APPTheme(dynamicColor = false) {
                val isSystemInDarkMode = isSystemInDarkTheme()
                val systemUiColor = rememberSystemUiController()
                var isLoginScreen by remember { mutableStateOf(true) }
                var isSignUpScreen by remember { mutableStateOf(false) }

                SideEffect {
                    systemUiColor.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !isSystemInDarkMode
                    )
                }
                var isSuccess by remember { mutableStateOf(false) }
                var isFailed by remember { mutableStateOf(false) }
                var isFailedSignup by remember { mutableStateOf(false) }
                var isSignupOK by remember { mutableStateOf(false) }

                if (isLoginScreen) {
                    Log.d("inside:","Inside isLoginSuccess.")

                    LoginScreen(
                        auth = auth,
                        onSuccess = {
                            // Code to execute when login is successful
                            isSuccess = true
                            isFailed = false
                        },
                        onFailed = {
                            // Code to execute when login fails
                            isFailed = true
                        },
                        onSignUpClick = {
                            // Navigate to signup screen
                            isLoginScreen = false
                            isSignUpScreen=true
                        }
                    )
                } else if(isSignUpScreen){
                    SignUpScreen(
                        auth = auth,
                        onSignUpSuccess = {
                            // Code to execute when signup is successful
                            // Navigate to the next screen or perform other actions
                            Log.d("Successful","Signup Successfully")
                            isSuccess = true
                            isSignupOK=true
                        },
                        onSignUpFailed = {
                            // Code to execute when signup fails
                            Log.d("UnSuccessful","Signup UnSuccessfully")
                            isFailedSignup=true
                        }
                    )

                }
                if (isSuccess) {
                    Toast.makeText(LocalContext.current,"Welcome", Toast.LENGTH_LONG).show()

                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize()
                    ) {
                        PersonsList()
                    }
                }

                if(isFailed)
                {
                    Toast.makeText(LocalContext.current,"Login Unsuccessful", Toast.LENGTH_LONG).show()
                }
                if(isSignupOK)
                {
                    Toast.makeText(LocalContext.current,"SignUp Successful", Toast.LENGTH_LONG).show()
                }

                if(isFailedSignup)
                {
                    Toast.makeText(LocalContext.current,"SignUp Unsuccessful", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
@Composable
fun PersonsList() {
    val persons = listOf(
        "John Doe",
        "Jane Smith",
        "Alice Johnson",
        "Bob Brown",
        "Emma Garcia"
    )

    LazyColumn {
        items(persons.size) { index ->
            Text(
                text = persons[index],
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Divider()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    auth: FirebaseAuth,
    onSignUpSuccess: () -> Unit,
    onSignUpFailed: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    val genderOptions = listOf("Male", "Female", "Other")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.up),
            contentDescription = "signup Image",
            modifier = Modifier
                .size(250.dp)
                .clip(CircleShape)
                .background(Color.White)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp)
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp)
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp)
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp)
                .padding(vertical = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp)
                .padding(vertical = 8.dp)
        ) {
            var expanded by remember { mutableStateOf(false) }
            var selectedIndex by remember { mutableStateOf(0) }

            Text(
                text = "Gender: ${genderOptions[selectedIndex]}",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp),
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                genderOptions.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedIndex = index
                            gender = option
                            expanded = false
                        },text = {
                            Text(
                                text = option,
                                color = Color.Black,
                            )
                        })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Save additional user information to Firebase here if needed
                            onSignUpSuccess()
                        } else {
                            onSignUpFailed()
                        }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFF05845))

        ) {
            Text(text = "Sign Up")
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(auth: FirebaseAuth, onSuccess: () -> Unit,
                onFailed: () -> Unit, onSignUpClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(id = R.drawable.`in`),
            contentDescription = "Contact Image",
            modifier = Modifier
                .size(250.dp)
                .clip(CircleShape)
                // Remove background modifier
                 .background(Color.White)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )


        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text("email") },
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp)
                .padding(vertical = 8.dp),
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp)
                .padding(vertical = 8.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                Log.d("Inside","Inside onclick login")
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            Log.d("Successful","Login Successfully")
                            onSuccess()
                        }
                        else
                        {
                            Log.d("Unsuccessful","Login failed")
                            onFailed()
                        }
                    }

            },

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFF05845))

        ) {

            Text(text = "login",)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Click here to",
                color = Color.Black // Set the text color for "Click here to"
            )
            TextButton(
                onClick = onSignUpClick,
//                colors = ButtonDefaults.textButtonColors(contentColor = Color.White) // Set the text color for the TextButton
            ) {
                Text(
                    text = "Sign Up",
                    color = Color(0xFFF05845) // Set the text color for "Sign Up"
                )
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Softec24_APPTheme {
        Greeting("Android")
    }
}