package com.example.lilac_project

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.lilac_project.data.model.LilacCategory
import com.example.lilac_project.databinding.ActivityPostingEditorBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PostingEditorActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPostingEditorBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<PostingEditorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initUi()
    }

    private fun initUi() = with(binding) {
        toolbar.setNavigationOnClickListener { finish() }

        val categories = LilacCategory.entries.filter { it != LilacCategory.all }
        val items = categories.map { it.title }
        val adapter = ArrayAdapter(this@PostingEditorActivity, R.layout.item_dropdown, items)
        (categoryField.editText as? AutoCompleteTextView)?.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, i, _ ->
                viewModel.category.value = categories[i].name
            }
        }

        maxPeopleCountSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val maxPeopleCount = p1 + 3
                viewModel.maxPeopleCount.value = maxPeopleCount
                peopleCountTextView.text = "${maxPeopleCount}명"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        nameEditText.doAfterTextChanged {
            viewModel.name.value = it?.toString() ?: ""
        }

        anonymityCheckBox.setOnCheckedChangeListener { _, b ->
            viewModel.anonymity.value = b
        }

        titleEditText.doAfterTextChanged {
            viewModel.title.value = it?.toString() ?: ""
        }

        contentEditText.doAfterTextChanged {
            viewModel.content.value = it?.toString() ?: ""
        }

        submitButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.submit()
                finish()
            }
        }

        lifecycleScope.launch {
            viewModel.isValidated.collectLatest {
                submitButton.isEnabled = it
            }
        }
    }
}