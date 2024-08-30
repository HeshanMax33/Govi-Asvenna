package com.example.govi_aswanna.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.govi_aswanna.R
import com.example.govi_aswanna.models.FoodModel

class foodAdapter(private val foodList: ArrayList<FoodModel>):
        RecyclerView.Adapter<foodAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.food_list_item_mp,parent,false)
        return ViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentFood = foodList[position]
        holder.tvfoodName.text = currentFood.foodName
    }

    override fun getItemCount(): Int {
    return foodList.size
    }
    class ViewHolder(itemView: View, clickListener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val tvfoodName : TextView = itemView.findViewById(R.id.tvfoodName)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }


}
