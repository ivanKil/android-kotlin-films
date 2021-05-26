package com.lessons.films.view

import androidx.recyclerview.widget.DiffUtil
import com.lessons.films.model.Film

class FilmsDiffUtil(val oldList: List<Film>, val newList: List<Film>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        if (oldList == null)
            return 0;
        return oldList.size
    }

    override fun getNewListSize(): Int {
        if (newList == null)
            return 0;
        return newList.size;
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}