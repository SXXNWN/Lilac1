package com.example.lilac_project

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lilac_project.databinding.ListAssignmentsBinding

class AssignmentAdapter(val assignment: Array<Assignment>) : RecyclerView.Adapter<AssignmentAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        Log.d("test","adapterCreate")
        val binding = ListAssignmentsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(assignment[position])
    }

    override fun getItemCount() = assignment.size


    class Holder(private val binding: ListAssignmentsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(assignment: Assignment){
            binding.txtInfo.text = assignment.info
            binding.txtProfessor.text = assignment.professor
            binding.txtDate.text = assignment.date
            binding.Star.setImageResource(when(assignment.star) {
                Star.Star -> R.drawable.star
                Star.NoStar -> R.drawable.nostar
            })
        }
    }
}