package com.testdemo.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import com.testdemo.R
import com.testdemo.States
import com.testdemo.adapter.MainAdapter
import com.testdemo.databinding.FragmentMainBinding
import com.testdemo.viewmodel.MainViewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var vb: FragmentMainBinding
    private val vm: MainViewModel by activityViewModels()
    private var adapter: MainAdapter? = null

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        adapter = MainAdapter { item, vb ->
            exitTransition = MaterialElevationScale(false).apply {
                duration = 300
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = 300
            }
            val extra = FragmentNavigatorExtras(
                vb.container to "container"
            )

            val directions = MainFragmentDirections.actionMainFragmentToDetailsFragment(item)
            findNavController().navigate(directions, extra)
        }

        vb = FragmentMainBinding.bind(view).apply {
            recyclerView.adapter = adapter
        }

        vm.data.observe(viewLifecycleOwner, {
            vb.progress.visibility = GONE
            when (it) {
                is States.Success -> {
                    adapter?.setData(ArrayList(it.users))
                }
                is States.Loading -> {
                    vb.progress.visibility = VISIBLE
                }
                is States.Error -> {
                    Snackbar.make(view, it.message, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        if (adapter?.itemCount == 0) {
            vm.getUsers(0)
        }

        vb.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lm = vb.recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = lm.childCount
                val totalItemCount = lm.itemCount
                val pastVisibleItems = lm.findFirstVisibleItemPosition()

                if ((visibleItemCount + pastVisibleItems + 2) >= totalItemCount) {
                    vm.getUsers(adapter?.itemCount ?: 0)
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()

        vm.recyclerState = vb.recyclerView.layoutManager?.onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()

        vb.recyclerView.layoutManager?.onRestoreInstanceState(vm.recyclerState)
    }
}