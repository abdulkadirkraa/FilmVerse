package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.FilmCategoryUI
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val filmVerseRepository: FilmVerseRepository
) {
    suspend operator fun invoke(): Flow<NetworkResponse<List<FilmCategoryUI>>> =
        filmVerseRepository.getAllCategories()
}