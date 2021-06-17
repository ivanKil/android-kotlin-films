package com.lessons.films.view.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lessons.films.databinding.ItemContactBinding
import com.lessons.films.model.FilmContact

interface OnContactClicked {
    fun onContactClicked(film: FilmContact)
}

class ContactsAdapter() : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {
    private var contacts: MutableList<FilmContact> = mutableListOf()
    private var itemBinding: ItemContactBinding? = null
    var clickListener: OnContactClicked? = null


    inner class ContactsViewHolder(private val itemBinding: ItemContactBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemBinding.contactPhone.setOnClickListener { clickListener?.onContactClicked(contacts.get(getAdapterPosition())) }
        }

        fun bind(film: FilmContact) {
            with(itemBinding) {
                contactName.text = film.name
                contactPhone.text = film.phones.elementAtOrElse(0) { "" }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsAdapter.ContactsViewHolder {
        itemBinding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(itemBinding!!)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) = holder.bind(contacts[position])

    override fun getItemCount() = contacts.size

    fun setData(films: List<FilmContact>) {
        this.contacts.clear()
        this.contacts.addAll(films)
        notifyDataSetChanged()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        itemBinding = null
        super.onDetachedFromRecyclerView(recyclerView)
    }

    fun removeListener() {
        clickListener = null
    }
}