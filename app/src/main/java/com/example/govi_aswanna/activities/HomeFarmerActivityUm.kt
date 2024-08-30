package com.example.govi_aswanna.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.govi_aswanna.models.User
import com.example.govi_aswanna.databinding.ActivityHomeFarmerUmBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFarmerActivityUm : AppCompatActivity() {

    private lateinit var binding : ActivityHomeFarmerUmBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeFarmerUmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.profileButtonUserHome.setOnClickListener {
            val intent = Intent(this, UserProfileActivityUM::class.java)
            startActivity(intent)
        }

        binding.marketPlaceFarmerUM.setOnClickListener {
            val intent = Intent(this, FetchingActivity_mp::class.java)
            startActivity(intent)
        }

        binding.logOutFarmerHomeUM.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivityUM::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonEditProfileFarmer.setOnClickListener {
            val intent = Intent(this, UpdateAccountActivityUM::class.java)
            startActivity(intent)
        }

        binding.marketManagementUM.setOnClickListener {
            val intent = Intent(this, HomeActivity_mp::class.java)
            startActivity(intent)
        }

        val database = FirebaseDatabase.getInstance().getReference("Users")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var numFarmers = 0
                var numBuyers = 0
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (user.accountType == "Farmer") {
                            numFarmers++
                        } else if (user.accountType == "Buyer") {
                            numBuyers++
                        }
                    }
                }

                // Display the number of farmers and buyers
                binding.totalFarmersFarmerUM.text = "Total number of Farmers in Govi Aswanna : $numFarmers"
                binding.totalBuyersFarmerUM.text = "Total number of Buyers in Govi Aswanna : $numBuyers"
            }


            override fun onCancelled(error: DatabaseError) {
                // Handle database errors
                Toast.makeText(applicationContext, "Database Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })

    }
}