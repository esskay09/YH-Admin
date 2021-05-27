package com.terranullius.yellowheart.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.terranullius.yellowheart.data.InitiativeDto
import com.terranullius.yellowheart.data.toInitiativeDto
import com.terranullius.yellowheart.other.Constants.COLLECTION_INITIATIVE
import kotlinx.coroutines.tasks.await
import timber.log.Timber

object FirestoreUtils {

    suspend fun getInitiatives(): List<InitiativeDto> {
        val firestore = FirebaseFirestore.getInstance()
        val initiativesCollectionRef = firestore.collection(COLLECTION_INITIATIVE)

        Timber.d(initiativesCollectionRef.get().await().documents.mapNotNull {
            it.toInitiativeDto()
        }.toString())

        return try {
            initiativesCollectionRef.get().await().documents.mapNotNull {
                it.toInitiativeDto()
            }
        } catch (e: Exception) {
            Timber.e("Error: getting Initiatives")
            emptyList()
        }
        //TODO MIGHT GET RETURNED BEFORE....
    }

}