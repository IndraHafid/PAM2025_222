package com.example.projectahirpam.uicontroller.barang

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectahirpam.data.database.AppDatabase
import com.example.projectahirpam.data.entity.BarangManualEntity
import com.example.projectahirpam.data.entity.KategoriEntity
import com.example.projectahirpam.utils.UserSession
import com.example.projectahirpam.viewmodel.BarangFormState
import com.example.projectahirpam.viewmodel.BarangManualViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarangScreen(
    viewModel: BarangManualViewModel = viewModel(),
    onBack: () -> Unit
) {
    var search by remember { mutableStateOf("") }
    var sortBy by remember { mutableStateOf("Nama (A-Z)") }
    val sortOptions = listOf("Nama (A-Z)", "Nama (Z-A)", "Stok (Rendah-Tinggi)", "Stok (Tinggi-Rendah)")
    var expanded by remember { mutableStateOf(false) }
    
    val context = androidx.compose.ui.platform.LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val userId = remember { UserSession(context).getUserId() }
    val kategoriDao = remember { db.kategoriDao() }
    val kategoriList by kategoriDao.getAll(userId).collectAsState(initial = emptyList())
    
    val barangList by viewModel.list.collectAsState(initial = emptyList())
    val formState by viewModel.formState.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val filtered = remember(barangList, search, sortBy) {
        barangList.filter { it.namaBarang.contains(search, ignoreCase = true) }
            .let { list ->
                when (sortBy) {
                    "Nama (A-Z)" -> list.sortedBy { it.namaBarang.lowercase(Locale.getDefault()) }
                    "Nama (Z-A)" -> list.sortedByDescending { it.namaBarang.lowercase(Locale.getDefault()) }
                    "Stok (Rendah-Tinggi)" -> list.sortedBy { it.jumlah }
                    "Stok (Tinggi-Rendah)" -> list.sortedByDescending { it.jumlah }
                    else -> list
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Data Barang", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A1A)
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
            )
        },
        containerColor = Color(0xFF1A1A1A)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A1A1A),
                            Color(0xFF2D2D2D),
                            Color(0xFF1A1A1A)
                        )
                    )
                )
        ) {
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Data Barang",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    FloatingActionButton(
                        onClick = { viewModel.showAddDialog() },
                        containerColor = Color(0xFFBB86FC)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Tambah Barang", tint = Color.White)
                    }
                }

                Spacer(Modifier.padding(16.dp))

                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    label = { Text("Cari Barang", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFFBB86FC),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFFBB86FC),
                        unfocusedLabelColor = Color.Gray
                    )
                )

                Spacer(Modifier.height(8.dp))

                // Sorting Dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = sortBy,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Urutkan", color = Color.White) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFFBB86FC),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color(0xFFBB86FC),
                            unfocusedLabelColor = Color.Gray
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        sortOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = Color.Black) },
                                onClick = {
                                    sortBy = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                if (filtered.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Belum ada barang",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                            if (search.isNotBlank()) {
                                Text(
                                    "Tidak ada hasil untuk \"$search\"",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                } else {
                    LazyColumn {
                        items(filtered) { barang ->
                            BarangItem(
                                barang = barang,
                                onEdit = { viewModel.showEditDialog(barang) },
                                onDelete = { viewModel.deleteBarang(barang) }
                            )
                        }
                    }
                }
            }
        }
        
        // Add/Edit Dialog
        if (showDialog) {
            BarangFormDialog(
                formState = formState,
                kategoriList = kategoriList,
                onDismiss = { viewModel.hideDialog() },
                onSave = { viewModel.saveBarang() },
                onFormChange = { viewModel.updateFormState(it) },
                errorMessage = errorMessage
            )
        }
    }
}

@Composable
private fun BarangItem(
    barang: BarangManualEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D2D2D).copy(alpha = 0.8f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    barang.namaBarang,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Stok: ${barang.jumlah}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Row {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFFBB86FC),
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFCF6679),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BarangFormDialog(
    formState: BarangFormState,
    kategoriList: List<KategoriEntity>,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    onFormChange: (BarangFormState) -> Unit,
    errorMessage: String
) {
    var expanded by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (formState.isEditing) "Edit Barang" else "Tambah Barang",
                color = Color.Black
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = formState.nama,
                    onValueChange = { onFormChange(formState.copy(nama = it)) },
                    label = { Text("Nama Barang", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = formState.jumlah,
                    onValueChange = { onFormChange(formState.copy(jumlah = it)) },
                    label = { Text("Jumlah", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(Modifier.height(8.dp))
                
                // Kategori Dropdown
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = formState.kategoriId?.let { id ->
                                kategoriList.find { it.id == id }?.namaKategori
                            } ?: "Pilih Kategori",
                            color = Color.Black
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        kategoriList.forEach { kategori ->
                            DropdownMenuItem(
                                text = { Text(kategori.namaKategori, color = Color.Black) },
                                onClick = {
                                    onFormChange(formState.copy(kategoriId = kategori.id))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                if (errorMessage.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text("Simpan", color = Color(0xFFBB86FC))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = Color.Gray)
            }
        },
        containerColor = Color.White
    )
}
