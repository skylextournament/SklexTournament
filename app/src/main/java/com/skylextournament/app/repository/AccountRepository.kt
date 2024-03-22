package com.skylextournament.app.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.skylextournament.app.common.model.Account
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val sessionRepository: SessionRepository,
) {
    private val auth by lazy { Firebase.auth }
    private val db by lazy { Firebase.firestore }

    fun createAccount(account: Account, completion: (Result<Unit>) -> Unit) {
        auth.createUserWithEmailAndPassword(account.email, account.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    db.collection(USERS_COLLECTION)
                        .document(account.email)
                        .set(account.toMap())
                        .addOnSuccessListener {
                            completion(Result.success(Unit))
                        }
                        .addOnFailureListener { exception ->
                            completion(Result.failure(exception))
                        }
                } else {
                    val exception = task.exception!!
                    completion(Result.failure(exception))
                }
            }
    }

    fun login(account: Account, completion: (Result<Account>) -> Unit) {
        auth.signInWithEmailAndPassword(account.email, account.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getCurrentAccount(account.email, completion)

                } else {
                    val exception = task.exception!!
                    completion(Result.failure(exception))
                }
            }
    }

    fun logout() {
        auth.signOut()
        sessionRepository.saveUsername("")
        sessionRepository.saveEmail("")
        sessionRepository.saveIsAdmin(false)
    }

    fun getAccount(email: String, completion: (Result<Account>) -> Unit) {
        db.collection(USERS_COLLECTION).document(email).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    document.toObject(Account::class.java)?.let { serializedAccount ->
                        val account = serializedAccount.copy(isAdmin = document.getBoolean("isAdmin") ?: false)
                        completion(Result.success(account))
                    } ?: completion(Result.failure(IllegalArgumentException("Account not exists")))
                } else {
                    completion(Result.failure(IllegalArgumentException("Account not exists")))
                }
            } else {
                val exception = task.exception!!
                completion(Result.failure(exception))
            }
        }
    }

    private fun getCurrentAccount(email: String, completion: (Result<Account>) -> Unit) {
        db.collection(USERS_COLLECTION).document(email).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    document.toObject(Account::class.java)?.let { serializedAccount ->
                        val account = serializedAccount.copy(isAdmin = document.getBoolean("isAdmin") ?: false)
                        sessionRepository.saveUsername(account.nickname)
                        sessionRepository.saveEmail(account.email)
                        sessionRepository.saveIsAdmin(account.isAdmin)
                        completion(Result.success(account))
                    } ?: completion(Result.failure(IllegalArgumentException("Account not exists")))
                } else {
                    completion(Result.failure(IllegalArgumentException("Account not exists")))
                }
            } else {
                val exception = task.exception!!
                completion(Result.failure(exception))
            }
        }
    }

    private fun Account.toMap() = mapOf(
        "email" to email,
        "nickname" to nickname,
        "isAdmin" to isAdmin
    )

    private companion object {
        const val USERS_COLLECTION = "users"
    }
}