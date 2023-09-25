package com.paulik.mapmodule.domain.interactor

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

interface MarkerInteractor {
    fun getMarker(latLng: LatLng): MarkerOptions
    fun moveMarker(arg0: Marker, titleMarker: String?)
}