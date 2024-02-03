package com.moshimoshi.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.moshimoshi.app.ui.theme.MoshiMoshiTheme
import com.moshimoshi.network.MoshiMoshi
import com.moshimoshi.network.authenticationcard.AuthenticationCard
import com.moshimoshi.network.authenticator.Authenticator
import com.moshimoshi.network.interceptor.AuthInterceptor
import com.moshimoshi.network.storage.datastore.TokenDataStoreImpl
import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import java.util.prefs.Preferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoshiMoshiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val Context.dataStore by preferencesDataStore(
                        name = "settings"
                    )
                    var datastore = DataStore<Preferences> by preferencesDataStore(name = "settings")
                    var tokenStore = TokenDataStoreImpl(
                        uniqueIdentifier = "APP",
                        dataStore = )
                    var authenticationCard = AuthenticationCard()
                    var authenticator = Authenticator(tokenStore = , card = authenticationCard)
                    var authInterceptor = AuthInterceptor(authenticator = authenticator)
                    MoshiMoshi(
                        baseUrl = "",
                        interceptor = ,
                        )
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