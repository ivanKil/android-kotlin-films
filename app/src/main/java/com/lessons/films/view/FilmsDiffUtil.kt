package com.lessons.films.view

import androidx.recyclerview.widget.DiffUtil
import com.lessons.films.model.Film

class FilmsDiffUtil(private val oldList: List<Film>, private val newList: List<Film>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id
    
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
}