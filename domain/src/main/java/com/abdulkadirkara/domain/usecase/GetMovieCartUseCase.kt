package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.FilmCardItem
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieCartUseCase @Inject constructor(
    private val filmVerseRepository: FilmVerseRepository
) {
    suspend operator fun invoke(userName: String): Flow<NetworkResponse<List<FilmCardItem>>> =
        filmVerseRepository.getMovieCart(userName)
}