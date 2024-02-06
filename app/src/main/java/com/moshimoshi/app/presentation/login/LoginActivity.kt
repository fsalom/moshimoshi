package com.moshimoshi.app.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moshimoshi.app.di.Container
import com.moshimoshi.app.presentation.app.HomeViewModel
import com.moshimoshi.app.ui.module.ui.theme.MoshiMoshiTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginActivity : ComponentActivity() {
    private val viewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isLogged by viewModel.isLogged.collectAsState()
            MoshiMoshiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.padding(all = 20.dp)) {
                        viewModel.load()
                        if (isLogged) {
                            Box {
                                Text("Logueado")
                            }
                            val intent = Intent()
                            intent.setClassName(
                                "com.moshimoshi.app",
                                "com.moshimoshi.app.presentation.app.HomeActivity"
                            )
                            LocalContext.current.startActivity(intent)
                        } else {
                            Box {
                                Button(onClick = {
                                    viewModel.login(
                                        username = "desarrollo@rudo.es",
                                        password = "12345678A"
                                    )
                                }) {
                                    Text(
                                        text = "Login"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MoshiMoshiTheme {

    }
}