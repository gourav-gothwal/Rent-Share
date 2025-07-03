package com.example.roomies.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roomies.R
import com.example.roomies.adapters.ExpenseAdapter
import com.example.roomies.model.Expense
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

class HomePage : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var usernameText: TextView
    private lateinit var totalExpensesText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter
    private val expenseList = mutableListOf<Expense>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home_page, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val profileImage = view.findViewById<ImageView>(R.id.imageView3)
        usernameText = view.findViewById(R.id.textView11)
        totalExpensesText = view.findViewById(R.id.textView14)
        recyclerView = view.findViewById(R.id.new_expense_recyclerView)
        val addExpenseButton = view.findViewById<Button>(R.id.add_expense_button)

        loadUserInfo(profileImage)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ExpenseAdapter(
            expenses = expenseList,
            onPaid = { expense ->
                val paidDate = LocalDate.now().toString()
                db.collection("expenses").document(expense.id)
                    .update(mapOf("isPaid" to true, "paidDate" to paidDate))
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Marked as paid", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to mark as paid", Toast.LENGTH_SHORT).show()
                    }
            },
            onEdit = { expense ->
                val bundle = Bundle().apply {
                    putString("id", expense.id)
                    putString("title", expense.title)
                    putDouble("amount", expense.amount)
                    putInt("persons", expense.persons)
                    putString("dueDate", expense.dueDate)
                }

                val addExpensePage = AddExpensePage().apply { arguments = bundle }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, addExpensePage)
                    .addToBackStack(null)
                    .commit()
            },
            onDelete = { expense ->
                db.collection("expenses").document(expense.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Expense deleted", Toast.LENGTH_SHORT).show()
                        loadExpenses()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to delete", Toast.LENGTH_SHORT).show()
                    }
            }
        )
        recyclerView.adapter = adapter

        loadExpenses()

        addExpenseButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, AddExpensePage())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<ImageView>(R.id.imageView4).setOnClickListener {
            Toast.makeText(requireContext(), "Notifications clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadExpenses()
    }

    private fun loadUserInfo(profileImage: ImageView) {
        val user = auth.currentUser
        user?.let {
            usernameText.text = it.displayName ?: it.email ?: "User"
            it.photoUrl?.let { url ->
                Glide.with(this)
                    .load(url)
                    .circleCrop()
                    .into(profileImage)
            }
        }
    }

    private fun loadExpenses() {
        db.collection("expenses")
            .whereEqualTo("isPaid", false)
            .get()
            .addOnSuccessListener { snapshot ->
                expenseList.clear()
                var total = 0.0
                for (doc in snapshot) {
                    val id = doc.id
                    val title = doc.getString("title") ?: "Untitled"
                    val amount = doc.getDouble("amount") ?: 0.0
                    val persons = doc.getLong("persons")?.toInt() ?: 1
                    val dueDate = doc.getString("dueDate") ?: ""
                    val isPaid = doc.getBoolean("isPaid") ?: false
                    val paidDate = doc.getString("paidDate") ?: ""

                    val expense = Expense(
                        id = id,
                        title = title,
                        amount = amount,
                        persons = persons,
                        dueDate = dueDate,
                        isPaid = isPaid,
                        paidDate = paidDate
                    )

                    expenseList.add(expense)
                    total += amount / persons
                }
                adapter.notifyDataSetChanged()
                totalExpensesText.text = "₹ %.2f".format(total)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load expenses", Toast.LENGTH_SHORT).show()
            }
    }

}
