package com.aminography.foursquareapp.domain.base

import kotlinx.coroutines.CoroutineScope

abstract class BaseUseCase<in ParamsType, out ResultType> {

    abstract fun execute(
        coroutineScope: CoroutineScope,
        params: ParamsType
    ): ResultType

}