package com.example.projectahirpam.uicontroller.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.projectahirpam.uicontroller.auth.LoginScreen
import com.example.projectahirpam.uicontroller.auth.RegisterScreen
import com.example.projectahirpam.uicontroller.barang.BarangScreen
import com.example.projectahirpam.uicontroller.dashboard.DashboardScreen
import com.example.projectahirpam.uicontroller.kategori.KategoriScreen
import com.example.projectahirpam.uicontroller.laporan.LaporanScreen
import com.example.projectahirpam.uicontroller.stok.*
import com.example.projectahirpam.utils.UserSession

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userSession = remember { UserSession(context) }

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(onRegisterSuccess = {
                navController.popBackStack()
            })
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onBarangClick = { navController.navigate(Routes.BARANG) },
                onKategoriClick = { navController.navigate(Routes.KATEGORI) },
                onStokClick = { navController.navigate(Routes.STOK) },
                onLaporanClick = { navController.navigate(Routes.LAPORAN) },
                onLogoutClick = {
                    userSession.clearSession()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.BARANG) { 
            BarangScreen(
                onBack = { navController.popBackStack() }
            ) 
        }

        composable(Routes.KATEGORI) { KategoriScreen { navController.popBackStack() } }

        composable(Routes.STOK) {
            StokHubScreen(
                onStokMasuk = { navController.navigate(Routes.STOK_MASUK) },
                onStokKeluar = { navController.navigate(Routes.STOK_KELUAR) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.STOK_MASUK) { StokMasukScreen { navController.popBackStack() } }

        composable(Routes.STOK_KELUAR) { StokKeluarScreen { navController.popBackStack() } }

        composable(Routes.LAPORAN) { LaporanScreen { navController.popBackStack() } }
    }
}
