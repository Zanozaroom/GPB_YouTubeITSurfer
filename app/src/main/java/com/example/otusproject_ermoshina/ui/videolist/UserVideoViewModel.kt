package com.example.otusproject_ermoshina.ui.videolist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.base.DataBaseLoadException
import com.example.otusproject_ermoshina.base.YTVideo
import com.example.otusproject_ermoshina.sources.helpers.VideoLoadImpl
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserVideoViewModel @Inject constructor(
    private val helper:VideoLoadImpl
) : BaseViewModel() {
    private val _state = MutableLiveData<LoadingResult<List<YTVideo>>>()
    val state: LiveData<LoadingResult<List<YTVideo>>> = _state

    init{
        viewModelScope.launch {
            _state.value = LoadingResult.Loading
            helper.loadFavoriteVideo()
                .catch { exception ->
                    _state.value = LoadingResult.Error
                    Log.i("AAA", " ошибка в loadFavoriteVideo $exception")
                    showToast(R.string.messageNetworkLoadException)
                }
                .collect{
                    _state.value = LoadingResult.Success(it)
                }
        }
    }
    fun deleteVideo(video: YTVideo){
        viewModelScope.launch {
            try{
                helper.deleteVideo(video)
            }catch (e:Exception){
                catchException(e)
            }
        }
    }

    private fun catchException(e:Exception){
        when (e) {
            is DataBaseLoadException -> {
                _state.value = LoadingResult.Error
                showToast(R.string.messageRoomLoadException)
                Log.i("AAA", e.sayException())
            }
            else -> {
                _state.value = LoadingResult.Error
                Log.i("AAA", "Непонятная ошибка, все сломалось в UserVideoViewModel $e")
            }
        }
    }
}