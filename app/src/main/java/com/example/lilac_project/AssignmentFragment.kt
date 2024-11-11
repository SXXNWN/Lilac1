package com.example.lilac_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lilac_project.databinding.ActivityMainBinding
import com.example.lilac_project.databinding.FragmentAssignmentBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
const val ARG_PARAM1 = "param1"
const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AssignmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AssignmentFragment : Fragment() {

    val assignments = arrayOf(
        Assignment("4장에서 풀어 볼 연습문제 및 기출문제", Star.Star,"장문정","2024-10-21"),
        Assignment("퀴즈 2 결과", Star.NoStar,"장문정","2024-10-19"),
        Assignment("프로젝트 제안발표 가이드라인",Star.NoStar,"김철기","2024-10-18"),
        Assignment("임베디드 기말 프로젝트 기한",Star.Star,"최차봉","2024-10-18"),
        Assignment("심리학의 이해 ppt 읽어보세요!",Star.NoStar,"고숙희","2024-10-16"),
        Assignment("화요일반 프로젝트 조편성입니다",Star.NoStar,"김철기","2024-10-14"),
        Assignment("과제 관련 답변입니다",Star.NoStar,"정재훈","2024-10-13"),
        Assignment("PDF 파일 제공",Star.NoStar,"이인복","2024-10-13"),
        Assignment("과제 2 결과",Star.Star,"장문정","2024-10-12"),
        Assignment("중간고사는 10/17(목) 오후 7:00 입니다.",Star.NoStar,"김철기","2024-10-11"),
        Assignment("11월 6일 정상 수업합니다",Star.Star,"장문정","2024-10-11")


    )
    lateinit var binding : FragmentAssignmentBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAssignmentBinding.inflate(layoutInflater)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding.recAssignments.adapter = AssignmentAdapter(assignments)
        binding.recAssignments.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AssignmentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AssignmentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}