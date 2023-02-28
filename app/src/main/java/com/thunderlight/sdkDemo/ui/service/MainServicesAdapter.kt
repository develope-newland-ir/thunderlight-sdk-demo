package com.thunderlight.sdkDemo.ui.service

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thunderlight.sdkDemo.constant.ConstantsStr
import com.thunderlight.sdkDemo.ui.service.model.MainServicesItem
import com.thunderlight.sdkdemo.databinding.ItemPaymentServicesBinding
import com.thunderlight.thundersmartsdk.constant.RequestType

/**
 * @author Created by M.Moradikia
 * @date  10/31/2022
 * @company Thunder-Light
 */
class MainServicesAdapter : RecyclerView.Adapter<MainServicesAdapter.ViewHolder>() {

    private var list = emptyList<MainServicesItem>()
    private lateinit var binding: ItemPaymentServicesBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemPaymentServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list[position])
        holder.setIsRecyclable(false)
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: MainServicesItem) {
            binding.let {
                binding.apply {
                    tvName.text = item.title
                    imgIcon.setImageResource(item.icon)
                    root.setOnClickListener {
                        onItemClickListener.let {
                            it?.let { it1 -> it1(ConstantsStr.ACTION_OPEN, item.action) }
                        }
                    }
                }
            }
        }
    }

    fun setData(serviceList: ArrayList<MainServicesItem>) {
        list = serviceList
        val mosiDiffUtils = MosiDiffUtils(list, serviceList)
        val diffUtils = DiffUtil.calculateDiff(mosiDiffUtils)
        list = serviceList
        diffUtils.dispatchUpdatesTo(this)
    }

    private var onItemClickListener: ((String, RequestType) -> Unit)? = null

    fun setOnItemClickListener(listener: (String, RequestType) -> Unit) {
        onItemClickListener = listener
    }

    class MosiDiffUtils(
        private val oldItem: List<MainServicesItem>,
        private val newItem: List<MainServicesItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }
    }
}