package com.terranullius.yhadmin.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.terranullius.yhadmin.data.Initiative
import com.terranullius.yhadmin.data.toInitiative
import com.terranullius.yhadmin.data.toInitiativeDto
import com.terranullius.yhadmin.other.Constants
import com.terranullius.yhadmin.other.UpdateImageProperties
import com.terranullius.yhadmin.utils.Result
import kotlinx.coroutines.Job
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

    private val _pickImage = MutableStateFlow<UpdateImageProperties?>(null)
    val pickImage: StateFlow<UpdateImageProperties?>
        get() = _pickImage

    private var initiatives: List<Initiative>? = null

    private var firestoreListenerJob: Job? = null

    private var selectedUri: Uri? = null

    var isFirstRun = true

    fun onSignedIn() {
        _isSignedIn.value = true
        if (isFirstRun) {
            if (firestoreListenerJob == null) {
                viewModelScope.launch {
                    firestoreListenerJob = launch {
                        setInitiativesListener()
                    }
                }
            }
            isFirstRun = false
        }
    }

    private suspend fun setInitiativesListener() {
        if (_isSignedIn.value) {
            val firestore = FirebaseFirestore.getInstance()
            val initiativesCollectionRef = firestore.collection(Constants.COLLECTION_INITIATIVE)

            initiativesCollectionRef.addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d("fuck", "firestore listener error: $error")
                }
                value?.let {
                    initiatives = it.documents.mapNotNull { documentSnapshot ->
                        documentSnapshot.toInitiativeDto()
                    }.map { initiativeDto ->
                        initiativeDto.toInitiative()
                    }.sortedBy { initiative ->
                        initiative.order
                    }
                }
                initiatives?.let {
                    Log.d("fuck", "initiatives: $it")
                    _initiativesFlow.value = Result.Success(it)
                }
            }
        }
    }


    fun getImage(updateImageProperties: UpdateImageProperties) {
        viewModelScope.launch {
            _pickImage.value = updateImageProperties
        }
    }

    fun onGetImage(uri: Uri) {
        viewModelScope.launch {
            selectedUri = uri
        }
    }

    fun updateInitiative(initiative: Initiative) {
        val firestore = FirebaseFirestore.getInstance()
        val initiativesCollectionRef = firestore.collection(Constants.COLLECTION_INITIATIVE)

        initiativesCollectionRef.document(initiative.id).set(
            initiative.toInitiativeDto()
        )
    }

    fun uploadImage(){
        viewModelScope.launch {
            selectedUri?.let { uploadImageToFireStorage(it) }
        }
    }

    private suspend fun uploadImageToFireStorage(uri: Uri) {
        FirebaseStorage.getInstance().reference.child("initiatives/${uri.lastPathSegment}")
            .putFile(uri)
            .addOnFailureListener {
                _pickImage.value = null
            }
            .addOnSuccessListener {
                it.metadata?.reference?.downloadUrl?.addOnSuccessListener { downloadUri ->
                    Log.d("fuck", "downloadUri: $downloadUri")
                    _pickImage.value?.initiative?.let { updatedInitiative ->
                        Log.d("fuck", "updatedInitiative: $updatedInitiative")
                        updateInitiative(updatedInitiative.copy(
                            images = updatedInitiative.images.mapIndexed { index, prevImageLink ->
                                var newUri = prevImageLink
                                if (index == _pickImage.value?.imagePosition) {
                                    newUri = downloadUri.toString()
                                }
                                Log.d("fuck", "newUri: $newUri")
                                newUri
                            } as MutableList<String>
                        ))
                        _pickImage.value = null
                    } ?: Log.d("fuck", " fireStorage null")
                }?.addOnFailureListener {
                    _pickImage.value = null
                }
            }
    }
}