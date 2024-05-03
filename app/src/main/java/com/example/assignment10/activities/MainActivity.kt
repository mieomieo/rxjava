package com.example.assignment10.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment10.R
import com.example.assignment10.adapter.ContactAdapter
import com.example.assignment10.adapter.ContactSearchAdapter
import com.example.assignment10.databinding.ActivityMainBinding
import com.example.assignment10.model.Contact
import com.example.assignment10.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.Normalizer
import java.util.regex.Pattern
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val contactViewModel: ContactViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val compositeDisposable = CompositeDisposable()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEvents()
        initControls()

    }

    @SuppressLint("CheckResult")
    private fun initControls() {
        val adapterContact = ContactAdapter(this@MainActivity, onItemClick, onItemDelete)
        val adapterSearchContact =
            ContactSearchAdapter(this@MainActivity, onItemClick, onItemDelete)
        binding.rvContacts.setHasFixedSize(true)
        binding.rvContacts.layoutManager = LinearLayoutManager(this)
        binding.rvContacts.adapter = adapterContact

        binding.recyclerViewListSearchContacts.setHasFixedSize(true)
        binding.recyclerViewListSearchContacts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewListSearchContacts.adapter = adapterSearchContact
        val allContactsObservable = contactViewModel.getAllContact()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        binding.searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = getTextSearch(s.toString())
                val allContactsSortedByNameObservable = contactViewModel.getAllContact()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { contacts ->
                        contacts.filter {
                            getTextSearch(it.name).contains(text, ignoreCase = true)
                        }
                    }
                compositeDisposable.add(allContactsSortedByNameObservable.subscribe { filteredContacts ->
                    adapterSearchContact.updateContact(filteredContacts)
                })
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed here
            }
        })
        compositeDisposable.add(allContactsObservable.subscribe { contacts ->
            adapterContact.setContacts(contacts)
        })
    }

    private fun initEvents() {
        binding.btFloat.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivity(intent)

        }
    }


    fun generateRandomEmail(): String {
        val alphabet = "abcdefghijklmnopqrstuvwxyz"
        val nameLength = (5..6).random()
        val randomName = (1..nameLength)
            .map { alphabet.random() }
            .joinToString("")

        return "$randomName@gmail.com"
    }

    fun generateRandomPhoneNumber(): String {
        val countryCode = "+84"
        val areaCode = "${Random.nextInt(100, 1000)}"
        val prefix = "${Random.nextInt(100, 1000)}"
        val lineNumber = "${Random.nextInt(1000, 10000)}"
        return "$countryCode$areaCode$prefix$lineNumber"
    }

    fun generateRandomName(): String {
        val alphabet = "abcdefghijklmnopqrstuvwxyz"
        val nameLength = (5..6).random()
        return (1..nameLength)
            .map { alphabet.random() }
            .joinToString("")
    }

    fun getTextSearch(input: String?): String {
        val nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(nfdNormalizedString).replaceAll("")
    }

    private val onItemClick: (Contact) -> Unit = {
        val intent = Intent(this@MainActivity, DetailContactActivity::class.java)
        intent.putExtra("DETAIL_CONTACT", it)
        startActivity(intent)
    }
    private val onItemDelete: (Contact) -> Unit = {
        val disposable = contactViewModel.deleteContact(it)
            .subscribe(
                {
                    // OnNext - Success
                    Toast.makeText(
                        this,
                        resources.getString(R.string.delete_contact_success),
                        Toast.LENGTH_SHORT
                    )
                },
                { error ->
                    Toast.makeText(
                        this,
                        resources.getString(R.string.delete_fail),
                        Toast.LENGTH_SHORT
                    )
                }
            )
        compositeDisposable.add(disposable)
    }
}
