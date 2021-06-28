package com.lessons.films.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.lessons.films.R
import com.lessons.films.model.FilmDetail

const val ARG_FILM = "ARG_FILM"

class NoteFragment : DialogFragment() {
    lateinit var film: FilmDetail
    protected val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    companion object {
        fun newInstance(param1: FilmDetail): NoteFragment {
            val fragment = NoteFragment()
            val args = Bundle()
            args.putParcelable(ARG_FILM, param1)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            film = requireArguments().getParcelable(ARG_FILM)!!
            viewModel.requestFilmNoteFromDb(film.id)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.film_note, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init(view)
    }

    fun init(view: View) {
        var note = view.findViewById<TextInputEditText>(R.id.note_text)
        viewModel.liveDataFilmNote.observe(viewLifecycleOwner) {
            note.setText(it.text)
        }
        note.setText("")
        view.findViewById<MaterialButton>(R.id.save_note_btn)
                .setOnClickListener { viewModel.saveNote(film.id, note.text.toString()).subscribe { dismiss() } }
        view.findViewById<View>(R.id.cancel_edit_btn).setOnClickListener { v: View? -> dismiss() }
    }

    override fun getTheme() = R.style.AlertDialogStyle

}