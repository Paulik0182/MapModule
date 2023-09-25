package com.paulik.mapmodule.domain

import com.google.android.gms.maps.model.MarkerOptions
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MapFragmentMvp : MvpView {
    fun init()
    fun loadMarkers(markers: MutableList<MarkerOptions>)
}
