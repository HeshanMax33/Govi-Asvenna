package com.example.govi_aswanna.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.govi_aswanna.models.User
import com.example.govi_aswanna.databinding.ActivityUpdateAccountUmBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class UpdateAccountActivityUM : AppCompatActivity() {

    private lateinit var binding : ActivityUpdateAccountUmBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateAccountUmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Authentication and get the current user
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!

        // Check if a user is currently logged in
        if (currentUser != null) {
            val userEmail = currentUser.email
            dbRef = FirebaseDatabase.getInstance().getReference("Users")

            dbRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Get the user data from the snapshot
                        val user = snapshot.children.first().getValue(User::class.java)
                        // Set the user data to the views in the activity
                        binding.fullNameUpdateUM.setText(user?.fullName)
                        binding.emailUpdateUM.setText(user?.email)
                        binding.phoneUpdateUM.setText(user?.phone)
                        binding.fullNameHUpdateUM.text = user?.fullName
                        binding.addressUpdateUM.setText(user?.address)
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

        binding.confirmUpdateUM.setOnClickListener {
            saveChanges()
        }
    }

    private fun saveChanges() {
        val fullName = binding.fullNameUpdateUM.text.toString().trim()
        val email = binding.emailUpdateUM.text.toString().trim()
        val phone = binding.phoneUpdateUM.text.toString().trim()
        val address = binding.addressUpdateUM.text.toString().trim()

        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Update the user's details in the database
        dbRef.orderByChild("email").equalTo(currentUser.email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { dataSnapshot ->
                        val user = dataSnapshot.getValue(User::class.java)
                        user?.let {
                            // Update user details based on the email
                            it.fullName = fullName
                            it.email = email
                            it.phone = phone
                            it.address = address
                            dbRef.child(dataSnapshot.key!!).setValue(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this@UpdateAccountActivityUM, "Changes saved successfully", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@UpdateAccountActivityUM, UserProfileActivityUM::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this@UpdateAccountActivityUM, "Failed to save changes", Toast.LENGTH_SHORT).show()
                                }
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
