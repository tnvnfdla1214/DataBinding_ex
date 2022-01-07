package com.example.user_databinding


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _userList = MutableLiveData<ArrayList<User>>()
    val userList : LiveData<ArrayList<User>>
        get() = _userList
    val liveVisible = MutableLiveData<Boolean>()

    private var items = ArrayList<User>()

    init{
        items = arrayListOf(
            User("https://item.kakaocdn.net/do/493188dee481260d5c89790036be0e668b566dca82634c93f811198148a26065","Han",25),
            User("https://item.kakaocdn.net/do/493188dee481260d5c89790036be0e668b566dca82634c93f811198148a26065","Lee",33)
        )
        _userList.value = items
        liveVisible.value = true
    }

    fun buttonClick(){

        val user = User("","Test",20)
        items.add(user)
        _userList.value = items

        liveVisible.value = liveVisible.value != true
        /*
        if(liveVisible.value == true){
            liveVisible.value = false
        }else{ liveVisible.value = true }
         */
    }
}