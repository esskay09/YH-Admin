package com.terranullius.yellowheart.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terranullius.yellowheart.data.Initiative
import com.terranullius.yellowheart.data.toInitiative
import com.terranullius.yellowheart.firebase.FirestoreUtils
import com.terranullius.yellowheart.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    val dummyFlowData = MutableStateFlow<Result<List<Initiative>>>(Result.Loading)

    fun refreshInitiatives() {
        viewModelScope.launch{
            dummyFlowData.value = Result.Success(FirestoreUtils.getInitiatives().map {
                it.toInitiative()
            })
        }
    }
}