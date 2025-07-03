package com.example.roomies.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.roomies.LoginActivity
import com.example.roomies.R
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.widget.Toast

class ProfilePage : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var nameText: TextView
    private lateinit var emailText: TextView
    private lateinit var profileImage: ImageView
    private lateinit var logoutText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // View bindings
        nameText = view.findViewById(R.id.textView3)
        emailText = view.findViewById(R.id.textView4)
        profileImage = view.findViewById(R.id.imageView6)
        logoutText = view.findViewById(R.id.textView5)

        val backButton = view.findViewById<ImageView>(R.id.imageView)
        val editProfile = view.findViewById<TextView>(R.id.textView9)
        val darkMode = view.findViewById<TextView>(R.id.textView8)
        val terms = view.findViewById<TextView>(R.id.textView6)
        val privacy = view.findViewById<TextView>(R.id.textView7)

        // Load user data
        auth.currentUser?.let { user ->
            nameText.text = user.displayName ?: "User"
            emailText.text = user.email ?: "No email"

            user.photoUrl?.let { url ->
                Glide.with(this)
                    .load(url)
                    .circleCrop()
                    .into(profileImage)
            }
        }

        // Logout logic
        logoutText.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        // Optional actions
        editProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Edit Profile clicked", Toast.LENGTH_SHORT).show()
            // Add future navigation to profile editor
        }

        darkMode.setOnClickListener {
            Toast.makeText(requireContext(), "Dark Mode clicked", Toast.LENGTH_SHORT).show()
            // Add toggle logic for theme
        }

        terms.setOnClickListener {
            Toast.makeText(requireContext(), "Terms and Conditions clicked", Toast.LENGTH_SHORT).show()
        }

        privacy.setOnClickListener {
            Toast.makeText(requireContext(), "Privacy Policy clicked", Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}
