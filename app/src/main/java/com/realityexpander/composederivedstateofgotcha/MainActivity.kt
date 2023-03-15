package com.realityexpander.composederivedstateofgotcha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.realityexpander.composederivedstateofgotcha.ui.theme.ComposeDerivedStateOfGotchaTheme

// From article:
// https://medium.com/@theAndroidDeveloper/yet-another-pitfall-in-jetpack-compose-you-must-be-aware-of-225a1d07d033

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDerivedStateOfGotchaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Greeting("Android")
                    SampleApp()
                }
            }
        }
    }
}

class TestViewModel : ViewModel() {
    var currentCounterValue by mutableStateOf(0) // NOTE: using a delegate here
        private set

    fun incrementCounter() {
        currentCounterValue += 1
    }
}

@Composable
private fun SampleApp() {
    val viewModel = viewModel<TestViewModel>()
    val counterValue = viewModel.currentCounterValue

    // This will not work because the derivedStateOf does not see the `counterValue` change bc its just a local variable
//    val isDivisible by remember {
//        derivedStateOf { counterValue % 10 == 0 }
//    }

    // One way to fix it
//    val isDivisible by remember(viewModel.currentCounterValue) {
//            derivedStateOf { counterValue % 10 == 0 }
//        }

    // Another way to fix it
val isDivisible by remember { derivedStateOf { viewModel.currentCounterValue % 10 == 0 } }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "$counterValue")
        Button(
            onClick = { viewModel.incrementCounter() },
            content = { Text(text = "Increment") }
        )
        Text(text = "Is divisible by 10 : $isDivisible")
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
    ComposeDerivedStateOfGotchaTheme {
        Greeting("Android")
    }
}
