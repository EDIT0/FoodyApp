package com.example.foodyapp.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodyapp.viewmodels.MainViewModel
import com.example.foodyapp.R
import com.example.foodyapp.adapters.RecipesAdapter
import com.example.foodyapp.databinding.FragmentRecipesBinding
import com.example.foodyapp.util.Constants
import com.example.foodyapp.util.NetworkResult
import com.example.foodyapp.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy {
        RecipesAdapter()
    }
    private lateinit var recipesFragmentBinding: FragmentRecipesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        recipesFragmentBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_recipes, container, false)

        return recipesFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        requestApiData()

    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

                else -> {}
            }
        }
    }

    private fun setupRecyclerView() {
        recipesFragmentBinding.recyclerview.adapter = mAdapter
        recipesFragmentBinding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        recipesFragmentBinding.sflSample.visibility = View.VISIBLE
    }

    private fun hideShimmerEffect() {
        recipesFragmentBinding.sflSample.visibility = View.GONE
    }

}