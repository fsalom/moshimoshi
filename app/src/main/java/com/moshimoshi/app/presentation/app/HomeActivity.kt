package com.moshimoshi.app.presentation.app

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.datastore.preferences.preferencesDataStore
import com.moshimoshi.app.data.datasource.character.api.CharacterApi
import com.moshimoshi.app.di.Container
import com.moshimoshi.app.ui.theme.MoshiMoshiTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val Context.dataStore by preferencesDataStore(
    name = "settings")
class HomeActivity : ComponentActivity() {
    private val viewModel = HomeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Container.config(LocalContext.current, dataStore)
            MoshiMoshiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val message by viewModel.message.collectAsState()
                    viewModel.initialize()
                    Column(modifier = Modifier.padding(all = 20.dp)) {
                        Text(
                            text = "Hello!"
                        )
                        Box {
                            Button(onClick = {
                                viewModel.logout()
                            }) {
                                Text(
                                    text = "Logout"
                                )
                            }
                        }
                        Box {
                            Button(onClick = {
                                viewModel.loadAuthenticated()
                            }) {
                                Text(
                                    text = "Llamada autenticada"
                                )
                            }
                        }
                        Box {
                            Button(onClick = {
                                viewModel.load()
                            }) {
                                Text(
                                    text = "Llamada no autenticada"
                                )
                            }
                        }
                        Box {
                            Button(onClick = {
                                viewModel.expireAccessToken()
                            }) {
                                Text(
                                    text = "Caducar accessToken"
                                )
                            }
                        }

                        Box {
                            Button(onClick = {
                                viewModel.expireAccessAndRefreshToken()
                            }) {
                                Text(
                                    text = "Caducar accessToken y refreshToken"
                                )
                            }
                        }
                        if(message.isNotEmpty()) {
                            Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
                            viewModel.reset()
                        }
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoshiMoshiTheme {
    }
}