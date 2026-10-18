package com.example.buildthehouse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buildthehouse.ui.theme.BuildTheHouseTheme
import com.example.buildthehouse.ui.theme.TopBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BuildTheHouseTheme {
                HouseScreen()
            }
        }
    }
}

// Saves only how many steps are checked; the list is rebuilt from BuildSteps on restore.
private val StepsSaver = Saver<List<BuildStep>, Int>(
    save = { it.doneCount() },
    restore = { BuildSteps.withDoneCount(it) },
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseScreen() {
    var steps by rememberSaveable(stateSaver = StepsSaver) { mutableStateOf(BuildSteps.initial) }
    var showSheet by remember { mutableStateOf(false) }
    val done = steps.doneCount()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TopBar),
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showSheet = true }) {
                Icon(Icons.Default.Build, contentDescription = stringResource(R.string.steps))
            }
        },
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.progress, done, steps.size),
                style = MaterialTheme.typography.titleMedium,
            )
            if (done == 0) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.size(width = 320.dp, height = 300.dp),
                ) {
                    Text(stringResource(R.string.empty_lot), fontStyle = FontStyle.Italic)
                }
            } else {
                HouseStageView(steps = steps)
            }
            if (done == steps.size) {
                Text(
                    text = stringResource(R.string.house_ready),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                )
            }
            OutlinedButton(
                onClick = { steps = BuildSteps.initial },
                enabled = done > 0,
            ) {
                Text(stringResource(R.string.reset))
            }
            // Keeps the reset button clear of the FAB when scrolled to the bottom.
            Spacer(Modifier.height(72.dp))
        }
    }

    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            BuildStepsSheetContent(
                steps = steps,
                onStepCheckedChange = { changed, checked ->
                    steps = steps.map { if (it.id == changed.id) it.copy(isDone = checked) else it }
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HouseScreenPreview() {
    BuildTheHouseTheme {
        HouseScreen()
    }
}
