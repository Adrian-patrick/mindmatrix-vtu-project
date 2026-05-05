package com.mindmatrix.raithavarta.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mindmatrix.raithavarta.R
import com.mindmatrix.raithavarta.data.TipEntity

class TipAdapter : ListAdapter<TipEntity, TipAdapter.TipViewHolder>(TipDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tip_card, parent, false)
        return TipViewHolder(view)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivTipImage: ImageView = itemView.findViewById(R.id.ivTipImage)
        private val tvSuccessLabel: TextView = itemView.findViewById(R.id.tvSuccessLabel)
        private val tvFarmerName: TextView = itemView.findViewById(R.id.tvFarmerName)
        private val tvInstructionEnglish: TextView = itemView.findViewById(R.id.tvInstructionEnglish)
        private val tvInstructionKannada: TextView = itemView.findViewById(R.id.tvInstructionKannada)

        fun bind(tip: TipEntity) {
            tvInstructionEnglish.text = tip.instruction
            tvInstructionKannada.text = tip.kannadaInstruction

            if (tip.isSuccessStory) {
                tvSuccessLabel.visibility = View.VISIBLE
                tvFarmerName.visibility = View.VISIBLE
                tvFarmerName.text = "Farmer: ${tip.farmerName}"
            } else {
                tvSuccessLabel.visibility = View.GONE
                tvFarmerName.visibility = View.GONE
            }

            Glide.with(itemView.context)
                .load(tip.imageUrl)
                .centerCrop()
                .placeholder(android.R.color.darker_gray)
                .into(ivTipImage)
        }
    }

    class TipDiffCallback : DiffUtil.ItemCallback<TipEntity>() {
        override fun areItemsTheSame(oldItem: TipEntity, newItem: TipEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TipEntity, newItem: TipEntity): Boolean {
            return oldItem == newItem
        }
    }
}
