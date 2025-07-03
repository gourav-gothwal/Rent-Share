package com.example.roomies.adapters

import android.animation.*
import android.app.AlertDialog
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.roomies.R
import com.example.roomies.model.Expense
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter(
    private val expenses: MutableList<Expense>,
    private val onPaid: (Expense) -> Unit,
    private val onEdit: (Expense) -> Unit,
    private val onDelete: (Expense) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    inner class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.cardView)
        val container: ConstraintLayout = view.findViewById(R.id.card_container)
        val titleText: TextView = view.findViewById(R.id.tvExpenseTitle)
        val amountText: TextView = view.findViewById(R.id.tvExpenseAmount)
        val dueDateText: TextView = view.findViewById(R.id.paid_Date)
        val personsText: TextView = view.findViewById(R.id.tvNumPersons)
        val paidTag: TextView = view.findViewById(R.id.paid_tag)
        val paidCheckBox: CheckBox? = view.findViewById(R.id.checkbox_paid) // only available in unpaid layout
    }

    override fun getItemViewType(position: Int): Int {
        return if (expenses[position].isPaid) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val layout = if (viewType == 1) {
            R.layout.paid_expense_item
        } else {
            R.layout.add_expense_item
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        val context = holder.itemView.context

        holder.titleText.text = expense.title
        val share = (expense.amount ?: 0.0) / expense.persons
        holder.amountText.text = "₹%.2f".format(share)
        holder.dueDateText.text = if (expense.isPaid) "Paid: ${expense.paidDate}" else "Due: ${expense.dueDate}"
        holder.personsText.text = "${expense.persons} persons"
        holder.paidTag.visibility = if (expense.isPaid) View.VISIBLE else View.GONE

        // Only handle checkbox if present (new_expense_item)
        holder.paidCheckBox?.apply {
            visibility = View.VISIBLE
            isChecked = false
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && !expense.isPaid) {
                    val colorFrom = Color.WHITE
                    val colorTo = ContextCompat.getColor(context, R.color.green)
                    val anim = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
                    anim.duration = 1000
                    anim.addUpdateListener { v ->
                        holder.container.setBackgroundColor(v.animatedValue as Int)
                    }

                    holder.paidTag.visibility = View.VISIBLE
                    holder.paidTag.setTextColor(Color.WHITE)
                    holder.dueDateText.text = "Paid"

                    anim.start()

                    Handler(Looper.getMainLooper()).postDelayed({
                        val pos = holder.adapterPosition
                        if (pos != RecyclerView.NO_POSITION) {
                            val paidDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                            expense.isPaid = true
                            expense.paidDate = paidDate
                            onPaid(expense)
                            expenses.removeAt(pos)
                            notifyItemRemoved(pos)
                        }
                    }, 600)
                }
            }
        }

        // Long press to edit/delete
        holder.card.setOnLongClickListener {
            val popup = PopupMenu(context, holder.card)
            popup.inflate(R.menu.expense_options_menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        onEdit(expense)
                        true
                    }
                    R.id.menu_delete -> {
                        AlertDialog.Builder(context)
                            .setTitle("Delete Expense")
                            .setMessage("Are you sure you want to delete this expense?")
                            .setPositiveButton("Yes") { _, _ -> onDelete(expense) }
                            .setNegativeButton("Cancel", null)
                            .show()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
            true
        }
    }

    override fun getItemCount(): Int = expenses.size
}
