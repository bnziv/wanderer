package com.example.wanderer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        val emailTextView: TextView = view.findViewById(R.id.emailTextView)
        val changePasswordButton: Button = view.findViewById(R.id.changePasswordButton)
        val signOutButton: Button = view.findViewById(R.id.signOutButton)

        emailTextView.text = user?.email ?: "No email available"

        changePasswordButton.setOnClickListener {
            val email = user?.email
            if (email != null) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Password reset email sent to $email", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to send reset email", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "No email associated with this account", Toast.LENGTH_SHORT).show()
            }
        }

        signOutButton.setOnClickListener {
            auth.signOut()
            Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }
}
