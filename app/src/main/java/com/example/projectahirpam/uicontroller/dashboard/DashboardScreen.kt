package com.example.projectahirpam.uicontroller.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun DashboardScreen(
    onBarangClick: () -> Unit = {},
    onKategoriClick: () -> Unit = {},
    onStokClick: () -> Unit = {},
    onLaporanClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A1A), // Hitam gelap
                        Color(0xFF2D2D2D), // Abu-abu gelap
                        Color(0xFF1A1A1A)  // Hitam gelap lagi
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
                
                // Title di sebelah logo
                Text(
                    text = "Dashboard",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            item {
                DashboardItem(
                    title = "Data Barang",
                    icon = Icons.Default.ShoppingCart,
                    onClick = onBarangClick
                )
            }
            item {
                DashboardItem(
                    title = "Kategori",
                    icon = Icons.Default.List,
                    onClick = onKategoriClick
                )
            }
            item {
                DashboardItem(
                    title = "Stok",
                    icon = Icons.Default.Edit,
                    onClick = onStokClick
                )
            }
            item {
                DashboardItem(
                    title = "Laporan",
                    icon = Icons.Default.DateRange,
                    onClick = onLaporanClick
                )
            }
        }

