package com.example.assignment10.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.assignment10.R
import com.example.assignment10.databinding.ActivityAddContactBinding
import com.example.assignment10.model.Contact
import com.example.assignment10.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class AddContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddContactBinding
    private val compositeDisposable = CompositeDisposable()
    private val viewModel: ContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btSubmit.setOnClickListener {
            createContact(it)
        }


    }

    private fun createContact(it: View?) {
        val name = binding.etName.text.toString()
        val phoneNumber = binding.etPhoneNumber.text.toString()
        val email = binding.etEmail.text.toString()
//      val image = binding.etImage.?.text.toString()
        val data = Contact(name = name, email = email, phoneNumber = phoneNumber)
        val disposable = viewModel.insertContact(data).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
            Toast.makeText(
                this,
                resources.getString(R.string.add_contact_success),
                Toast.LENGTH_SHORT
            )
                .show()
        }, {
            Toast.makeText(
                this,
                resources.getString(R.string.add_fail),
                Toast.LENGTH_SHORT
            ).show()
        })
        compositeDisposable.add(disposable)
        startActivity(Intent(this@AddContactActivity, MainActivity::class.java))
    }

}