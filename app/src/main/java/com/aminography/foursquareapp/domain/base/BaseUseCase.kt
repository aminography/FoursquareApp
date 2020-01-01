package com.aminography.foursquareapp.domain.base

import kotlinx.coroutines.CoroutineScope

abstract class BaseUseCase<ParamsType, ResultType> {

    abstract fun execute(
        coroutineScope: CoroutineScope,
        params: ParamsType
    ): ResultType

}