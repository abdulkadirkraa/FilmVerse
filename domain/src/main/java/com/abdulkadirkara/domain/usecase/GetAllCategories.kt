package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.FilmCategoryEntity
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all movie categories from the repository.
 *
 * @param filmVerseRepository The repository responsible for fetching movie categories.
 */
class GetAllCategoriesUseCase @Inject constructor(
    private val filmVerseRepository: FilmVerseRepository
) {

    /**
     * Retrieves a list of all available movie categories.
     *
     * @return A [Flow] emitting a [NetworkResponse] containing a list of [FilmCategoryEntity] objects.
     */
    suspend operator fun invoke(): Flow<NetworkResponse<List<FilmCategoryEntity>>> =
        filmVerseRepository.getAllCategories()
}