import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gym.ui.viewModel.MemberViewModel


@Composable
fun DashboardScreen(nav: NavController, vm: MemberViewModel) {
    val members by vm.member.collectAsState()

    Column(Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Total Members: ${members.size}", fontSize = 20.sp)

        Button(onClick = { nav.navigate("add") }) {
            Text("Add Member")
        }

        Button(onClick = { nav.navigate("list") }) {
            Text("View Members")
        }
    }
}
