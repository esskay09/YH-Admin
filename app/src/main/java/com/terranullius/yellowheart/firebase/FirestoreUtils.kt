package com.terranullius.yellowheart.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.terranullius.yellowheart.data.InitiativeDto
import com.terranullius.yellowheart.data.toInitiativeDto
import com.terranullius.yellowheart.other.Constants.COLLECTION_INITIATIVE
import kotlinx.coroutines.tasks.await

object FirestoreUtils {

    suspend fun getInitiatives(): List<InitiativeDto> {
        val firestore = FirebaseFirestore.getInstance()
        val initiativesCollectionRef = firestore.collection(COLLECTION_INITIATIVE)

        return try {
            initiativesCollectionRef.get().await().documents.mapNotNull {
                it.toInitiativeDto()
            }
        } catch (e: Exception) {
            emptyList()
        }
        //TODO MIGHT GET RETURNED BEFORE....
    }

}