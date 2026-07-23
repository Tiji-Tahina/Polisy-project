package com.example.polisy_project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.polisy_project.ui.viewmodel.PVState
import com.example.polisy_project.ui.viewmodel.PVViewModel

@Composable
fun PVScreen(
    viewModel: PVViewModel,
    officerId: Int,
    onPVCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.pvState) {
        when (viewModel.pvState) {
            is PVState.Success -> {
                snackbarHostState.showSnackbar("PV saved successfully")
                viewModel.resetState()
                onPVCreated()
            }
            is PVState.Offline -> {
                snackbarHostState.showSnackbar("Offline — PV saved locally, will sync later")
                viewModel.resetState()
                onPVCreated()
            }
            else -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("New Procès-Verbal", style = MaterialTheme.typography.headlineSmall)

        Text("Driver", style = MaterialTheme.typography.titleMedium)
        SectionCard {
            OutlinedTextField(viewModel.driverName, { viewModel.driverName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.driverLicense, { viewModel.driverLicense = it }, label = { Text("License Number") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.driverPhone, { viewModel.driverPhone = it }, label = { Text("Phone") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
        }

        Text("Vehicle", style = MaterialTheme.typography.titleMedium)
        SectionCard {
            OutlinedTextField(viewModel.vehiclePlate, { viewModel.vehiclePlate = it }, label = { Text("Plate Number") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.vehicleMake, { viewModel.vehicleMake = it }, label = { Text("Make") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.vehicleModel, { viewModel.vehicleModel = it }, label = { Text("Model") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.vehicleYear, { viewModel.vehicleYear = it }, label = { Text("Year") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        }

        Text("Infraction", style = MaterialTheme.typography.titleMedium)
        SectionCard {
            OutlinedTextField(viewModel.infractionType, { viewModel.infractionType = it }, label = { Text("Type") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.infractionDate, { viewModel.infractionDate = it }, label = { Text("Date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.infractionLocation, { viewModel.infractionLocation = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(viewModel.infractionDescription, { viewModel.infractionDescription = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
        }

        if (viewModel.pvState is PVState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = { viewModel.submitPV(officerId) }, modifier = Modifier.fillMaxWidth()) {
                Text("Submit PV")
            }
        }

        SnackbarHost(hostState = snackbarHostState)
    }
}

@Composable
private fun SectionCard(content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            content()
        }
    }
}
