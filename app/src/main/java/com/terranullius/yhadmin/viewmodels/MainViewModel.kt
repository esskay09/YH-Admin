package com.terranullius.yhadmin.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.terranullius.yhadmin.data.AdminDto
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

    private val _adminsFlow = MutableStateFlow<Result<List<AdminDto>>>(Result.Loading)
    val adminsFlow: StateFlow<Result<List<AdminDto>>>
        get() = _adminsFlow

    private val _isSignedIn = MutableStateFlow(false)
    val isSignedInFlow: StateFlow<Boolean>
        get() = _isSignedIn

    private val _pickImage = MutableStateFlow<UpdateImageProperties?>(null)
    val pickImage: StateFlow<UpdateImageProperties?>
        get() = _pickImage

    private val _imageUploadStatus = MutableStateFlow<Result<Unit>>(Result.Success(Unit))
    val imageUploadStatus: StateFlow<Result<Unit>>
        get() = _imageUploadStatus

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
                        getAdmins()
                        setInitiativesListener()
                    }
                }
            }
            isFirstRun = false
        }
    }

    private fun setInitiativesListener() {
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

    private fun getAdmins() {
        if (_isSignedIn.value) {
            val firestore = FirebaseFirestore.getInstance()
            val initiativesCollectionRef = firestore.collection(Constants.COLLECTION_ADMIN)

            initiativesCollectionRef.get().addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    task.result.let {
                        if (it != null) {
                            _adminsFlow.value = Result.Success(it.toObjects(AdminDto::class.java))
                        } else {
                            Log.d("fuck", "firestore admin listener error")
                            _adminsFlow.value = Result.Error(NullPointerException())
                        }
                    }
                } else {
                    Log.d("fuck", "firestore admin listener error")
                    _adminsFlow.value = Result.Error(NullPointerException())
                }
            }
        }
    }

    fun getImage(updateImageProperties: UpdateImageProperties) {
        _pickImage.value = updateImageProperties
    }

    fun onGetImage(uri: Uri) {
        selectedUri = uri
    }

    fun updateInitiative(initiative: Initiative) {
        val firestore = FirebaseFirestore.getInstance()
        val initiativesCollectionRef = firestore.collection(Constants.COLLECTION_INITIATIVE)

        initiativesCollectionRef.document(initiative.id).set(
            initiative.toInitiativeDto()
        )
    }

    fun uploadImage() {
        _imageUploadStatus.value = Result.Loading
        selectedUri?.let { uploadImageToFireStorage(it) }
        selectedUri = null
    }

    private fun uploadImageToFireStorage(uri: Uri) {
        FirebaseStorage.getInstance().reference.child("initiatives/${uri.lastPathSegment}")
            .putFile(uri)
            .addOnFailureListener {
                _pickImage.value = null
                _imageUploadStatus.value = Result.Error(NullPointerException())
            }
            .addOnSuccessListener {
                it.metadata?.reference?.downloadUrl?.addOnSuccessListener { downloadUri ->
                    Log.d("fuck", "downloadUri: $downloadUri")
                    _imageUploadStatus.value = Result.Success(Unit)
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
                    _imageUploadStatus.value = Result.Error(NullPointerException())
                }
            }
    }
}