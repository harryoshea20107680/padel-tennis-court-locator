package org.setu.padelcourtlocator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.setu.padelcourtlocator.models.PadelCourt
import timber.log.Timber
import android.net.Uri
import android.widget.ImageView

class PadelCourtAdapter(
    private var courts: List<PadelCourt>,
    private val listener: OnCourtListener
) : RecyclerView.Adapter<PadelCourtAdapter.CourtViewHolder>() {

    class CourtViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val nameText: TextView =
            itemView.findViewById(R.id.courtNameText)

        val addressText: TextView =
            itemView.findViewById(R.id.courtAddressText)

        val detailsText: TextView =
            itemView.findViewById(R.id.courtDetailsText)

        val websiteText: TextView =
            itemView.findViewById(R.id.courtWebsiteText)

        val courtImage: ImageView =
            itemView.findViewById(R.id.courtImage)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourtViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_padel_court,
                parent,
                false
            )

        return CourtViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CourtViewHolder,
        position: Int
    ) {
        val court = courts[position]

        val imageUri = court.imageUri?.let {
            Uri.parse(it)
        }

        if (imageUri != null) {
            holder.courtImage.setImageURI(imageUri)
        } else {
            holder.courtImage.setImageResource(
                android.R.drawable.ic_menu_gallery
            )
        }

        holder.nameText.text = court.name
        holder.addressText.text = court.address

        val type = if (court.isIndoor) "Indoor" else "Outdoor"
        val count = court.courtCount
        val multipleCourts = if (count == 1) "court" else "courts"

        holder.detailsText.text = "$count $type $multipleCourts"

        val website = court.website

        if (website.isNullOrBlank()) {
            holder.websiteText.visibility = View.GONE
        } else {
            holder.websiteText.text = website
            holder.websiteText.visibility = View.VISIBLE
        }

        // Single click: edit court
        holder.itemView.setOnClickListener {
            val index = holder.bindingAdapterPosition

            if (index != RecyclerView.NO_POSITION) {
                val selected = courts[index]

                Timber.d("Court clicked: id=%d", selected.id)
                listener.onCourtClick(selected)
            }
        }

        // Long press: ask to delete court
        holder.itemView.setOnLongClickListener {
            val index = holder.bindingAdapterPosition

            if (index != RecyclerView.NO_POSITION) {
                val selected = courts[index]

                Timber.d(
                    "Court long pressed: id=%d",
                    selected.id
                )

                listener.onCourtLongClick(selected)
            } else {
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return courts.size
    }

    fun updateCourts(newCourts: List<PadelCourt>) {
        courts = newCourts.toList()
        notifyDataSetChanged()
    }
}
