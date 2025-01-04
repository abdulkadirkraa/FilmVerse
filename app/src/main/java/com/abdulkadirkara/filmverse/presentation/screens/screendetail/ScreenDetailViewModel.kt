package com.abdulkadirkara.filmverse.presentation.screens.screendetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.CRUDResponseUI
import com.abdulkadirkara.domain.usecase.InsertMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenDetailViewModel @Inject constructor(
    private val insertMovieUseCase: InsertMovieUseCase,
) : ViewModel() {

    private val _insertMovieCardResult = MutableLiveData<NetworkResponse<CRUDResponseUI>>()
    val insertMovieCardResult: MutableLiveData<NetworkResponse<CRUDResponseUI>>
        get() = _insertMovieCardResult

    fun insertMovieCard(
        name: String,
        image: String,
        price: Int,
        category: String,
        rating: Double,
        year: Int,
        director: String,
        description: String,
        orderAmount: Int,
        userName: String
    ){
        viewModelScope.launch {
            insertMovieUseCase(
                name, image, price, category, rating, year, director, description, orderAmount, userName
            ).collect{
                _insertMovieCardResult.value = it
                Log.e("ScreenDetailViewModel", "insertMovieCard: $it")
            }
        }
    }
}