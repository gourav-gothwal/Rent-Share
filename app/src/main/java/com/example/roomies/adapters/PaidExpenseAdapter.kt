package com.example.roomies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomies.R
import com.example.roomies.model.Expense

class PaidExpenseAdapter(
    private var paidExpenseList: List<Expense>
) : RecyclerView.Adapter<PaidExpenseAdapter.PaidExpenseViewHolder>() {

    class PaidExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvExpenseTitle)
        val amount: TextView = itemView.findViewById(R.id.tvExpenseAmount)
        val persons: TextView = itemView.findViewById(R.id.tvNumPersons)
        val paidDate: TextView = itemView.findViewById(R.id.paid_Date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaidExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.paid_expense_item, parent, false)
        return PaidExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaidExpenseViewHolder, position: Int) {
        val expense = paidExpenseList[position]
        holder.title.text = expense.title

        //Show per-person amount
        val perPersonAmount = (expense.amount ?: 0.0) / expense.persons
        holder.amount.text = "₹%.2f".format(perPersonAmount)

        holder.persons.text = "${expense.persons} persons"
        holder.paidDate.text = "Paid: ${expense.paidDate}"
    }

    override fun getItemCount(): Int = paidExpenseList.size

    fun updateList(newList: List<Expense>) {
        paidExpenseList = newList
        notifyDataSetChanged()
    }
}
