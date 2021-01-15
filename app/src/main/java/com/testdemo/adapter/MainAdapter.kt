package com.testdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.testdemo.R
import com.testdemo.databinding.RowItemBinding
import com.testdemo.model.UserModel

class MainAdapter(private val listener: ((item: UserModel, binding: RowItemBinding) -> Unit)) : RecyclerView.Adapter<MainAdapter.Holder>() {

    private lateinit var context: Context
    private val data = arrayListOf<UserModel>()

    fun setData(list: ArrayList<UserModel>) {
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        return Holder(LayoutInflater.from(context).inflate(R.layout.row_item, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = data[position]

        holder.vb.labelId.text = "${item.id}"
        holder.vb.labelLogin.text = item.login
        Glide.with(context).load(item.avatarUrl).into(holder.vb.imageAvatar)

        holder.vb.container.transitionName = "container$position"

        holder.vb.root.setOnClickListener { item.let { listener.invoke(it, holder.vb) } }
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val vb = RowItemBinding.bind(view)
    }
}