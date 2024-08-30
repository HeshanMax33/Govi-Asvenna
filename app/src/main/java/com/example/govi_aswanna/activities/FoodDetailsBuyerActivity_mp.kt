package com.example.govi_aswanna.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.govi_aswanna.R
import com.example.govi_aswanna.models.FoodModel
import com.google.firebase.database.FirebaseDatabase

class FoodDetailsBuyerActivity_mp : AppCompatActivity() {

    private lateinit var tvId: TextView
    private lateinit var tvfoodName: TextView
    private lateinit var tvqty: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvseller: TextView
    private lateinit var tvSpinner: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details_buyer_mp)

        initView()
        setValuesToViews()

        //Update Button
        btnUpdate = findViewById(R.id.btnUpdate)


        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("foodId").toString(),
                intent.getStringExtra("foodName").toString()
            )
        }
//        //Delete Button
//        btnDelete = findViewById(R.id.btnDelete)
//
//        btnDelete.setOnClickListener {
//            deleteRecord(intent.getStringExtra("foodId").toString())
//        }
    }

    //Delete Implementation
    private fun deleteRecord(id: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Foods").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Your Marketplace Data Deleted", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FetchingActivity_mp::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Data Deleting Err ${error.message}", Toast.LENGTH_SHORT).show()

        }
    }

    private fun initView() {
        tvSpinner = findViewById(R.id.tvSpinner)
        tvId = findViewById(R.id.tvId)
        tvfoodName = findViewById(R.id.tvfoodName)
        tvqty = findViewById(R.id.tvqty)
        tvPrice = findViewById(R.id.tvPrice)
        tvseller = findViewById(R.id.tvsaller)
    }

    private fun setValuesToViews() {
        tvSpinner.text = intent.getStringExtra("type")
        tvId.text = intent.getStringExtra("foodId")
        tvfoodName.text = intent.getStringExtra("foodName")
        tvqty.text = intent.getStringExtra("qty")
        tvPrice.text = intent.getStringExtra("price")
        tvseller.text = intent.getStringExtra("seller")
        val data:String?
        data = tvfoodName?.toString()

        Toast.makeText(this," data : ${tvfoodName.text}", Toast.LENGTH_SHORT).show()
    }

    private fun openUpdateDialog(
        foodId: String,
        foodName: String
    ) {
        val updateDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val updateDialogView = inflater.inflate(R.layout.update_marketplace_mp, null)

        updateDialog.setView(updateDialogView)

        val etFoodName = updateDialogView.findViewById<EditText>(R.id.etFoodName)
        val etqty = updateDialogView.findViewById<EditText>(R.id.etqty)
        val etPrice = updateDialogView.findViewById<EditText>(R.id.etPrice)
        // val etsaller = updateDialogView.findViewById<EditText>(R.id.etsaller)
        val btnUpdateData = updateDialogView.findViewById<Button>(R.id.btnUpdateData)

        etFoodName.setText(intent.getStringExtra("foodName").toString())
        etqty.setText(intent.getStringExtra("qty").toString())
        etPrice.setText(intent.getStringExtra("price").toString())

        updateDialog.setTitle("Updating $foodName Record")
        val alertDialog = updateDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            //Pass the update value
            updateMarketplaceData(
                foodId,
                etFoodName.text.toString(),
                etqty.text.toString(),
                etPrice.text.toString()
            )

            Toast.makeText(
                applicationContext,
                "New Market Place Data Updated", Toast.LENGTH_LONG)
                .show()

            //Show Updated Data
            tvfoodName.text = etFoodName.text.toString()
            tvqty.text = etqty.text.toString()
            tvPrice.text = etPrice.text.toString()

            alertDialog.dismiss()
        }
    }
    //Updated Value passing
    private fun updateMarketplaceData(
        id: String,
        name: String,
        quy: String,
        price: String

    ){

        val dbRef = FirebaseDatabase.getInstance().getReference("Foods").child(id)
        val marketInfo = FoodModel(id, name, quy, price)

        dbRef.setValue(marketInfo)


    }


}