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
import com.example.todo_list.presentation.ui.note_editing.EditNoteScreen
import com.example.todo_list.presentation.ui.notes_list.adapter.NotesListAdapter
import com.example.todo_list.presentation.ui.notes_list.view.FoldingListView
import com.example.todo_list.presentation.ui.notes_list.view.ListCustomView
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

    private lateinit var completedNotesView: ListCustomView<NotesListView.Item>
    private lateinit var uncompletedNotesListAdapter: NotesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().applicationContext
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)

        uncompletedNotesListAdapter = createListAdapter()
        binding.uncompletedNotes.adapter = uncompletedNotesListAdapter

        completedNotesView = FoldingListView(
            presenter::onCompletedNotesPanelClicked,
            createListAdapter()
        ).also {
            binding.completedNotesContainer.addView(it.build(binding.completedNotesContainer))
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        TodoListApp.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createNoteFab.setOnClickListener {
            presenter.onButtonAddNoteClicked()
        }
    }

    override fun setNotes(
        uncompleted: List<NotesListView.Item>,
        completed: List<NotesListView.Item>,
        // For the case when completed notes panel is hidden
        isThereCompletedNotes: Boolean
    ) {
        uncompletedNotesListAdapter.submitList(uncompleted)
        binding.completedNotesContainer.isVisible = isThereCompletedNotes
        completedNotesView.submitList(completed)
        binding.noNotesPrompt.isVisible =
            !isThereCompletedNotes && uncompleted.isEmpty()
    }

    override fun setCompletedNotesPanelState(isOpen: Boolean) {
        completedNotesView.isPanelOpen = isOpen
    }

    override fun goToEditNoteScreen(note: Note?) {
        val bundle = bundleOf(EditNoteScreen.KEY_NOTE to note)
        findNavController().navigate(R.id.action_NotesListScreen_to_EditNoteScreen, bundle)
    }

    private fun createListAdapter() = NotesListAdapter(
        { item -> presenter.onNoteClicked(item.id) },
        { item, isChecked -> presenter.onNoteCheckboxClicked(item.id, isChecked) }
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}