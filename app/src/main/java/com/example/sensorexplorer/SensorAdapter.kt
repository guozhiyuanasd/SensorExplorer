package com.example.sensorexplorer

import android.hardware.Sensor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sensorexplorer.databinding.ItemSensorBinding

class SensorAdapter(
    private val sensors: List<Sensor>,
    private val onClick: (Sensor) -> Unit
) : RecyclerView.Adapter<SensorAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSensorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSensorBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sensor = sensors[position]
        holder.binding.apply {
            textName.text = sensor.name
            textVendor.text = "厂商: ${sensor.vendor}"
            textType.text = "类型: ${sensor.type}  (${typeName(sensor.type)})"
            textSpec.text = buildString {
                append("量程: ${sensor.maximumRange}\n")
                append("分辨率: ${sensor.resolution}\n")
                append("功耗: ${sensor.power} mA\n")
                append("最小延迟: ${sensor.minDelay} μs")
            }
            root.setOnClickListener { onClick(sensor) }
        }
    }

    override fun getItemCount() = sensors.size

    private fun typeName(type: Int): String = when (type) {
        Sensor.TYPE_ACCELEROMETER -> "加速度"
        Sensor.TYPE_MAGNETIC_FIELD -> "磁场"
        Sensor.TYPE_GYROSCOPE -> "陀螺仪"
        Sensor.TYPE_LIGHT -> "光线"
        Sensor.TYPE_PROXIMITY -> "距离"
        Sensor.TYPE_PRESSURE -> "气压"
        Sensor.TYPE_GRAVITY -> "重力"
        Sensor.TYPE_LINEAR_ACCELERATION -> "线性加速度"
        Sensor.TYPE_ROTATION_VECTOR -> "旋转矢量"
        Sensor.TYPE_RELATIVE_HUMIDITY -> "相对湿度"
        Sensor.TYPE_AMBIENT_TEMPERATURE -> "环境温度"
        Sensor.TYPE_HEART_RATE -> "心率"
        Sensor.TYPE_STEP_COUNTER -> "计步"
        Sensor.TYPE_STEP_DETECTOR -> "步行检测"
        Sensor.TYPE_SIGNIFICANT_MOTION -> "显著运动"
        Sensor.TYPE_GAME_ROTATION_VECTOR -> "游戏旋转矢量"
        Sensor.TYPE_GYROSCOPE_UNCALIBRATED -> "未校准陀螺仪"
        else -> "其他"
    }
}
