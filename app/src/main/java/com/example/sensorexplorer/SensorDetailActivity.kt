package com.example.sensorexplorer

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.sensorexplorer.databinding.ActivityDetailBinding

class SensorDetailActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private val heartRatePermissionCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = intent.getStringExtra(EXTRA_SENSOR_NAME) ?: "传感器"

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val type = intent.getIntExtra(EXTRA_SENSOR_TYPE, 0)
        sensor = sensorManager.getDefaultSensor(type)

        if (sensor == null) {
            binding.textValues.text = "该传感器在当前设备上不可用"
            return
        }

        // 心率等身体传感器在 Android 10+ 需要运行时授权
        if (type == Sensor.TYPE_HEART_RATE
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && checkSelfPermission(Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BODY_SENSORS),
                heartRatePermissionCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == heartRatePermissionCode) {
            if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                register()
            } else {
                binding.textValues.text = "未授予身体传感器权限，无法读取该传感器"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val s = sensor ?: return
        val needsBody = s.type == Sensor.TYPE_HEART_RATE
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED
        if (!needsBody) register()
    }

    private fun register() {
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        binding.textValues.text = event.values.joinToString("\n") { "%.4f".format(it) }
        binding.textTimestamp.text = "时间戳: ${event.timestamp}"
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        val label = when (accuracy) {
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "高"
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "中"
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> "低"
            else -> "不可靠"
        }
        binding.textAccuracy.text = "精度: $label"
    }
}
