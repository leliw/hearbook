package eu.haintech.hearbook.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.haintech.hearbook.R
import eu.haintech.hearbook.ui.viewmodels.CameraViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanningScreen(
    pageCount: Int,
    onTakePhoto: () -> Unit,
    onFinishScanning: () -> Unit,
    bookId: Long,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    val previewUseCase = remember { viewModel.getPreviewUseCase() }

    LaunchedEffect(Unit) {
        viewModel.setupImageCapture()
    }

    // Stan animacji błysku
    var shouldShowFlash by remember { mutableStateOf(false) }
    val flashAlpha by animateFloatAsState(
        targetValue = if (shouldShowFlash) 1f else 0f,
        animationSpec = tween(
            durationMillis = if (shouldShowFlash) 50 else 100,
            easing = LinearEasing
        ),
        finishedListener = {
            if (shouldShowFlash) {
                shouldShowFlash = false
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.pages_count, pageCount),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (hasCameraPermission) {
                // Camera preview
                AndroidView(
                    factory = { ctx ->
                        PreviewView(ctx).apply {
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                        }.also { previewView ->
                            previewUseCase.setSurfaceProvider(previewView.surfaceProvider)
                            viewModel.bindPreview(
                                context = ctx,
                                lifecycleOwner = lifecycleOwner,
                                preview = previewUseCase,
                                onError = { /* TODO: Handle error */ }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 160.dp)
                )
            }

            // Efekt błysku
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = flashAlpha))
            )

            // Bottom controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Take photo button
                Button(
                    onClick = {
                        if (hasCameraPermission) {
                            shouldShowFlash = true
                            viewModel.takePhoto(
                                context = context,
                                bookId = bookId,
                                executor = ContextCompat.getMainExecutor(context),
                                onPhotoTaken = { /* TODO: Handle photo taken */ },
                                onError = { /* TODO: Handle error */ }
                            )
                            onTakePhoto()
                        }
                    },
                    modifier = Modifier
                        .size(120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = stringResource(R.string.take_photo),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                // Finish scanning button
                Button(
                    onClick = onFinishScanning,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.finish_scanning),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
} 