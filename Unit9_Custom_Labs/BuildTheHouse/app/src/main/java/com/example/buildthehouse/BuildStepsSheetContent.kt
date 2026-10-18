package com.example.buildthehouse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Stateless list of construction steps. Only the step allowed by [isStepEnabled] can be
 * toggled; the change is reported through [onStepCheckedChange].
 */
@Composable
fun BuildStepsSheetContent(
    steps: List<BuildStep>,
    onStepCheckedChange: (step: BuildStep, checked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val done = steps.doneCount()
    Column(modifier.padding(horizontal = 16.dp)) {
        steps.forEachIndexed { index, step ->
            val enabled = isStepEnabled(index, done)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = step.isDone,
                        enabled = enabled,
                        role = Role.Checkbox,
                        onValueChange = { onStepCheckedChange(step, it) },
                    )
                    .padding(vertical = 4.dp),
            ) {
                // The Row handles the click, so the Checkbox itself is display-only.
                Checkbox(checked = step.isDone, onCheckedChange = null, enabled = enabled)
                Text(
                    text = "${index + 1}. ${step.title}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (enabled) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    },
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun BuildStepsSheetContentPreview() {
    BuildStepsSheetContent(steps = BuildSteps.withDoneCount(3), onStepCheckedChange = { _, _ -> })
}
