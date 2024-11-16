package com.example.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.databinding.ItemContactBinding
import com.example.contacts.model.Contact

interface ContactsActionListener {
    fun intentCall(contact: Contact)

    fun intentSMS(contact: Contact)
}

class ContactsAdapter(
        private val actionListener: ContactsActionListener
) : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>(), View.OnClickListener {

    var contacts: List<Contact> = emptyList()
        set(newVal) {
            field = newVal
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        binding.messageButton.setOnClickListener(this)
        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = contacts[position]
        with(holder.binding) {
            holder.itemView.tag = contact
            messageButton.tag = contact
            nameText.text = contact.name
            numberText.text = contact.phoneNumber
        }
    }

    override fun getItemCount(): Int = contacts.size

    class ContactsViewHolder(
        val binding: ItemContactBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(view: View) {
        val contact = view.tag as Contact
        when (view.id) {
            R.id.message_button -> {
                actionListener.intentSMS(contact)
            }
            else -> {
                actionListener.intentCall(contact)
            }
        }
    }
}