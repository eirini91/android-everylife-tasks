package com.televantou.everylifetasks.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.televantou.everylifetasks.data.menuItems.MenuItem
import com.televantou.everylifetasks.data.tasks.Task
import com.televantou.everylifetasks.databinding.MainFragmentBinding
import com.televantou.everylifetasks.utils.ServiceManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(),
        MainViewModel.DataUpdatedListener, MenuArrayAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    var taskAdapter = TaskAdapter(emptyList())
    var menuItemAdapter = MenuArrayAdapter(emptyList(), this)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        viewModel.dataUpdatedListener = this

        val binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = (viewModel)
        binding.taskAdapter = taskAdapter
        binding.menuArrayAdapter = menuItemAdapter
        getData()

        return binding.root
    }


    fun getData() {
        if (ServiceManager(activity).isNetworkAvailable()) {
            viewModel.getTasks()
        } else {
            viewModel.loadLocalData(null)
        }
    }

    override fun onDataUpdated(tasks: List<Task>?) {
        requireActivity().runOnUiThread(java.lang.Runnable {
            taskAdapter.set(tasks!!)

        })
    }

    override fun onMenuUpdated(menuItems: List<MenuItem>) {
        menuItemAdapter.setItems(menuItems)
    }

    override fun onErrorDialog(error: String) {
        android.app.AlertDialog.Builder(context).setTitle("Something Went Wrong").setMessage(error)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }.show()
    }

    override fun onItemClicked(menuItem: MenuItem) {
        viewModel.filterItems()
    }

    fun refresh() {
        getData()
    }


}