package com.example.actividad_01

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.content.Context
import android.os.Environment
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvDeviceInfo: TextView = findViewById(R.id.tvDeviceInfo)
        tvDeviceInfo.text = getDeviceInfo()

        val tvCameraInfo: TextView = findViewById(R.id.tvCameraInfo)
        tvCameraInfo.text = getCameraInfo()


    }

    private fun getDeviceInfo(): String {
        val sb = StringBuilder()
        sb.append("Modelo: ").append(Build.MODEL).append("\n")
        sb.append("Fabricante: ").append(Build.MANUFACTURER).append("\n")
        sb.append("Versión de Android: ").append(Build.VERSION.RELEASE).append("\n")
        sb.append("SDK: ").append(Build.VERSION.SDK_INT).append("\n")
        sb.append("Procesador: ").append(Build.HARDWARE).append("\n")
        sb.append("Display Info: ").append(Build.DISPLAY).append("\n")
        sb.append("Densidad: ").append(resources.displayMetrics.densityDpi).append(" DPI\n")
        sb.append("Ram: ").append(Runtime.getRuntime().totalMemory() / (1024 * 1024)).append(" MB\n")

        // Definir el espacio total y disponible de almacenamiento
        val storage = getStorageInfo()
        sb.append("Espacio total de almacenamiento: ").append(storage.totalGB).append(" GB\n")
        sb.append("Espacio disponible: ").append(storage.availableGB).append(" GB\n")

        return sb.toString()
    }

    private fun getStorageInfo(): StorageInfo {
        val path = Environment.getExternalStorageDirectory().path
        val stat = android.os.StatFs(path)
        val totalBytes = stat.blockCountLong * stat.blockSizeLong
        val availableBytes = stat.availableBlocksLong * stat.blockSizeLong

        val totalGB = totalBytes / (1024 * 1024 * 1024)
        val availableGB = availableBytes / (1024 * 1024 * 1024)

        return StorageInfo(totalGB, availableGB)
    }

    private fun getCameraInfo(): String {
        val cameraInfo = StringBuilder()
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as android.hardware.camera2.CameraManager
        cameraManager.cameraIdList.forEachIndexed { index, id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val facing = characteristics.get(android.hardware.camera2.CameraCharacteristics.LENS_FACING)
            val facingString = when (facing) {
                android.hardware.camera2.CameraCharacteristics.LENS_FACING_FRONT -> "Frontal"
                android.hardware.camera2.CameraCharacteristics.LENS_FACING_BACK -> "Trasera"
                else -> "Otra"
            }
            cameraInfo.append("Cámara ${index + 1}: $facingString\n")
        }
        return cameraInfo.toString()
    }

    data class StorageInfo(val totalGB: Long, val availableGB: Long)
}
