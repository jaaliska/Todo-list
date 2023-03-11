package com.example.todo_list.presentation.ui.notes_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.todo_list.presentation.utils.ui_kit.ProgressDialog
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

    private var isThereCompletedNotes: Boolean = false
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

    override fun setUncompletedNotes(listItems: List<NotesListView.Item>) {
        uncompletedNotesListAdapter.submitList(listItems)
        updateNoNotesPromptVisibility()
    }

    override fun setCompletedNotes(
        listItems: List<NotesListView.Item>, isThereCompletedNotes: Boolean
    ) {
        this.isThereCompletedNotes = isThereCompletedNotes
        binding.completedNotesContainer.isVisible = isThereCompletedNotes
        completedNotesView.submitList(listItems)
        updateNoNotesPromptVisibility()
    }

    override fun setCompletedNotesPanelState(isOpen: Boolean) {
        completedNotesView.isPanelOpen = isOpen
    }

    override fun goToEditNoteScreen(note: Note?) {
        val bundle = bundleOf(EditNoteScreen.KEY_NOTE to note)
        findNavController().navigate(R.id.action_NotesListScreen_to_EditNoteScreen, bundle)
    }

    override fun showProgressDialog() {
        ProgressDialog.showProgress(requireContext())
    }

    override fun hideProgressDialog() {
        ProgressDialog.hideProgress()
    }

    override fun showErrorToast(error: String?) {
        val message = error ?: getString(R.string.something_went_wrong)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun createListAdapter() = NotesListAdapter(
        { item -> presenter.onNoteClicked(item.id) },
        { item, isChecked -> presenter.onNoteCheckboxClicked(item.id, isChecked) }
    )

    private fun updateNoNotesPromptVisibility() {
        binding.noNotesPrompt.isVisible =
            !isThereCompletedNotes && uncompletedNotesListAdapter.itemCount == 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ProgressDialog.hideProgress()
        _binding = null
    }
}