package com.example.scheduleapp.models

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot

interface FirebaseRepository {
    abstract fun downloadDB(): Task<DataSnapshot>

    abstract fun downloadDBReference(reference: String): Task<DataSnapshot>

    abstract fun getCurrentUser(): FirebaseUser?

    abstract fun sendResetMessage(email: String): Task<Void>

    abstract fun signIn(email: String, password: String, newAccount: Boolean): Task<AuthResult>

    abstract fun signOut()
}