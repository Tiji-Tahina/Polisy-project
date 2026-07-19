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
import com.example.polisy_project.ui.viewmodel.FormViewModel

@Composable
fun FormScreen(viewModel: FormViewModel, modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.saveSuccess) {
        when (viewModel.saveSuccess) {
            true -> {
                snackbarHostState.showSnackbar("Infraction saved successfully")
                viewModel.resetSaveStatus()
            }
            false -> {
                snackbarHostState.showSnackbar("Failed to save infraction")
                viewModel.resetSaveStatus()
            }
            null -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Driver Information", style = MaterialTheme.typography.titleMedium)
        SectionCard {
            OutlinedTextField(
                value = viewModel.driverName,
                onValueChange = { viewModel.driverName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.driverLicense,
                onValueChange = { viewModel.driverLicense = it },
                label = { Text("License Number") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.driverPhone,
                onValueChange = { viewModel.driverPhone = it },
                label = { Text("Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Text("Vehicle Information", style = MaterialTheme.typography.titleMedium)
        SectionCard {
            OutlinedTextField(
                value = viewModel.vehiclePlate,
                onValueChange = { viewModel.vehiclePlate = it },
                label = { Text("Plate Number") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.vehicleMake,
                onValueChange = { viewModel.vehicleMake = it },
                label = { Text("Make") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.vehicleModel,
                onValueChange = { viewModel.vehicleModel = it },
                label = { Text("Model") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.vehicleYear,
                onValueChange = { viewModel.vehicleYear = it },
                label = { Text("Year") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Text("Infraction Details", style = MaterialTheme.typography.titleMedium)
        SectionCard {
            OutlinedTextField(
                value = viewModel.infractionType,
                onValueChange = { viewModel.infractionType = it },
                label = { Text("Infraction Type") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.infractionDate,
                onValueChange = { viewModel.infractionDate = it },
                label = { Text("Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.infractionDescription,
                onValueChange = { viewModel.infractionDescription = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = { viewModel.submitInfraction() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Infraction")
        }

        Spacer(modifier = Modifier.height(16.dp))

        SnackbarHost(hostState = snackbarHostState)
    }
}

@Composable
private fun SectionCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}
