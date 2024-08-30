package com.example.govi_aswanna.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.govi_aswanna.models.User
import com.example.govi_aswanna.databinding.ActivityUserprofileUmBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserProfileActivityUM : AppCompatActivity() {

    private lateinit var binding: ActivityUserprofileUmBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserprofileUmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Authentication and get the current user
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        // Check if a user is currently logged in
        if (currentUser != null) {
            val userEmail = currentUser.email
            val dbRef = FirebaseDatabase.getInstance().getReference("Users")
            dbRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Get the user data from the snapshot
                        val user = snapshot.children.first().getValue(User::class.java)
                        // Set the user data to the views in the activity
                        binding.fullNameProfileUM.text = user?.fullName
                        binding.emailProfileUM.text = user?.email
                        binding.phoneProfileUM.text = user?.phone
                        binding.fullNameHProfileUM.text = user?.fullName
                        binding.nicProfileUM.text = user?.nic
                        binding.addressProfileUM.text = user?.address
                    } else {
                        // No user found with the given email
                        Toast.makeText(applicationContext, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                    Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // User is not logged in, so redirect to the login activity
            val intent = Intent(this, LoginActivityUM::class.java)
            startActivity(intent)
            finish()
        }

        binding.updateNavProfileUM.setOnClickListener {
            val intent = Intent(this, UpdateAccountActivityUM::class.java)
            startActivity(intent)
        }

        binding.logOutProfile.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivityUM::class.java)
            startActivity(intent)
            finish()
        }

        binding.deleteProfileUM.setOnClickListener {
            // Initialize Firebase Authentication and get the current user
            val currentUser = FirebaseAuth.getInstance().currentUser
            // Check if a user is currently logged in
            if (currentUser != null) {
                val userEmail = currentUser.email
                val dbRef = FirebaseDatabase.getInstance().getReference("Users")
                dbRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Get the user ID from the snapshot
                            val userId = snapshot.children.first().key
                            // Delete the user from the database
                            dbRef.child(userId!!).removeValue()
                            // Sign out the user
                            FirebaseAuth.getInstance().signOut()
                            // Delete the user from Firebase Authentication
                            currentUser.delete().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Redirect to the login activity
                                    val intent = Intent(this@UserProfileActivityUM, LoginActivityUM::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // Handle the error
                                    Toast.makeText(applicationContext, "Failed to delete user account", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            // No user found with the given email
                            Toast.makeText(applicationContext, "User not found", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle the error
                        Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }


    }

}


