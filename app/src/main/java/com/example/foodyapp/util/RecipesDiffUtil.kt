package com.example.foodyapp.util

import androidx.recyclerview.widget.DiffUtil
import com.example.foodyapp.models.Result

class RecipesDiffUtil(
    private val oldList: List<Result>,
    private val newList: List<Result>
) : DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition] // value
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition] // reference value
    }

}