package com.example.roomies.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.roomies.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AddExpensePage : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var titleInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var personCountText: TextView
    private lateinit var dueDateText: TextView
    private lateinit var amountPerPersonText: TextView

    private var personCount = 1
    private var selectedDueDate: String = ""
    private var editId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_expense_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        titleInput = view.findViewById(R.id.title_input)
        amountInput = view.findViewById(R.id.amount_input)
        personCountText = view.findViewById(R.id.person_count_text)
        dueDateText = view.findViewById(R.id.selected_due_date)
        amountPerPersonText = view.findViewById(R.id.amount_per_person_text)

        val increaseButton = view.findViewById<ImageButton>(R.id.increase_button)
        val decreaseButton = view.findViewById<ImageButton>(R.id.decrease_button)
        val saveButton = view.findViewById<Button>(R.id.save_expense_button)
        val datePicker = view.findViewById<LinearLayout>(R.id.due_date_picker)

        // Load arguments if editing
        editId = arguments?.getString("id")
        val title = arguments?.getString("title")
        val amount = arguments?.getDouble("amount")
        val persons = arguments?.getInt("persons")
        val dueDate = arguments?.getString("dueDate")

        if (editId != null) {
            titleInput.setText(title)
            amountInput.setText(amount?.toString())
            personCount = persons ?: 1
            personCountText.text = personCount.toString()
            selectedDueDate = dueDate ?: ""
            dueDateText.text = selectedDueDate.ifEmpty { "Select Due Date" }
            updateAmountPerPerson()
        }

        increaseButton.setOnClickListener {
            personCount++
            personCountText.text = personCount.toString()
            updateAmountPerPerson()
        }

        decreaseButton.setOnClickListener {
            if (personCount > 1) {
                personCount--
                personCountText.text = personCount.toString()
                updateAmountPerPerson()
            }
        }

        amountInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) updateAmountPerPerson()
        }

        datePicker.setOnClickListener {
            showDatePicker()
        }

        saveButton.setOnClickListener {
            val titleText = titleInput.text.toString().trim()
            val amountText = amountInput.text.toString().trim()

            if (titleText.isEmpty() || amountText.isEmpty() || selectedDueDate.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amountValue = amountText.toDoubleOrNull()
            if (amountValue == null) {
                Toast.makeText(requireContext(), "Enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expenseData = hashMapOf(
                "title" to titleText,
                "amount" to amountValue,
                "persons" to personCount,
                "dueDate" to selectedDueDate,
                "isPaid" to false,
                "paidDate" to ""
            )

            if (editId != null) {
                db.collection("expenses").document(editId!!)
                    .update(expenseData as Map<String, Any>)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Expense updated", Toast.LENGTH_SHORT).show()
                        returnToHome()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to update expense", Toast.LENGTH_SHORT).show()
                    }
            } else {
                db.collection("expenses")
                    .add(expenseData)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Expense added", Toast.LENGTH_SHORT).show()
                        returnToHome()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to add expense", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun returnToHome() {
        parentFragmentManager.popBackStack() // ✅ Go back to existing HomePage instance
    }

    private fun updateAmountPerPerson() {
        val amount = amountInput.text.toString().toDoubleOrNull() ?: 0.0
        val perPerson = if (personCount == 0) 0.0 else amount / personCount
        amountPerPersonText.text = "Each person pays: ₹%.2f".format(perPerson)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                calendar.set(year, month, dayOfMonth)
                selectedDueDate = sdf.format(calendar.time)
                dueDateText.text = selectedDueDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
