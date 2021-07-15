package com.terranullius.yellowheartadmin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.terranullius.yellowheartadmin.data.Initiative
import com.terranullius.yellowheartadmin.data.InitiativeDto
import com.terranullius.yellowheartadmin.data.toInitiative
import com.terranullius.yellowheartadmin.data.toInitiativeDto
import com.terranullius.yellowheartadmin.other.Constants
import com.terranullius.yellowheartadmin.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel : ViewModel() {

    //TODO ADD FEATURE TO LISTEN TO REFRESH LIVE

    private val _initiativesFlow = MutableStateFlow<Result<List<Initiative>>>(Result.Loading)
    val initiativesFlow: StateFlow<Result<List<Initiative>>>
        get() = _initiativesFlow

    private val _isSignedIn = MutableStateFlow(false)
    val isSignedInFlow: StateFlow<Boolean>
        get() = _isSignedIn

    var isFirstRun = true

    fun onSignedIn() {
        _isSignedIn.value = true
        if (isFirstRun) {
            refreshInitiatives()
            isFirstRun = false
        }
    }

    private fun refreshInitiatives() {
        if (_isSignedIn.value) {
            viewModelScope.launch {
                _initiativesFlow.value = Result.Success(getInitiatives().map {
                    it.toInitiative()
                }.sortedBy {
                    it.order
                })
            }
        }
    }

    private suspend fun getInitiatives(): List<InitiativeDto> {
        val firestore = FirebaseFirestore.getInstance()
        val initiativesCollectionRef = firestore.collection(Constants.COLLECTION_INITIATIVE)

        return try {
            initiativesCollectionRef.get().await().documents.mapNotNull {
                it.toInitiativeDto()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}