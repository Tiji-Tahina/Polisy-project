package com.example.polisy_project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.polisy_project.ui.viewmodel.SearchMode
import com.example.polisy_project.ui.viewmodel.SearchState
import com.example.polisy_project.ui.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onDriverFound: () -> Unit,
    onCreatePV: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Search Driver", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = onLogout) { Text("Logout") }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = viewModel.searchMode == SearchMode.LICENSE,
                onClick = { viewModel.searchMode = SearchMode.LICENSE },
                label = { Text("License #") }
            )
            FilterChip(
                selected = viewModel.searchMode == SearchMode.PLATE,
                onClick = { viewModel.searchMode = SearchMode.PLATE },
                label = { Text("Plate #") }
            )
        }

        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = { viewModel.searchQuery = it },
            label = {
                Text(
                    when (viewModel.searchMode) {
                        SearchMode.LICENSE -> "Enter license number"
                        SearchMode.PLATE -> "Enter plate number"
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.search() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        when (viewModel.searchState) {
            is SearchState.Loading -> CircularProgressIndicator()
            is SearchState.Error -> Text(
                text = (viewModel.searchState as SearchState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
            is SearchState.Result -> {
                viewModel.searchResult?.let { result ->
                    if (result.driver != null) {
                        DriverInfoCard(result.driver.name, result.driver.license_number, result.driver.phone)
                        result.vehicle?.let {
                            VehicleInfoCard(it.plate_number, it.make, it.model, it.year)
                        }
                        if (result.infractions.isNotEmpty()) {
                            Text("Previous Infractions (${result.infractions.size})", style = MaterialTheme.typography.titleMedium)
                            result.infractions.forEach { inf ->
                                InfractionCard(inf.type, inf.date, inf.location, inf.description)
                            }
                        } else {
                            Text("No previous infractions found.")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onCreatePV, modifier = Modifier.fillMaxWidth()) {
                            Text("Create Procès-Verbal")
                        }
                    } else {
                        Text("No driver found.")
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun DriverInfoCard(name: String, license: String, phone: String) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Driver", style = MaterialTheme.typography.titleSmall)
            Text("Name: $name")
            Text("License: $license")
            Text("Phone: $phone")
        }
    }
}

@Composable
private fun VehicleInfoCard(plate: String, make: String, model: String, year: Int) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Vehicle", style = MaterialTheme.typography.titleSmall)
            Text("Plate: $plate")
            Text("Make: $make  Model: $model  Year: $year")
        }
    }
}

@Composable
private fun InfractionCard(type: String, date: String, location: String, description: String) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(type, style = MaterialTheme.typography.titleSmall)
            Text("Date: $date")
            Text("Location: $location")
            if (description.isNotBlank()) Text("Notes: $description")
        }
    }
}
