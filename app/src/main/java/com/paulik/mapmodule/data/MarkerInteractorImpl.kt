package com.paulik.mapmodule.data

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.paulik.mapmodule.domain.interactor.MarkerInteractor
import com.paulik.mapmodule.domain.repos.IMapFragmentRepo

class MarkerInteractorImpl(
    private var mapFragmentRepo: IMapFragmentRepo
) : MarkerInteractor {

    override fun getMarker(latLng: LatLng): MarkerOptions {
        return mapFragmentRepo.addMarkerOnMap(latLng)
    }

    override fun moveMarker(arg0: Marker, titleMarker: String?) {
        mapFragmentRepo.draggedMarker(arg0, titleMarker)
    }
}