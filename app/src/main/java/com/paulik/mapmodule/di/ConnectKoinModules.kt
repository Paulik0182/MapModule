package com.paulik.mapmodule.di

import android.content.Context
import com.paulik.mapmodule.data.DataRepoImpl
import com.paulik.mapmodule.data.IMapFragmentRepoImpl
import com.paulik.mapmodule.data.MarkerInteractorImpl
import com.paulik.mapmodule.domain.interactor.MarkerInteractor
import com.paulik.mapmodule.domain.repos.DataRepo
import com.paulik.mapmodule.domain.repos.IMapFragmentRepo
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ConnectKoinModules {
    val appModule = module {
        scope(named<Context>()) {
            scoped {
                AppModule().applicationContext(context = androidApplication())
            }
        }

        single<IMapFragmentRepo> { IMapFragmentRepoImpl() }

        single<DataRepo> {
            DataRepoImpl(get())
        }

        single<MarkerInteractor> {
            MarkerInteractorImpl(get())
        }
    }
}