package com.example.todo_list.presentation.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.todo_list.R
import com.example.todo_list.app.TodoListApp
import com.example.todo_list.databinding.FragmentMainBinding
import com.example.todo_list.domain.model.Note
import com.example.todo_list.presentation.ui.base.BaseFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

class NoteListScreen : BaseFragment(), NoteListView {

    @Inject
    lateinit var presenterProvider: Provider<NoteListPresenter>
    private val presenter by moxyPresenter {
        presenterProvider.get()
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        TodoListApp.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onButtonAddNoteClicked()
        binding.fab.setOnClickListener { _ ->
            val a = Note(1, "Check this note", false, true)
            val bundle = bundleOf("note" to a)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showUncompletedNotes(list: List<Note>) {
        TODO("Not yet implemented")
    }

    override fun showCompletedNotes(list: List<Note>) {
        TODO("Not yet implemented")
    }

    override fun goEditNoteScreen(note: Note?) {
        TODO("Not yet implemented")
    }

    override fun showProgressDialog() {
        TODO("Not yet implemented")
    }

    override fun hideProgressDialog() {
        TODO("Not yet implemented")
    }

    override fun showErrorToast(error: String?) {
        TODO("Not yet implemented")
    }
}