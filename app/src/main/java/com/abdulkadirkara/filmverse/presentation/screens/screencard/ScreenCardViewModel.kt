package com.abdulkadirkara.filmverse.presentation.screens.screencard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.common.networkResponse.onEmpty
import com.abdulkadirkara.common.networkResponse.onError
import com.abdulkadirkara.common.networkResponse.onLoading
import com.abdulkadirkara.common.networkResponse.onSuccess
import com.abdulkadirkara.data.remote.ApiConstants
import com.abdulkadirkara.domain.model.CRUDResponseUI
import com.abdulkadirkara.domain.model.FilmCardItem
import com.abdulkadirkara.domain.usecase.DeleteMovieUseCase
import com.abdulkadirkara.domain.usecase.GetMovieCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenCardViewModel @Inject constructor(
    private val getMovieCartUseCase: GetMovieCartUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase,
) : ViewModel() {
    private val _movieCardState = MutableLiveData<CardUIState<List<FilmCardItem>>>()
    val movieCardState: LiveData<CardUIState<List<FilmCardItem>>> = _movieCardState

    private val _deleteMovieCardResult = MutableLiveData<NetworkResponse<CRUDResponseUI>>()
    val deleteMovieCardResult: MutableLiveData<NetworkResponse<CRUDResponseUI>>
        get() = _deleteMovieCardResult

    private val _productCount = MutableLiveData<Int>()
    val productCount: LiveData<Int> get() = _productCount

    init {
        getMovieCard(ApiConstants.USER_NAME)
    }

    private fun getMovieCard(userName: String) {
        viewModelScope.launch {
            getMovieCartUseCase(userName = userName).collect { it ->
                it.onError {
                    _movieCardState.value = CardUIState.Error(it.toString())
                }.onEmpty {
                    _movieCardState.value = CardUIState.Empty
                    _productCount.value = 0
                }.onLoading {
                    _movieCardState.value = CardUIState.Loading
                }.onSuccess {
                    _movieCardState.value = CardUIState.Success(it)
                    _productCount.value = it.size
                }
            }
        }
    }

    fun deleteMovieCard(cartId: Int, userName: String) {
        viewModelScope.launch {
            deleteMovieUseCase(cartId, userName).collect {
                _deleteMovieCardResult.value = it
            }
        }
    }

}