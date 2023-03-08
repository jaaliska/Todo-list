package com.example.todo_list.presentation.ui.notes_list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo_list.R
import com.example.todo_list.app.TodoListApp
import com.example.todo_list.databinding.FragmentNotesListBinding
import com.example.todo_list.domain.model.Note
import com.example.todo_list.presentation.ui.base.BaseFragment
import com.example.todo_list.presentation.ui.notes_list.adapter.NotesListAdapter
import com.example.todo_list.presentation.ui.notes_list.view.FoldingListCustomView
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

    private var completedNotesListView: ListCustomView<NotesListView.Item>? = null
    private var uncompletedNotesListAdapter: NotesListAdapter? = null


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
        binding.fab.setOnClickListener { _ ->
            presenter.onButtonAddNoteClicked()
        }
    }

    override fun showUncompletedNotes(listItems: List<NotesListView.Item>) {
        setNoNotesTextVisibility(false)
        if (uncompletedNotesListAdapter == null) {
            uncompletedNotesListAdapter = createListAdapter(listItems)
            binding.uncompletedNotesContainer.layoutManager = LinearLayoutManager(context)
            binding.uncompletedNotesContainer.adapter = uncompletedNotesListAdapter
            Log.i("MyCheck", "UncompletedNotes was build")
        } else {
            uncompletedNotesListAdapter!!.submitList(listItems)
        }
    }

    override fun showCompletedNotes(listItems: List<NotesListView.Item>, isPanelOpen: Boolean) {
        setNoNotesTextVisibility(false)
        if (completedNotesListView == null) {
            binding.completedNotesContainer.removeAllViews()
            completedNotesListView = FoldingListCustomView(
                isPanelOpen,
                presenter::onFoldingPanelCLicked,
                createListAdapter(listItems)
            ).also {
                binding.completedNotesContainer.addView(it.build(binding.completedNotesContainer))
            }
            Log.i("MyCheck", "Custom view was build")
        } else {
            completedNotesListView!!.submitList(listItems)
        }
    }

    override fun hideCompletedNotes() {
        completedNotesListView = null
        binding.completedNotesContainer.removeAllViews()
    }

    private fun createListAdapter(listItems: List<NotesListView.Item>) = NotesListAdapter(
        { item ->
            presenter.onNoteClicked(item.id)
        },
        { item, isCheck ->
            presenter.onNoteCheckboxClicked(item.id, isCheck)
        }
    ).also {
        it.submitList(listItems)
    }

    override fun goToEditNoteScreen(note: Note?) {
        val bundle = bundleOf("note" to note)
        findNavController().navigate(R.id.action_NotesListScreen_to_EditNoteScreen, bundle)
    }

    override fun showEmptyScreen() {
        with(binding) {
            uncompletedNotesContainer.removeAllViews()
            completedNotesContainer.removeAllViews()
            setNoNotesTextVisibility(true)
        }
    }

    private fun setNoNotesTextVisibility(isVisible: Boolean) {
        if (binding.textviewNoNotes.isVisible != isVisible) {
            binding.textviewNoNotes.isVisible = isVisible
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}