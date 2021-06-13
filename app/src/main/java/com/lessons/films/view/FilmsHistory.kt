package com.lessons.films.view

import androidx.recyclerview.widget.GridLayoutManager
import com.lessons.films.R

class FilmsHistory : FilmsLenta() {
    override fun setTitle() = binding!!.tvTitleList.setText(R.string.history)

    override fun requestData() = viewModel.requestFilmsHistory()

    override fun setObserve() =
            viewModel.liveDataFilmsHistory.observe(viewLifecycleOwner) {
                appState = it
                renderData(it)
            }

    override fun getRecyclerViewLayout() = GridLayoutManager(context, 4)

}