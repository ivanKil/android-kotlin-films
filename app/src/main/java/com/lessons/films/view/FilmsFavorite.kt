package com.lessons.films.view

import androidx.recyclerview.widget.GridLayoutManager
import com.lessons.films.R

class FilmsFavorite : FilmsLenta() {
    companion object {
        fun newInstance() = FilmsFavorite()
    }

    override fun setTitle() {
        binding!!.tvTitleList.setText(R.string.favorites)
    }

    override fun requestData() = viewModel.requestFavoritesFilms()

    override fun getRecyclerViewLayout() = GridLayoutManager(context, 2)

}