package com.lessons.films.view

import com.lessons.films.R

class FilmsUpcoming : FilmsLenta() {
    override fun setTitle() {
        binding!!.tvTitleList.setText(R.string.upcoming)
    }

    override fun requestData() = viewModel.requestUpcomingFilms()
}