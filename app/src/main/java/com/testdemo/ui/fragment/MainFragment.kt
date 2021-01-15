package com.testdemo.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import com.testdemo.MainFragmentView
import com.testdemo.R
import com.testdemo.adapter.MainAdapter
import com.testdemo.databinding.FragmentMainBinding
import com.testdemo.model.UserModel
import com.testdemo.presenter.MainPresenter
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

class MainFragment : MvpAppCompatFragment(), MainFragmentView {

    private lateinit var vb: FragmentMainBinding
    @InjectPresenter
    lateinit var vm: MainPresenter
    private var adapter: MainAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

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

    override fun showError(message: String) {
        vb.progress.visibility = GONE
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDataReceived(data: ArrayList<UserModel>) {
        vb.progress.visibility = GONE
        adapter?.setData(ArrayList(data))
    }

    override fun showLoading() {
        vb.progress.visibility = VISIBLE
    }
}