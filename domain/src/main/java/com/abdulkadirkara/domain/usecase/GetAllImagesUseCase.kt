package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.FilmImageEntity
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all film images from the repository.
 *
 * @param filmVerseRepository The repository responsible for fetching the film images.
 */
class GetAllImagesUseCase @Inject constructor(
    private val filmVerseRepository: FilmVerseRepository
) {

    /**
     * Retrieves a list of all available film images.
     *
     * @return A [Flow] emitting a [NetworkResponse] containing a list of [FilmImageEntity] objects.
     */
    suspend operator fun invoke(): Flow<NetworkResponse<List<FilmImageEntity>>> =
        filmVerseRepository.getAllImages()
}