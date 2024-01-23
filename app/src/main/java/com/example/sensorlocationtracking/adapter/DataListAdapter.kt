package com.example.sensorlocationtracking.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sensorlocationtracking.databinding.ParentSensorLocationItemBinding
import com.example.sensorlocationtracking.model.LocationDataList

class DataListAdapter(private var context: Context) : RecyclerView.Adapter<DataListAdapter.DataViewHolder>() {

    private val items = ArrayList<LocationDataList>()

    fun setData(data: ArrayList<LocationDataList>) {
        items.clear()
        items.addAll(data)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        ParentSensorLocationItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.onBind(context, items[position])
    }

    override fun getItemCount(): Int = items.size

    class DataViewHolder(private val binding: ParentSensorLocationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(context: Context, item: LocationDataList) {
            binding.markers.text = "Start Marker: ${item.startMarker}, End Marker: ${item.endMarker}"

            val linearLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val eventListChildAdapter = DataListChildAdapter(item.locationData, item.velocity)
            binding.parentRv.apply {
                layoutManager = linearLayoutManager
                adapter = eventListChildAdapter
                itemAnimator = null
                setHasFixedSize(true)
            }
        }

    }


}