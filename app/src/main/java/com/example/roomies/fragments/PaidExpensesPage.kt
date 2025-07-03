package com.example.roomies.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomies.R
import com.example.roomies.adapters.PaidExpenseAdapter
import com.example.roomies.model.Expense
import com.google.firebase.firestore.FirebaseFirestore

class PaidExpensesPage : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PaidExpenseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_paid_expenses_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()
        recyclerView = view.findViewById(R.id.paid_expenses_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = PaidExpenseAdapter(emptyList())
        recyclerView.adapter = adapter

        loadPaidExpenses()
    }

    private fun loadPaidExpenses() {
        db.collection("expenses")
            .whereEqualTo("isPaid", true)
            .get()
            .addOnSuccessListener { snapshot ->
                val paidExpenses = snapshot.mapNotNull {
                    it.toObject(Expense::class.java)
                }
                adapter.updateList(paidExpenses)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to load: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
