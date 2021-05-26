package com.lessons.films.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lessons.films.R
import com.lessons.films.databinding.ItemFilmBinding
import com.lessons.films.model.Film
import java.text.SimpleDateFormat

interface OnFilmClicked {
    fun onFilmClicked(film: Film)
    fun onFavoriteReverse(film: Film)
}

class FilmsAdapter : RecyclerView.Adapter<FilmsAdapter.FilmsViewHolder>() {
    private var films: MutableList<Film> = mutableListOf()
    private var itemBinding: ItemFilmBinding? = null
    var clickListener: OnFilmClicked? = null

    inner class FilmsViewHolder(private val itemBinding: ItemFilmBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private val format = SimpleDateFormat("yyyy")


        init {
            itemBinding.poster.setOnClickListener { clickListener?.onFilmClicked(films.get(getAdapterPosition())) }
            itemBinding.favoriteImg.setOnClickListener { clickListener?.onFavoriteReverse(films.get(getAdapterPosition())) }
        }

        fun bind(film: Film) {
            itemBinding.title.setText(film.name)
            itemBinding.tvReleaseDate.setText(format.format(film.releaseDate))

            if (film.voteAverage == 0.0)
                itemBinding.imgStar.visibility = View.GONE
            else
                itemBinding.tvVote.setText(film.voteAverage.toString())
            if (film.favorite)
                itemBinding.favoriteImg.setImageResource(R.drawable.favorite_fill_24)
            Glide.with(itemBinding.poster)
                    .load(film.poster).centerCrop()
                    .into(itemBinding.poster)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsViewHolder {
        itemBinding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmsViewHolder(itemBinding!!)
    }

    override fun onBindViewHolder(holder: FilmsViewHolder, position: Int) {
        holder.bind(films[position])
    }

    override fun getItemCount() = films.size

    fun setData(films: List<Film>) {
        val callback = FilmsDiffUtil(this.films, films)
        val result = DiffUtil.calculateDiff(callback)
        this.films.clear()
        this.films.addAll(films)
        result.dispatchUpdatesTo(this)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        itemBinding = null
        super.onDetachedFromRecyclerView(recyclerView)
    }
}