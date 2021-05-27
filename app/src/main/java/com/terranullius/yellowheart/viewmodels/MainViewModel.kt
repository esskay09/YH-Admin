package com.terranullius.yellowheart.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terranullius.yellowheart.data.Initiative
import com.terranullius.yellowheart.data.toInitiative
import com.terranullius.yellowheart.firebase.FirebaseAuthUtils
import com.terranullius.yellowheart.firebase.FirestoreUtils
import com.terranullius.yellowheart.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _initiativesFlow = MutableStateFlow<Result<List<Initiative>>>(Result.Loading)
    val initiativesFlow: StateFlow<Result<List<Initiative>>>
        get() = _initiativesFlow

    private val _isSignedIn = MutableStateFlow(false)
    val isSignedInFlow: StateFlow<Boolean>
        get() = _isSignedIn

    var isFirstRun = true

    fun onSignedIn() {
        _isSignedIn.value = true
        if (isFirstRun){
            refreshInitiatives()
            isFirstRun = false
        }
    }

    fun refreshInitiatives() {
        if (_isSignedIn.value) {
            viewModelScope.launch {
                _initiativesFlow.value = Result.Success(FirestoreUtils.getInitiatives().map {
                    it.toInitiative()
                })
            }
        }
    }

}