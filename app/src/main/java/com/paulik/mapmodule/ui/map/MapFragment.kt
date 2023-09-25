package com.paulik.mapmodule.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.paulik.mapmodule.R
import com.paulik.mapmodule.data.MapFragmentPresenter
import com.paulik.mapmodule.databinding.FragmentMapsBinding
import com.paulik.mapmodule.domain.interactor.MarkerInteractor
import com.paulik.mapmodule.domain.repos.DataRepo
import com.paulik.mapmodule.domain.repos.IMapFragmentRepo
import com.paulik.mapmodule.ui.BaseFragment
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.inject

const val TO_MARKER = "TO_MARKER"

private val isParis = LatLng(48.85769609115522, 2.3638554986231695)

class MapFragment : BaseFragment<FragmentMapsBinding>(
    FragmentMapsBinding::inflate
) {
    private var toMarker: MarkerOptions? = null
    lateinit var googleMap: GoogleMap

    private val dataRepo: DataRepo by inject()
    private val markerInteractor: MarkerInteractor by inject()
    private val mapFragmentRepo: IMapFragmentRepo by inject()

    private val presenter: MapFragmentPresenter by moxyPresenter {
        MapFragmentPresenter(
            mapFragmentRepo
        )
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { gMap ->
        googleMap = gMap
        googleMap.apply {
            clear()
            setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.mpa))
            addMarker(
                MarkerOptions().position(isParis).title(getString(R.string.is_paris))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )

            if (toMarker != null) {
                moveCamera(CameraUpdateFactory.newLatLngZoom(toMarker!!.position, 30f))
            } else {
                moveCamera(CameraUpdateFactory.newLatLngZoom(isParis, 17f))
            }
            isMyLocationEnabled = true
            isTrafficEnabled = true
            isBuildingsEnabled = true
        }
        presenter.loadMarkers()
        initSpeedMeter()
        addMarkerOnMap()
        draggedMarker()
    }

    private fun addMarkerOnMap() {
        googleMap.setOnMapClickListener { latLng ->
            googleMap.addMarker(markerInteractor.getMarker(latLng))
        }
    }

    private fun draggedMarker() {

        var titleMarker: String? = null

        googleMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(arg0: Marker) {
                titleMarker = arg0.title + arg0.snippet
            }

            override fun onMarkerDragEnd(arg0: Marker) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.position))
                markerInteractor.moveMarker(arg0, titleMarker)
            }

            override fun onMarkerDrag(arg0: Marker) {}
        })
    }

    private fun enableLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
            return
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        val toMarkerString = arguments?.getString(TO_MARKER, null)
        toMarker = Gson().fromJson(toMarkerString, MarkerOptions::class.java)
        mapFragment?.getMapAsync(callback)
    }

    override fun init() {
        enableLocation()
    }

    override fun loadMarkers(markers: MutableList<MarkerOptions>) {
        markers.forEach {
            try {
                googleMap.addMarker(it)
            } catch (e: UninitializedPropertyAccessException) {
            }
        }
    }


    private fun initSpeedMeter() {
        googleMap.setOnMyLocationChangeListener {
            binding.speedMeterTextView.text =
                "${(it.speedAccuracyMetersPerSecond * 3.6).toInt()} \n км/ч"
        }
    }

    override fun onPause() {
        dataRepo.saveMarkers()
        super.onPause()
    }

    companion object {
        fun newInstance(bundle: Bundle): MapFragment {
            val fragment = MapFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}