package com.example.myquizapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myquizapp.R
import com.example.myquizapp.models.Question

class QuestionAdapter(
    private val questions: MutableList<Question>,
    private val listener: QuestionActionListener? = null // optional
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    interface QuestionActionListener {
        fun onEditQuestion(position: Int)
        fun onDeleteQuestion(position: Int)
    }

    private fun getOptionLetter(index: Int) = when (index) {
        0 -> "A"
        1 -> "B"
        2 -> "C"
        3 -> "D"
        else -> "?"
    }

    class QuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionNumber: TextView = view.findViewById(R.id.tv_q_number)
        val questionText: TextView = view.findViewById(R.id.tv_q_text)
        val correctOption: TextView = view.findViewById(R.id.tv_correct_option)
        val ivEdit: ImageView = view.findViewById(R.id.iv_edit)
        val ivDelete: ImageView = view.findViewById(R.id.iv_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.questionNumber.text = "Q${position + 1}."
        holder.questionText.text = question.questionText
        holder.correctOption.text = "Correct: ${getOptionLetter(question.correctAnswerIndex)}"

        // Click listeners for edit and delete (only if listener is provided)
        holder.ivEdit.setOnClickListener { listener?.onEditQuestion(position) }
        holder.ivDelete.setOnClickListener { listener?.onDeleteQuestion(position) }

        // Hide icons if listener is null (optional)
        if (listener == null) {
            holder.ivEdit.visibility = View.GONE
            holder.ivDelete.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = questions.size
}
