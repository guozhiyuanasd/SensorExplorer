package com.example.sensorexplorer

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sensorexplorer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        // TYPE_ALL 枚举设备全部已注册传感器
        val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = SensorAdapter(sensors) { sensor ->
            val intent = Intent(this, SensorDetailActivity::class.java).apply {
                putExtra(EXTRA_SENSOR_TYPE, sensor.type)
                putExtra(EXTRA_SENSOR_NAME, sensor.name)
            }
            startActivity(intent)
        }

        binding.textCount.text = "检测到 ${sensors.size} 个传感器"
    }

    companion object {
        const val EXTRA_SENSOR_TYPE = "extra_sensor_type"
        const val EXTRA_SENSOR_NAME = "extra_sensor_name"
    }
}
