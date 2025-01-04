package com.abdulkadirkara.domain.usecase

abstract class BaseUseCase <in P,R> {
    abstract suspend fun invoke(parameters: P?): R
}