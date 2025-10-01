package com.f8fit.bambutestandroid.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


sealed class AuthResultState {
    object Loading: AuthResultState()
    data class Success(val userId: String): AuthResultState()
    data class Error(val message: String?): AuthResultState()
}


class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {


    suspend fun register(name: String, email: String, password: String): AuthResultState {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user

            user?.updateProfile(
                userProfileChangeRequest {
                    displayName = name
                }
            )?.await()

            val uid = user?.uid ?: ""
            AuthResultState.Success(uid)
        } catch (e: Exception) {
            AuthResultState.Error(e.localizedMessage)
        }
    }



    suspend fun login(email: String, password: String): AuthResultState {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: ""
            AuthResultState.Success(uid)
        } catch (e: Exception) {
            AuthResultState.Error(e.localizedMessage)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun currentUserId(): String? = firebaseAuth.currentUser?.uid
}