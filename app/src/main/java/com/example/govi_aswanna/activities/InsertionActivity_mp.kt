package com.example.govi_aswanna.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.govi_aswanna.R
import com.example.govi_aswanna.models.FoodModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class     InsertionActivity_mp : AppCompatActivity() {

    //
    private lateinit var spinner: Spinner
//

    private lateinit var etFoodName: EditText
    private lateinit var etqty: EditText
    private lateinit var etPrice: EditText
    private lateinit var etsaller: EditText
    private lateinit var btnSaveData: Button

    //DB ref
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion_mp)
//
        spinner = findViewById(R.id.spinner)

        val options = arrayOf("Select an option from the list","Vegetable", "Fruits", "Grains", "Foods", "Dairy")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

//

        etFoodName = findViewById(R.id.etFoodName)
        etqty = findViewById(R.id.etqty)
        etPrice = findViewById(R.id.etPrice)
        etsaller = findViewById(R.id.etsaller)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Foods")

        btnSaveData.setOnClickListener {
            saveFoodData()
        }
    }

    private fun saveFoodData() {
        // User Input
        val foodName = etFoodName.text.toString()
        val qty = etqty.text.toString()
        val price = etPrice.text.toString()
        val seller = etsaller.text.toString()

        // Spinner Input
        val type = spinner.selectedItem.toString()

        // Input Validation
        var isValid = true
        if (foodName.isEmpty()) {
            etFoodName.error = "Please enter Food Name"
            isValid = false
        }
        if (qty.isEmpty()) {
            etqty.error = "Please enter Quantity"
            isValid = false
        }
        if (price.isEmpty()) {
            etPrice.error = "Please enter Price"
            isValid = false
        }
        if (seller.isEmpty()) {
            etsaller.error = "Please enter Saller Name"
            isValid = false
        }
        if (type.isEmpty()) {
            Toast.makeText(this, "Please select a food type", Toast.LENGTH_LONG).show()
            isValid = false
        }

        if (isValid) {
            // Get Id from Firebase DB
            val foodId = dbRef.push().key!!

            val food = FoodModel(foodId, foodName, qty, price, seller, type)

            // Ref and create child by id
            dbRef.child(foodId).setValue(food)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                    etFoodName.text.clear()
                    etqty.text.clear()
                    etPrice.text.clear()
                    etsaller.text.clear()


                    // Go back to Home Page
                    val intent = Intent(this, HomeActivity_mp::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}