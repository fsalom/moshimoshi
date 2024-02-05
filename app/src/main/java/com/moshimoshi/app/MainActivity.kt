package com.moshimoshi.app

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.preferencesDataStore
import com.moshimoshi.app.api.CharacterApi
import com.moshimoshi.app.ui.theme.MoshiMoshiTheme
import com.moshimoshi.network.MoshiMoshi
import com.moshimoshi.network.authenticationcard.AuthenticationCard
import com.moshimoshi.network.authenticationcard.api.APIAuthenticationImpl
import com.moshimoshi.network.authenticator.Authenticator
import com.moshimoshi.network.authenticator.AuthenticatorImpl
import com.moshimoshi.network.interceptor.AuthInterceptor
import com.moshimoshi.network.retrofit.Authenticated
import com.moshimoshi.network.storage.datastore.TokenDataStoreImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Request
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query




class MainActivity : ComponentActivity() {
    val Context.dataStore by preferencesDataStore(
    name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoshiMoshiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val tokenStore = TokenDataStoreImpl(
                        uniqueIdentifier = "APP",
                        dataStore = dataStore)

                    val loginRequest = Request.Builder()
                        .url("https://publicobject.com/helloworld.txt")
                        .build()

                    val authenticationCard = APIAuthenticationImpl(
                        loginCall = loginRequest,
                        refreshCall = loginRequest,
                        packageName = "com.moshimoshi.app",
                        className = "com.moshimoshi.app.MainActivity",
                        context = LocalContext.current)

                    val authenticator = AuthenticatorImpl(
                        tokenStore = tokenStore,
                        card = authenticationCard)

                    val authInterceptor = AuthInterceptor(
                        authenticator = authenticator)

                    val moshi = MoshiMoshi(
                        baseUrl = "https://rickandmortyapi.com/api/",
                        interceptor = authInterceptor,
                        authenticator = authenticator)

                    var call = moshi.create(CharacterApi::class.java)

                    runBlocking {
                        // Launch a coroutine
                        val job = launch {
                            var x = moshi.load {
                                call.getCharacter(id = 1)
                            }
                        }

                        // Wait for coroutine to complete
                        job.join()
                        println("Main function complete.")
                    }


                    Greeting("Android")
                }
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
    MoshiMoshiTheme {
        Greeting("Android")
    }
}