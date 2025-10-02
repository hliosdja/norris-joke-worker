package com.example.work_manager_rnd.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.work_manager_rnd.presentation.viewmodel.JokeViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JokeScreen(
    viewModel: JokeViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val (show, setShow) = remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 30.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SchedulerText { setShow(true) }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.joke,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic
                )
            }
        }
        if (show) {
            Scheduler(
                onConfirm = { hour, minute ->
                    viewModel.updateSchedule(hour, minute)
                    setShow(false)
                },
                onDismissRequest = { setShow(false) }
            )
        }
    }
}

@Composable
private fun SchedulerText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val text = buildAnnotatedString {
        append("Set a schedule when to get another joke ")
        withLink(
            LinkAnnotation.Clickable(
                tag = "",
                styles = TextLinkStyles(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = {
                    onClick()
                }
            )
        ) {
            append("here")
        }
        append(".")
    }

    Text(
        text = text,
        style = TextStyle.Default.copy(
            color = MaterialTheme.colorScheme.onBackground
        ),
        fontStyle = FontStyle.Italic,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Scheduler(
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    val calendar = remember { Calendar.getInstance() }
    val state = rememberTimePickerState(
        initialHour = calendar[Calendar.HOUR_OF_DAY],
        initialMinute = calendar[Calendar.MINUTE],
        is24Hour = false
    )

    Dialog(onDismissRequest = onDismissRequest) {
        Card {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                TimePicker(state = state)
                Button(
                    onClick = {
                        onConfirm(state.hour, state.minute)
                    }
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}