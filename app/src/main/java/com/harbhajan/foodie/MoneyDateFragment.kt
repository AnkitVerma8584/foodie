package com.harbhajan.foodie

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.util.*


class MoneyDateFragment : DialogFragment() {
    lateinit var txtDate:TextView
    lateinit var imgCalender:ImageView
    lateinit var btnSend:Button
    lateinit var txtTotalOrders:TextView
    lateinit var txtOrderFund:TextView
    lateinit var txtTotalCollection:TextView
    lateinit var txtTotalFund:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_money_date, container, false)
        txtDate=view.findViewById(R.id.txtDate)
        var date:String=""
        imgCalender=view.findViewById(R.id.imgCalender)
        btnSend=view.findViewById(R.id.btnSend)
        txtTotalOrders=view.findViewById(R.id.txtTotalOrders)
        txtOrderFund=view.findViewById(R.id.txtOrderFund)
        txtTotalCollection=view.findViewById(R.id.txtTotalCollection)
        txtTotalFund=view.findViewById(R.id.txtTotalFund)
        val c=Calendar.getInstance()
        val year=c.get(Calendar.YEAR)
        val month=c.get(Calendar.MONTH)
        val day=c.get(Calendar.DAY_OF_MONTH)
        imgCalender.setOnClickListener {
            val dpd = DatePickerDialog(
                activity as Context,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    date="$dayOfMonth/$month/$year"
                    txtDate.text=date
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        btnSend.setOnClickListener {
            if(date==""){
                Toast.makeText(activity as Context,"Please select date",Toast.LENGTH_SHORT).show()
            }else{

            }
        }


        return view
    }

}