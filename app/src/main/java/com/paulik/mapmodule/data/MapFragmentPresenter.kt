package com.paulik.mapmodule.data

import android.annotation.SuppressLint
import com.google.android.gms.maps.model.MarkerOptions
import com.paulik.mapmodule.domain.MapFragmentMvp
import com.paulik.mapmodule.domain.repos.IMapFragmentRepo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import moxy.MvpPresenter

class MapFragmentPresenter(
    private var mapFragmentRepo: IMapFragmentRepo
) : MvpPresenter<MapFragmentMvp>() {

    private val mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()

    public override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadMarkers()
    }

    private lateinit var callMapFragmentRepo: Single<MutableList<MarkerOptions>>
    fun loadMarkers() {
        callMapFragmentRepo =
            mapFragmentRepo.getMarkers() ?: Single.just(mutableListOf<MarkerOptions>())
        loadMarkersJavaRx()
    }

    @SuppressLint("CheckResult")
    private fun loadMarkersJavaRx() {
        callMapFragmentRepo
            .observeOn(mainThreadScheduler)
            .subscribe({ markers ->
                if (!markers.isNullOrEmpty()) {
                    viewState.loadMarkers(markers)
                } else {
                }
            }, {
            })
    }
}