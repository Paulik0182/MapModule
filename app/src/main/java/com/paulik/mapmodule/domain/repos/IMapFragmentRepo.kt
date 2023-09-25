package com.paulik.mapmodule.domain.repos


import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.rxjava3.core.Single

interface IMapFragmentRepo {
    fun getMarkers(): Single<MutableList<MarkerOptions>>
    fun addMarkerOnMap(latLng: LatLng): MarkerOptions
    fun draggedMarker(arg0: Marker, titleMarker: String?)
    fun saveListMarkers()
    fun onCorrectionClick(i: Int, marker: MarkerOptions)
    fun onRemove(i: Int): MutableList<MarkerOptions>
}
