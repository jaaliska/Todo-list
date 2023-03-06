package com.example.todo_list.presentation.ui.notes_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.todo_list.R
import com.example.todo_list.app.TodoListApp
import com.example.todo_list.databinding.FragmentNotesListBinding
import com.example.todo_list.domain.model.Note
import com.example.todo_list.presentation.ui.base.BaseFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

class NotesListScreen : BaseFragment(), NotesListView {

    @Inject
    lateinit var presenterProvider: Provider<NotesListPresenter>
    private val presenter by moxyPresenter {
        presenterProvider.get()
    }

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as TodoListApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onButtonAddNoteClicked()
        binding.fab.setOnClickListener { _ ->
            val bundle = bundleOf("note" to null)
            findNavController().navigate(R.id.action_NotesListScreen_to_EditNoteScreen, bundle)
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

    override fun showEmptyScreen() {
        binding.textviewNoNotes.isVisible = true
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