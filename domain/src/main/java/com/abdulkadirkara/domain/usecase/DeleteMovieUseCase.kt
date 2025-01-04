package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.CRUDResponseUI
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteMovieUseCase @Inject constructor(
    private val filmVerseRepository: FilmVerseRepository
) {
    suspend operator fun invoke(cartId: Int, userName: String): Flow<NetworkResponse<CRUDResponseUI>> =
        filmVerseRepository.deleteMovie(cartId, userName)
}