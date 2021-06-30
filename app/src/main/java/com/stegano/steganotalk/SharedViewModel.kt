package com.stegano.steganotalk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private var _data = MutableLiveData<String>("ddddddd")
    val data: LiveData<String> = _data

    // data를 쓸 때에는 무조건 이 함수를 이용해야 함
    fun saveData(newData: String) {
        _data.value = newData
    }
}