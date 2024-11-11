package com.example.lilac_project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lilac_project.data.model.LilacReply
import com.example.lilac_project.databinding.ActivityPostingViewerBinding
import com.example.lilac_project.databinding.DialogReplyBinding
import com.example.lilac_project.databinding.ItemReplyBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PostingViewerActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context, id: Long) {
            context.startActivity(Intent(context, PostingViewerActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra("id", id)
            })
        }
    }

    private val binding by lazy { ActivityPostingViewerBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<PostingViewerViewModel>()
    private val adapter by lazy { ReplyAdapter() }

    private var _id: Long? = null
    private val id get() = _id!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent != null) {
            _id = intent.getLongExtra("id", 0)
        }

        if (savedInstanceState != null) {
            _id = savedInstanceState.getLong("id", 0)
        }

        if (id == 0L) {
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initUi()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong("id", id)
        super.onSaveInstanceState(outState)
    }

    private fun initUi() = with(binding) {
        toolbar.setNavigationOnClickListener { finish() }

        recyclerView.adapter = adapter

        writeReplyButton.setOnClickListener {
            showReplyDialog()
        }

        lifecycleScope.launch {
            var isFirst = true

            viewModel.getStudy(id).collectLatest {
                if (it == null) {
                    finish()
                    return@collectLatest
                }

                if (isFirst) {
                    isFirst = false
                    viewModel.update(it.copy(views = it.views + 1))
                }

                toolbar.setOnMenuItemClickListener { menu ->
                    if (menu.itemId == R.id.action_participate) {
                        if (it.currentPeopleCount < it.maxPeopleCount) {
                            lifecycleScope.launch {
                                viewModel.update(it.copy(currentPeopleCount = it.currentPeopleCount + 1))
                                Toast.makeText(
                                    this@PostingViewerActivity,
                                    "지원 완료되었습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@PostingViewerActivity,
                                "모집인원이 다 찼습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    return@setOnMenuItemClickListener true
                }

                nameTextView.text = it.name
                titleTextView.text = it.title
                statusTextView.text =
                    if (it.currentPeopleCount < it.maxPeopleCount) "모집중" else "모집완료"
                viewsTextView.text = "${it.views}"
                peopleCountTextView.text = "${it.currentPeopleCount} / ${it.maxPeopleCount}"
                contentTextView.text = it.content
            }
        }

        lifecycleScope.launch {
            viewModel.getReplies(id).collectLatest {
                repliesTextView.text = "${it.size}"

                adapter.submitList(it)
            }
        }
    }

    private fun showReplyDialog() {
        val dialogBinding = DialogReplyBinding.inflate(LayoutInflater.from(this))

        MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setPositiveButton("확인") { _, _ ->
                val name = dialogBinding.nameEditText.text?.toString()?.trim() ?: ""
                val content = dialogBinding.contentEditText.text?.toString()?.trim() ?: ""
                if (name.isEmpty() && content.isEmpty()) return@setPositiveButton

                lifecycleScope.launch {
                    viewModel.reply(id, name, content)
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private class ReplyAdapter :
        ListAdapter<LilacReply, ReplyAdapter.ReplyItemViewHolder>(diffUtil) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyItemViewHolder {
            val binding =
                ItemReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ReplyItemViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ReplyItemViewHolder, position: Int) {
            val reply = getItem(position)

            with(holder.binding) {
                nameTextView.text = reply.name
                contentTextView.text = reply.content
            }
        }

        class ReplyItemViewHolder(val binding: ItemReplyBinding) :
            RecyclerView.ViewHolder(binding.root)

        companion object {
            val diffUtil = object : DiffUtil.ItemCallback<LilacReply>() {
                override fun areItemsTheSame(oldItem: LilacReply, newItem: LilacReply): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: LilacReply, newItem: LilacReply): Boolean {
                    return true
                }
            }
        }
    }
}