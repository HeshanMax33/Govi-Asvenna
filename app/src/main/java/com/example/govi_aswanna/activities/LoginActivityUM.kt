package com.example.govi_aswanna.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.govi_aswanna.R
import androidx.appcompat.app.AppCompatActivity
import com.example.govi_aswanna.models.User
import com.example.govi_aswanna.databinding.ActivityLoginUmBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class LoginActivityUM : AppCompatActivity() {

    private lateinit var binding: ActivityLoginUmBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var accountType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginUmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        binding.signupNav.setOnClickListener {
            val intent = Intent(this, SignupActivityUM::class.java)
            startActivity(intent)
        }

        val accountTypeSpinner = binding.accountTypeSpinner
        val accountTypeAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.account_types, android.R.layout.simple_spinner_item
        )
        accountTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        accountTypeSpinner.adapter = accountTypeAdapter
        accountTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                accountType = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        binding.buttonLogIn.setOnClickListener {
            val email = binding.logInEmail.text.toString()
            val password = binding.logInPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            // Check if user exists in database and has the correct account type
                            val currentUser = FirebaseAuth.getInstance().currentUser

                            Log.i("Email", email.toString())
                            Log.i("Account Type", accountType.toString())
                            if (currentUser != null) {
                                val userQuery = databaseReference.orderByChild("email").equalTo(email)
                                Log.i("TAG", userQuery.toString())
                                userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        Log.i("Override", email.toString())
                                        if (snapshot.exists()) {
                                            Log.i("Snapshot", email.toString())
                                            for (data in snapshot.children) {
                                                val user = data.getValue(User::class.java)
                                                Log.i("TAG", user.toString())
                                                if (user?.accountType == accountType) {
                                                    // Redirect based on the selected account type
                                                    when (accountType) {
                                                        "Farmer" -> {
                                                            val intent = Intent(
                                                                this@LoginActivityUM,
                                                                HomeFarmerActivityUm::class.java
                                                            )
                                                            startActivity(intent)
                                                        }
                                                        "Buyer" -> {
                                                            val intent = Intent(
                                                                this@LoginActivityUM,
                                                                HomeBuyerActivityUM::class.java
                                                            )
                                                            startActivity(intent)
                                                        }
                                                    }
                                                } else {
                                                    // User does not have the correct account type
                                                    Toast.makeText(
                                                        this@LoginActivityUM,
                                                        "You do not have access to this account type",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        } else {
                                            // User does not exist in database
                                            Toast.makeText(
                                                this@LoginActivityUM,
                                                "User not found",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        // Failed to read value
                                        Log.w(TAG, "Failed to read value.", error.toException())
                                    }
                                })
                            } else {
                                // Current user is null
                                Toast.makeText(
                                    this@LoginActivityUM,
                                    "User not found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // Login failed
                            Toast.makeText(
                                this@LoginActivityUM,
                                "Details Incorrect",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
            }
        }
    }
}