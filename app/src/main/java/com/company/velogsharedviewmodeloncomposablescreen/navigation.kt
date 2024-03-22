package com.company.velogsharedviewmodeloncomposablescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation

// ViewModel은 스택에서 해당 화면이 사라질 때 뷰 모델이 종료되면서 composabel간의 데이터 공유가 불가하였다.
// 하지만 backStackEntry의 확장함수를 만들면서 한 화면이 사라지더라도 여러 화면간에서 뷰모델을 공유할 수 있게 해준다면 기존의 문제는 해결 될 것이다.
// 중첩된 네비게이션 그래프에서 하위 네비게이션 그래프들 간에 ViewModel을 공유하게 하는 방식이다.

@Composable
fun SharedViewModelStudy() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "Main"
    ) {
        navigation(
            startDestination = "First",
            route = "Main"
        ) {
            composable("First") { entry ->
                val viewModel = entry.sharedViewModel<SharedViewModel>(navController)
                val state = viewModel.sharedState

                FirstScreen(navController , state , viewModel)
            }
            composable("Second") { entry ->
                val viewModel = entry.sharedViewModel<SharedViewModel>(navController)
                val state = viewModel.sharedState

                SecondScreen(navController , state , viewModel)
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    // 현재 화면(composable)의 상위 네비게이션 route 값을 반환하고 없으면 기본 viewModel을 반환한다.

    val parentEntry = remember(this) { // this는 NavBackStackEntry를 의미한다.
        navController.getBackStackEntry(navGraphRoute)
        // 현재 composable의 상위 composable의 route 값의 NavBackStackEntry를 상태값으로 반환+저장한다.
    }
    return viewModel(parentEntry)
    // 현재
}

@Composable
private fun FirstScreen(navController: NavController , state : Int , viewModel : SharedViewModel) {
    Column{
        Text(text = state.toString() , fontSize = 500.sp)
        Spacer(Modifier.size(16.dp))
        Button(onClick = {
            viewModel.updateState()
            navController.navigate("Second")
        }) {
            Text(text = "go To Second && state++")
        }
    }
}

@Composable
private fun SecondScreen(navController: NavController , state: Int , viewModel: SharedViewModel
) {
    Column{
        Text(text = state.toString() , fontSize = 500.sp)
        Spacer(Modifier.size(16.dp))
        Button(onClick = {
            viewModel.updateState()
            navController.navigate("First")
        }) {
            Text(text = "go To First && state++")
        }
    }
}