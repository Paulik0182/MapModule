package com.paulik.mapmodule.ui.markers

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.paulik.mapmodule.R
import com.paulik.mapmodule.databinding.FragmentListMarkersBinding
import com.paulik.mapmodule.domain.presenters.ListMarkersPresenter
import com.paulik.mapmodule.ui.BaseFragment
import com.paulik.mapmodule.ui.map.MapFragment
import com.paulik.mapmodule.ui.map.TO_MARKER
import com.paulik.mapmodule.ui.viewById
import moxy.ktx.moxyPresenter

class ListMarkersFragment : BaseFragment<FragmentListMarkersBinding>(
    FragmentListMarkersBinding::inflate
) {

    private val presenter: ListMarkersPresenter by moxyPresenter { ListMarkersPresenter() }
    private val listMarkersRecyclerview by viewById<RecyclerView>(R.id.list_markers_recyclerview)

    private val adapter: ListMarkersAdapter by lazy {
        ListMarkersAdapter(
            ::onItemClick,
            ::onCorrectionClick,
            ::onRemove
        )
    }

    private fun onItemClick(marker: MarkerOptions) {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .replace(
                    R.id.fragment_container_frame_layout,
                    MapFragment.newInstance(Bundle().apply {
                        putString(TO_MARKER, Gson().toJson(marker))
                    })
                )
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    private fun onCorrectionClick(i: Int, marker: MarkerOptions) {
        presenter.onCorrectionClick(i, marker)
    }

    private fun onRemove(i: Int, marker: MarkerOptions) {
        setDataToAdapter(presenter.onRemove(i))
    }

    override fun init() {
        listMarkersRecyclerview.adapter = adapter
        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(
            listMarkersRecyclerview
        )
        presenter.loadMarkers()
    }

    override fun loadMarkers(markers: MutableList<MarkerOptions>) {
        setDataToAdapter(markers)
    }

    private fun setDataToAdapter(data: MutableList<MarkerOptions>) {
        adapter.setData(data)
    }

    override fun onPause() {
        presenter.saveListMarkers()
        super.onPause()
    }

    interface Controller {
        //todo
    }

    private fun getController(): Controller = activity as Controller

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getController()
    }

    companion object {
        fun newInstance() = ListMarkersFragment()
    }
}
