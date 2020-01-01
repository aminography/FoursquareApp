package com.aminography.foursquareapp.core.logic.dependency

import com.aminography.foursquareapp.data.datasource.local.LocalDataSource
import com.aminography.foursquareapp.data.datasource.local.db.AppDatabase
import com.aminography.foursquareapp.data.datasource.remote.RemoteDataSource
import com.aminography.foursquareapp.data.datasource.remote.webservice.base.WEB_SERVICE_END_POINT
import com.aminography.foursquareapp.data.datasource.remote.webservice.base.createService
import com.aminography.foursquareapp.data.repository.VenueDetailsRepository
import com.aminography.foursquareapp.data.repository.VenueRecommendationsRepository
import com.aminography.foursquareapp.domain.LoadVenueDetails
import com.aminography.foursquareapp.domain.LoadVenueRecommendations
import com.aminography.foursquareapp.domain.repository.IVenueDetailsRepository
import com.aminography.foursquareapp.domain.repository.IVenueRecommendationsRepository
import com.aminography.foursquareapp.core.logic.decision.FetchDecisionMaker
import com.aminography.foursquareapp.core.logic.location.LocationProvider
import com.aminography.foursquareapp.core.logic.network.NetworkChecker
import com.aminography.foursquareapp.presentation.viewmodel.VenueDetailsViewModel
import com.aminography.foursquareapp.presentation.viewmodel.VenueRecommendationsViewModel
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

/**
 * @author aminography
 */

val networkModule = module {
    single {
        NetworkChecker(androidContext())
    }

    single {
        createService(
            WEB_SERVICE_END_POINT
        )
    }
}

val locationModule = module {
    factory {
        LocationProvider.createInstance(
            LocationServices.getFusedLocationProviderClient(androidContext())
        )
    }

    single {
        FetchDecisionMaker(
            get(),
            get()
        )
    }
}

val dataSourceModule = module {
    single {
        AppDatabase.getInstance(androidContext()).let {
            LocalDataSource(
                it.locationDao(),
                it.venueDao(),
                it.venueDetailsDao()
            )
        }
    }

    single {
        RemoteDataSource(get())
    }
}

val repositoryModule = module {
    factory<IVenueRecommendationsRepository> {
        VenueRecommendationsRepository(
            get(),
            get(),
            get(),
            get()
        )
    }

    factory<IVenueDetailsRepository> {
        VenueDetailsRepository(
            get(),
            get()
        )
    }
}

val useCaseModule = module {
    factory {
        LoadVenueRecommendations(
            get()
        )
    }

    factory {
        LoadVenueDetails(
            get()
        )
    }
}

val viewModelModule = module {
    viewModel {
        VenueRecommendationsViewModel(
            get()
        )
    }
    viewModel {
        VenueDetailsViewModel(
            get()
        )
    }
}

val appComponent: List<Module> = listOf(
    networkModule,
    locationModule,
    dataSourceModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)