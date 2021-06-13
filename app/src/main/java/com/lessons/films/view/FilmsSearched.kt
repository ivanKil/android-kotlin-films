package com.lessons.films.view

import androidx.recyclerview.widget.GridLayoutManager
import com.lessons.films.R

class FilmsSearched : FilmsLenta() {
    override fun setTitle() = binding!!.tvTitleList.setText(R.string.search_result)
    override fun requestData() = viewModel.searchFilms("")
    override fun getRecyclerViewLayout() = GridLayoutManager(context, 3)
    override fun setObserve() =
            viewModel.liveDataSearch.observe(viewLifecycleOwner) {
                appState = it
                renderData(it)
            }
}