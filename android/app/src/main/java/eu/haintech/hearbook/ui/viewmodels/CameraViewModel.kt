package eu.haintech.hearbook.ui.viewmodels

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.haintech.hearbook.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor

class CameraViewModel : ViewModel() {
    private val _pageCount = MutableStateFlow(0)
    val pageCount: StateFlow<Int> = _pageCount.asStateFlow()

    private var imageCapture: ImageCapture? = null
    private var mediaPlayer: MediaPlayer? = null

    fun getPreviewUseCase(): Preview {
        return Preview.Builder().build()
    }

    fun getCameraSelector(): CameraSelector {
        return CameraSelector.DEFAULT_BACK_CAMERA
    }

    fun setupImageCapture() {
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    private fun playShutterSound(context: Context) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, R.raw.camera_shutter)
            mediaPlayer?.start()
        } catch (e: Exception) {
            // Ignoruj błąd odtwarzania dźwięku
        }
    }

    private fun showFlashEffect(view: View?) {
        view?.let {
            it.alpha = 1f
            it.visibility = View.VISIBLE
            it.animate()
                .alpha(0f)
                .setDuration(100)
                .withEndAction {
                    it.visibility = View.GONE
                }
                .start()
        }
    }

    fun takePhoto(
        context: Context,
        bookId: String,
        executor: Executor,
        onPhotoTaken: (Uri) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val imageCapture = imageCapture ?: return@launch

            // Odtwórz dźwięk migawki
            playShutterSound(context)

            // Create output file
            val photoFile = createFile(context, bookId)
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            imageCapture.takePicture(
                outputOptions,
                executor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        output.savedUri?.let { uri ->
                            _pageCount.value++
                            onPhotoTaken(uri)
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        onError(exception.message ?: "Unknown error taking photo")
                    }
                }
            )
        }
    }

    private fun createFile(context: Context, bookId: String): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val storageDir = context.getExternalFilesDir("books/$bookId")
        storageDir?.mkdirs()
        return File(storageDir, "PAGE_${timeStamp}.jpg")
    }

    fun bindPreview(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        preview: Preview,
        onError: (String) -> Unit
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                cameraProvider.unbindAll()

                imageCapture?.let { capture ->
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        getCameraSelector(),
                        preview,
                        capture
                    )
                }
            } catch (e: Exception) {
                onError(e.message ?: "Failed to bind camera preview")
            }
        }, ContextCompat.getMainExecutor(context))
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
} 