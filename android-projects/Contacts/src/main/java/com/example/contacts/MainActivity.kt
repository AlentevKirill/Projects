package com.example.contacts

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.databinding.ActivityMainBinding
import com.example.contacts.model.Contact
import com.example.contacts.model.fetchAllContacts

typealias ContactsListener = (contacts: List<Contact>) -> Unit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ContactsAdapter
    private val listeners = mutableListOf<ContactsListener>()
    private var contacts = mutableListOf<Contact>()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            RQ_FOR_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //getContact()
                    val intent = intent
                    finish()
                    startActivity(intent)
                    //Log.d(TAG, contacts.toString())
                    //Log.d(TAG, "АЛО ГДЕ КОНТАКТЫ")
                } else {
                    if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            !shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)
                        } else {
                            false
                        }
                    ) {
                        Toast.makeText(this, "The permission is required for the application to work," +
                                " you can still give it in the application settings", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Permission is required to get information about contacts," +
                                " without it the application will not be able to work", Toast.LENGTH_LONG).show()
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_CONTACTS),
                            RQ_FOR_READ_CONTACTS)
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) {
            getContact()
        } else {
            ActivityCompat.requestPermissions(
                this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            RQ_FOR_READ_CONTACTS)
            //getContact()
        }
        //contacts = this.fetchAllContacts().toMutableList()
        Log.d(TAG, "АЛО ГДЕ КОНТАКТЫ !!!!!")
        setContentView(binding.root)
        adapter = ContactsAdapter(object : ContactsActionListener {
            override fun intentCall(contact: Contact) {
                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.phoneNumber))
                if (callIntent.resolveActivity(packageManager) != null) {
                    startActivity(callIntent)
                }
            }

            override fun intentSMS(contact: Contact) {
                val message = "I'm busy right now, I'll call you back later"
                val smsIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + contact.phoneNumber))
                smsIntent.putExtra("sms_body", message)
                startActivity(smsIntent)
            }
        })
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        addListener(contactsListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeListener(contactsListener)
    }

    private fun addListener(listener: ContactsListener) {
        listeners.add(listener)
        listener.invoke(contacts)
    }

    private fun removeListener(listener: ContactsListener) {
        listeners.remove(listener)
    }

    /*private fun notifyChanges() {
        listeners.forEach {it.invoke(contacts)}
    }*/

    private val contactsListener: ContactsListener = { adapter.contacts = it }

    private fun getContact() {
        contacts = this.fetchAllContacts().toMutableList()
        val count = contacts.size
        val contactsFound = resources.getQuantityString(R.plurals.numberOfContactsAvailable, count, count)
        Toast.makeText(this, contactsFound, Toast.LENGTH_LONG).show()
    }

    private companion object {
        const val RQ_FOR_READ_CONTACTS = 1
        //const val RQ_FOR_CALL_PHONE = 2
    }
}