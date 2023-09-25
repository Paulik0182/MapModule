package com.paulik.mapmodule.data

import com.paulik.mapmodule.domain.repos.DataRepo
import com.paulik.mapmodule.domain.repos.IMapFragmentRepo

class DataRepoImpl(
    private var mapFragmentRepo: IMapFragmentRepo
) : DataRepo {

    override fun saveMarkers() {
        mapFragmentRepo.saveListMarkers()
    }
}