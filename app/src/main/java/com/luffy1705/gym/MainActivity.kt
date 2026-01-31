package com.luffy1705.gym

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luffy1705.gym.ui.navigation.NavGraph
import com.luffy1705.gym.ui.screens.LottieSplashScreen
import com.luffy1705.gym.ui.theme.GymTheme
import com.luffy1705.gym.ui.viewModel.MemberViewModel
import com.luffy1705.gym.ui.viewModel.SplashViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
//        enableEdgeToEdge()
        setContent {

            val splashVm: SplashViewModel = viewModel()

            if (splashVm.showSplash) {
                LottieSplashScreen {
                    splashVm.onAnimationFinished()
                }
            } else {
                GymTheme {
                    AppRoot()
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot(){
val context  = LocalContext.current.applicationContext as Application
    val memberViewModel : MemberViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory(context))


//    val memberViewModel : MemberViewModel = viewModel(
//        factory = object : ViewModelProvider.Factory{
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                return MemberViewModel(context) as T
//            }
//        }
//    )
//    val memberViewModel : MemberViewModel = viewModel()
    NavGraph(memberViewModel)

}