package com.example.todo_list.presentation.ui.note_editing

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.todo_list.R
import com.example.todo_list.app.TodoListApp
import com.example.todo_list.databinding.FragmentEditNoteBinding
import com.example.todo_list.presentation.ui.base.BaseFragment
import io.reactivex.rxjava3.subjects.BehaviorSubject
import moxy.ktx.moxyPresenter

class EditNoteScreen : BaseFragment(), EditNoteView {

    private val presenter by moxyPresenter {
        (requireActivity().application as TodoListApp).appComponent.editNotePresenter()
    }

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private val isNotificationOn = BehaviorSubject.createDefault(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()

        binding.fab.setOnClickListener { _ ->
            goBack()
            presenter.onSaveButtonClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
                isNotificationOn.subscribe {
                    if(it) {
                        menu.findItem(R.id.action_bell).setIcon(R.drawable.ic_bell)
                        menu.findItem(R.id.action_delete).isVisible = true
                    } else {
                        menu.findItem(R.id.action_bell).setIcon(R.drawable.ic_bell_off)
                        menu.findItem(R.id.action_delete).isVisible = false
                    }
                }

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_bell -> {
                        val v = isNotificationOn.value!!
                        isNotificationOn.onNext(!v)
                        true
                    }
                    R.id.action_delete -> {
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun setToolbarTitle(tittle: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = tittle
    }

    override fun setText(text: String) {
        TODO("Not yet implemented")
    }

    override fun setReminderState(isReminderActive: Boolean) {
        TODO("Not yet implemented")
    }

    override fun showDeleteIcon() {
        TODO("Not yet implemented")
    }

    override fun goBack() {
        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
    }

    override fun showProgressDialog() {
        TODO("Not yet implemented")
    }

    override fun hideProgressDialog() {
        TODO("Not yet implemented")
    }

    override fun showErrorToast(error: String) {
        TODO("Not yet implemented")
    }

}