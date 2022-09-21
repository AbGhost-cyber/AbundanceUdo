package com.example.abundanceudo.featureBmi.presentation.addBmi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.abundanceudo.R
import com.example.abundanceudo.databinding.CommonAdapterLayoutBinding

typealias SelectItemCallback = (String, View) -> Unit

class CommonAdapter(
    private val onItemClick: SelectItemCallback
) : RecyclerView.Adapter<CommonAdapter.CommonViewHolder>() {
    private var selectedPos = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val itemBinding = CommonAdapterLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CommonViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffUtilCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    fun setSelectedCategoryPos(newPos: Int, view: View) {
        if (selectedPos >= 0) {
            notifyItemChanged(selectedPos)
            if (newPos >= 0) this.selectedPos = newPos
            notifyItemChanged(newPos)
            onItemClick(differ.currentList[newPos], view)
        }
    }

    fun getSelectedItem(): String {
        return differ.currentList[selectedPos]
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    inner class CommonViewHolder(private val binding: CommonAdapterLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(value: String) {
            binding.tvValue.text = value
            if (selectedPos != -1) {
                val context: Context = binding.root.context
                binding.selLine.isVisible = selectedPos == adapterPosition
                if (selectedPos == adapterPosition) {
                    binding.tvValue.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gradientStartColor
                        )
                    )
                } else {
                    binding.tvValue.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.my_gray
                        )
                    )
                }
            }
            binding.root.setOnClickListener {
                setSelectedCategoryPos(adapterPosition, it)
            }
        }
    }
}
