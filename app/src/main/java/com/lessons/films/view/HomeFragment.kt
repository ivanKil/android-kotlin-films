package com.lessons.films.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lessons.films.databinding.FilmsFragmentBinding

class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FilmsFragmentBinding.inflate(inflater, container, false);
        return binding!!.root;
    }
}