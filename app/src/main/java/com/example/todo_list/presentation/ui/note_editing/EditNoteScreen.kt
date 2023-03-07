package com.example.todo_list.presentation.ui.note_editing

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.todo_list.R
import com.example.todo_list.app.TodoListApp
import com.example.todo_list.app.di.factory.EditNotePresenterFactory
import com.example.todo_list.databinding.FragmentEditNoteBinding
import com.example.todo_list.presentation.ui.base.BaseFragment
import com.example.todo_list.presentation.utils.ui_kit.ProgressDialog
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class EditNoteScreen : BaseFragment(), EditNoteView {

    @Inject
    lateinit var presenterFactory: EditNotePresenterFactory
    private val presenter by moxyPresenter<EditNotePresenter> {
        presenterFactory.create(
            arguments?.getParcelable("note")
        )
    }

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private val progress = ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as TodoListApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener { _ ->
            presenter.onSaveButtonClicked()
        }

        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.onTextChanged(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun setToolbar(
        isNewNote: Boolean,
        isReminderActive: Boolean,
        isDeletingAvailable: Boolean
    ) {
        with(binding.toolbar) {
            menu.clear()
            inflateMenu(R.menu.menu_main)
            setNavigationIcon(R.drawable.ic_left_arrow)
            setNavigationOnClickListener { _ -> goBack() }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_bell -> {
                        presenter.onReminderClicked()
                        true
                    }
                    R.id.action_delete -> {
                        if (isDeletingAvailable) {
                            presenter.onDeleteButtonClicked()
                        }
                        true
                    }
                    else -> false
                }
            }
            if (!isDeletingAvailable) {
                menu.findItem(R.id.action_delete).isVisible = false
            }
            setReminderState(isReminderActive)
            setTitle(if (isNewNote) R.string.new_note_tittle else R.string.edit_note_tittle)
        }
    }

    override fun setText(text: String) {
        val currentText = binding.editText.text.toString()
        if (text != currentText) {
            binding.editText.setText(text)
        }
    }

    override fun setReminderState(isReminderActive: Boolean) {
        binding.toolbar.menu.findItem(R.id.action_bell).setIcon(
            if (isReminderActive) R.drawable.ic_bell else R.drawable.ic_bell_off
        )

    }

    override fun goBack() {
        findNavController().popBackStack()
    }

    override fun showProgressDialog() {
        progress.showProgress(requireContext())
    }

    override fun hideProgressDialog() {
        progress.hideProgress()
    }

    override fun showErrorToast(error: String?) {
        val message = error ?: getString(R.string.something_went_wrong)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}