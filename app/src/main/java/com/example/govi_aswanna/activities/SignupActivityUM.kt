package com.example.govi_aswanna.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.govi_aswanna.R
import com.example.govi_aswanna.models.User
import com.example.govi_aswanna.databinding.ActivitySignupUmBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignupActivityUM : AppCompatActivity() {

    private lateinit var binding: ActivitySignupUmBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private var accountType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupUmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        binding.loginNav.setOnClickListener {
            val intent = Intent(this, LoginActivityUM::class.java)
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

        binding.button8.setOnClickListener {
            val fullName = binding.fullNameUM.text.toString()
            val email = binding.emailUM.text.toString()
            val nic = binding.nicUM.text.toString()
            val password = binding.passwordUM.text.toString()
            val phone = binding.phoneUM.text.toString()
            val address = binding.addressUM.text.toString()

            if (fullName.isNotEmpty() && email.isNotEmpty() && nic.isNotEmpty() && password.isNotEmpty()
                && phone.isNotEmpty() && address.isNotEmpty()) {

                val user = User(fullName, email, nic, password, phone, accountType, address)
                dbRef.child(nic).setValue(user)

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

                    if (it.isSuccessful) {
                        val intent = Intent(this, LoginActivityUM::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Cannot leave empty fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
