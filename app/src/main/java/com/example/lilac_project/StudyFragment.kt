package com.example.lilac_project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lilac_project.data.model.LilacCategory
import com.example.lilac_project.data.model.LilacStudy
import com.example.lilac_project.databinding.FragmentStudyBinding
import com.example.lilac_project.databinding.ItemStudyBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class StudyFragment : Fragment() {
    private var _binding: FragmentStudyBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<StudyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            queryEditText.doAfterTextChanged {
                viewModel.query.value = it?.toString()?.trim() ?: ""
            }

            postingButton.setOnClickListener {
                startActivity(Intent(requireContext(), PostingEditorActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                })
            }

            viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

            TabLayoutMediator(
                tabLayout,
                viewPager
            ) { tab, position -> tab.text = LilacCategory.entries[position].title }
                .attach();
        }
    }

    private class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount() = LilacCategory.entries.size

        override fun createFragment(position: Int): Fragment {
            return StudyPageFragment.getInstance(LilacCategory.entries[position])
        }
    }
}

class StudyPageFragment : Fragment() {
    companion object {
        fun getInstance(category: LilacCategory): StudyPageFragment {
            return StudyPageFragment().apply {
                arguments = bundleOf("category" to category.name)
            }
        }
    }

    private var _category: LilacCategory? = null
    private val category get() = _category!!

    private val viewModel by viewModels<StudyViewModel>({ requireParentFragment() })
    private val adapter by lazy { StudyAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            val category = requireArguments().getString("category")
            _category = LilacCategory.entries.first { it.name == category }
        }

        if (savedInstanceState != null) {
            val category = savedInstanceState.getString("category")
            _category = LilacCategory.entries.first { it.name == category }
        }

        assert(_category != null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("category", category.name)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = FrameLayout(inflater.context)
        val recyclerView = RecyclerView(inflater.context).apply {
            tag = "recycler_view"
            layoutManager = LinearLayoutManager(inflater.context)
        }

        layout.addView(recyclerView, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        layout.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewWithTag<RecyclerView>("recycler_view")
        recyclerView.addItemDecoration(
            MaterialDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        recyclerView.adapter = adapter.apply {
            onClickListener = {
                PostingViewerActivity.startActivity(requireContext(), it.id)
            }
        }

        lifecycleScope.launch {
            viewModel.query.flatMapLatest {
                if (category == LilacCategory.all) {
                    viewModel.db.studyDao().studies(it)
                } else {
                    viewModel.db.studyDao().studies(category.name, it)
                }
            }.collectLatest {
                adapter.submitList(it)
            }
        }
    }

    private class StudyAdapter :
        ListAdapter<LilacStudy, StudyAdapter.StudyItemViewHolder>(diffUtil) {
        var onClickListener: ((LilacStudy) -> Unit)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyItemViewHolder {
            val binding =
                ItemStudyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return StudyItemViewHolder(binding)
        }

        override fun onBindViewHolder(holder: StudyItemViewHolder, position: Int) {
            val study = getItem(position)

            with(holder.binding) {
                root.setOnClickListener {
                    onClickListener?.invoke(study)
                }

                statusTextView.text =
                    if (study.currentPeopleCount < study.maxPeopleCount) "모집중" else "모집완료"
                peopleCountTextView.text = "${study.currentPeopleCount} / ${study.maxPeopleCount}"
                titleTextView.text = study.title
            }
        }

        class StudyItemViewHolder(val binding: ItemStudyBinding) :
            RecyclerView.ViewHolder(binding.root)

        companion object {
            val diffUtil = object : DiffUtil.ItemCallback<LilacStudy>() {
                override fun areItemsTheSame(oldItem: LilacStudy, newItem: LilacStudy): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: LilacStudy, newItem: LilacStudy): Boolean {
                    return oldItem.currentPeopleCount == newItem.currentPeopleCount
                }
            }
        }
    }
}