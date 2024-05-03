package com.example.assignment10.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.assignment10.R
import com.example.assignment10.databinding.ActivityEditContactBinding
import com.example.assignment10.model.Contact
import com.example.assignment10.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable

@AndroidEntryPoint
class EditContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditContactBinding
    private val compositeDisposable = CompositeDisposable()
    private val viewModel: ContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val contact = intent?.getParcelableExtra<Contact>("DATA")
        binding.etName.text= Editable.Factory.getInstance().newEditable(contact?.name)
        binding.etEmail.text= Editable.Factory.getInstance().newEditable(contact?.email)
        binding.etPhoneNumber.text= Editable.Factory.getInstance().newEditable(contact?.phoneNumber)
//        Glide.with(binding.imageView.context)
//            .load(contact?.image ?: "")
//            .error(R.drawable.profile)
//            .into(binding.imageView);
//        binding.btSave.setOnClickListener{finish()}

        binding.btSave.setOnClickListener{
//            val newContact = Contact(binding.etName.text.toString(), binding.etEmail.text.toString(),  binding.etPhoneNumber.text.toString())
            contact?.name = binding.etName.text.toString()
            contact?.email = binding.etEmail.text.toString()
            contact?.phoneNumber = binding.etPhoneNumber.text.toString()
            updateContact(it,contact)
            val resultIntent = Intent().apply {
                putExtra("EDIT_DATA", contact)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
    private fun updateContact(it: View?, contact:Contact?) {
        if (contact != null) {
            val disposable = viewModel.updateContact(contact)
                .subscribe(
                    {
                        // OnNext - Success
                        Toast.makeText(
                            this,
                            resources.getString(R.string.update_contact_success),
                            Toast.LENGTH_SHORT
                        )
                    },
                    { error ->
                        Toast.makeText(
                            this,
                            resources.getString(R.string.update_fail),
                            Toast.LENGTH_SHORT
                        )
                    }
                )
            compositeDisposable.add(disposable)
        }
    }

}