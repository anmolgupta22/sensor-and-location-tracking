package com.example.sensorlocationtracking.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sensorlocationtracking.MyApplication
import com.example.sensorlocationtracking.R
import com.example.sensorlocationtracking.adapter.DataListAdapter
import com.example.sensorlocationtracking.databinding.FragmentPreviousTrackingBinding
import com.example.sensorlocationtracking.model.LocationDataList
import com.example.sensorlocationtracking.viewmodel.LocationViewModel
import com.example.sensorlocationtracking.viewmodel.LocationViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.launch
import javax.inject.Inject

class PreviousTrackingFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentPreviousTrackingBinding? = null
    private val binding: FragmentPreviousTrackingBinding
        get() = _binding!!

    @Inject
    lateinit var viewModel: LocationViewModel

    @Inject
    lateinit var viewModelFactory: LocationViewModelFactory
    private var googleMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    private var mapFragment: SupportMapFragment? = null
    private lateinit var navHostFragment: NavHostFragment
    private var dataListAdapter: DataListAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPreviousTrackingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity().application as MyApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[LocationViewModel::class.java]

        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        dataListAdapter = DataListAdapter(requireContext())
        binding.parentRv.apply {
            layoutManager = linearLayoutManager
            adapter = dataListAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }

        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(
            R.id.nav_graph_host_fragment
        ) as NavHostFragment

        mapFragment = SupportMapFragment.newInstance()
        parentFragmentManager
            .beginTransaction()
            .add(R.id.mapView, mapFragment!!, null)
            .setReorderingAllowed(true)
            .commit()
        mapFragment?.getMapAsync(this)

        binding.back.setOnClickListener {
            navHostFragment.navController.navigateUp()
        }

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val waypoints: MutableList<LatLng> = arrayListOf()
        //calling db

        lifecycleScope.launch {
            val previousLocationList = viewModel.fetchAllLocationTracking()
            if (previousLocationList.isNotEmpty()) {
                dataListAdapter?.apply {
                    setData(previousLocationList as ArrayList<LocationDataList>)
                    notifyDataSetChanged()
                }
                var count = 0
                for (locationData in previousLocationList) {
                    if (locationData.locationData.isNotEmpty()) {
                        for (data in locationData.locationData) {
                            // add latLng in list
                            data.latLng?.let { waypoints.add(it) }
                        }
                        // get first and last marker
                        val firstValue = locationData.locationData.first()
                        val lastValue = locationData.locationData.last()
                        val firstLocation: LatLng = firstValue.latLng!!
                        val lastLocation: LatLng = lastValue.latLng!!
                        count++
                        // google camera and zoom animate
                        googleMap?.apply {
                            moveCamera(CameraUpdateFactory.newLatLng(firstLocation))
                            animateCamera(CameraUpdateFactory.zoomTo(23F), 1500, null)

                            //add count for maker
                            currentMarker =
                                addMarker(MarkerOptions().position(firstLocation)
                                    .title("$count Start Marker"))
                            currentMarker =
                                addMarker(MarkerOptions().position(lastLocation)
                                    .title("$count End Marker"))
                        }
                    }

                }
                // polyline added in the map
                val polylineOptions = PolylineOptions().apply {
                    width(18f)
                    color(Color.BLUE)
                    geodesic(true)
                    addAll(waypoints)
                }
                googleMap?.addPolyline(polylineOptions)
            }
        }
    }
}