package eu.haintech.hearbook.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.haintech.hearbook.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(
    bookTitle: String,
    currentPage: Int,
    currentParagraph: Int,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onPreviousPageClick: () -> Unit,
    onNextPageClick: () -> Unit,
    onPreviousParagraphClick: () -> Unit,
    onNextParagraphClick: () -> Unit,
    onSpeedClick: () -> Unit,
    onViewScansClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = bookTitle,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = stringResource(R.string.current_position, currentPage, currentParagraph),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Main playback controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous page button
                Button(
                    onClick = onPreviousPageClick,
                    modifier = Modifier.size(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.previous_page),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = stringResource(R.string.previous_page),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                // Play/Pause button
                Button(
                    onClick = onPlayPauseClick,
                    modifier = Modifier.size(120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = stringResource(if (isPlaying) R.string.pause else R.string.play),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = stringResource(if (isPlaying) R.string.pause else R.string.play),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                // Next page button
                Button(
                    onClick = onNextPageClick,
                    modifier = Modifier.size(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = stringResource(R.string.next_page),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = stringResource(R.string.next_page),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            // Paragraph navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPreviousParagraphClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.previous_paragraph),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Button(
                    onClick = onNextParagraphClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.next_paragraph),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // Speed control
            Button(
                onClick = onSpeedClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Text(
                    text = stringResource(R.string.reading_speed),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // View scans button
            Button(
                onClick = onViewScansClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.view_scans),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
} 