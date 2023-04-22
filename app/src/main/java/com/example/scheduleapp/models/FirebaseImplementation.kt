package com.example.scheduleapp.models

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class FirebaseImplementation(
    private val fDatabase: FirebaseDatabase,
    private val fAuth: FirebaseAuth
) : FirebaseRepository {
    override fun downloadDB(): Task<DataSnapshot> {
        return fDatabase.getReference("").get()
    }

    override fun downloadDBReference(reference: String): Task<DataSnapshot> {
        return fDatabase.getReference(reference).get()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return fAuth.currentUser
    }

    override fun signIn(email: String, password: String, newAccount: Boolean): Task<AuthResult> {
        return if (newAccount) {
            fAuth.createUserWithEmailAndPassword(email, password)
        } else {
            fAuth.signInWithEmailAndPassword(email, password)
        }
    }

    override fun sendResetMessage(email: String): Task<Void> {
        return fAuth.sendPasswordResetEmail(email)
    }

    override fun signOut() {
        fAuth.signOut()
    }
}