package com.example.govi_aswanna.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.govi_aswanna.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeActivity_mp : AppCompatActivity() {

    private lateinit var btnInsertData: Button
    private lateinit var btnFetchData: Button
    private lateinit var tvItemCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_mp)

        btnInsertData = findViewById(R.id.btnInsertData)
        btnFetchData = findViewById(R.id.btnFetchData)
        tvItemCount = findViewById(R.id.tvItemCount)

        btnInsertData.setOnClickListener {
            val intent = Intent(this, InsertionActivity_mp::class.java)
            startActivity(intent)
        }

        btnFetchData.setOnClickListener {
            val intent = Intent(this, FetchingActivity_mp::class.java)
            startActivity(intent)
        }

        // Get a reference to the Firebase database
        val database = FirebaseDatabase.getInstance().reference

        // Count the number of children in the "data" node
        database.child("data").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val count = snapshot.childrenCount
                tvItemCount.text = "Number of items: $count"
            } else {
                tvItemCount.text = "No items found"
            }
        }.addOnFailureListener {
            Log.e("HomeActivity_mp", "Failed to read value.", it)
        }

    }
}
