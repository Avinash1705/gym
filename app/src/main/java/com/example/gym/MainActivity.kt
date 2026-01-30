package com.example.gym

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gym.ui.navigation.NavGraph
import com.example.gym.ui.viewModel.MemberViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        enableEdgeToEdge()
        setContent {
            MaterialTheme {
//                NavGraph(vm)
                AppRoot()
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