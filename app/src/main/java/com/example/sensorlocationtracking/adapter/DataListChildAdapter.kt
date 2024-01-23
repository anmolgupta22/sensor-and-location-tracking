package com.example.sensorlocationtracking.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sensorlocationtracking.databinding.SensorLocationItemBinding
import com.example.sensorlocationtracking.model.LocationData

class DataListChildAdapter(
    private var items: ArrayList<LocationData>,
    private var velocity: Double?,
) : RecyclerView.Adapter<DataListChildAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        SensorLocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.onBind(items[position],velocity)
    }

    override fun getItemCount() = items.size


    class DataViewHolder(private var binding: SensorLocationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(item: LocationData, velocity: Double?) {
            binding.latitudeTv.text = "latitude: " + item.latLng?.latitude.toString()
            binding.longitudeTv.text = "longitude: " + item.latLng?.longitude.toString()
            binding.altitudeTv.text = "altitude: " + item.altitude.toString()
            binding.velocityTv.text = "velocity: " + velocity.toString()
            binding.magnetometerTv.text = "magneto meter: " + item.magnetometer.toString()
            binding.accelerometerTv.text = "acceleration  meter: " + item.acceleration?.joinToString()
            binding.gyroscopeTv.text = "gyroscope: " + item.gyroscope.toString()
        }


    }

}