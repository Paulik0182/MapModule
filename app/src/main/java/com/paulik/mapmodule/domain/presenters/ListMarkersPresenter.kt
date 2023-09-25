package com.paulik.mapmodule.domain.presenters

import android.annotation.SuppressLint
import com.google.android.gms.maps.model.MarkerOptions
import com.paulik.mapmodule.data.IMapFragmentRepoImpl
import com.paulik.mapmodule.domain.MapFragmentMvp
import com.paulik.mapmodule.domain.repos.IMapFragmentRepo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import moxy.MvpPresenter

class ListMarkersPresenter : MvpPresenter<MapFragmentMvp>() {

    private val mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()

    private var listMarkersRepo: IMapFragmentRepo = IMapFragmentRepoImpl()

    public override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    lateinit var callMenuRepo: Single<MutableList<MarkerOptions>>
    fun loadMarkers() {
        callMenuRepo = listMarkersRepo.getMarkers() ?: Single.just(mutableListOf<MarkerOptions>())
        loadMarkersJavaRx()
    }

    @SuppressLint("CheckResult")
    private fun loadMarkersJavaRx() {
        callMenuRepo
            .observeOn(mainThreadScheduler)
            .subscribe({ markers ->
                if (!markers.isNullOrEmpty()) {
                    viewState.loadMarkers(markers)
                } else {
                    viewState.loadMarkers(mutableListOf())
                }
            }, {
                viewState.loadMarkers(mutableListOf())
            })
    }

    fun onCorrectionClick(i: Int, marker: MarkerOptions) {
        listMarkersRepo.onCorrectionClick(i, marker)
    }

    fun onRemove(i: Int): MutableList<MarkerOptions> = listMarkersRepo.onRemove(i)

    fun saveListMarkers() {
        listMarkersRepo.saveListMarkers()
    }
}
