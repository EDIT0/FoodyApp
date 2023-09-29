package com.example.foodyapp.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodyapp.viewmodels.MainViewModel
import com.example.foodyapp.R
import com.example.foodyapp.adapters.RecipesAdapter
import com.example.foodyapp.databinding.FragmentRecipesBinding
import com.example.foodyapp.util.NetworkListener
import com.example.foodyapp.util.NetworkResult
import com.example.foodyapp.util.observeOnce
import com.example.foodyapp.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private val args by navArgs<RecipesFragmentArgs>()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy {
        RecipesAdapter()
    }

    private lateinit var networkListener: NetworkListener

    private var _recipesFragmentBinding: FragmentRecipesBinding? = null
    private val recipesFragmentBinding: FragmentRecipesBinding get() = _recipesFragmentBinding!!

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
        _recipesFragmentBinding = FragmentRecipesBinding.inflate(inflater, container, false)
        recipesFragmentBinding.lifecycleOwner = this
        recipesFragmentBinding.mainViewModel = mainViewModel
//        recipesFragmentBinding = DataBindingUtil.inflate(inflater,
//            R.layout.fragment_recipes, container, false)

        return recipesFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }

        // 문제 있는 듯 TODO
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("MYTAG", "NetworkListener ${status.toString()}")
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        recipesFragmentBinding.recipesFab.setOnClickListener {
            if(recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }
    }

    private fun setupRecyclerView() {
        recipesFragmentBinding.recyclerview.adapter = mAdapter
        recipesFragmentBinding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if(database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d("MYTAG","readApiData called!")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        Log.d("MYTAG","requestApiData called!")
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
                    loadDataFromCache()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

                else -> {}
            }
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if(database.isNotEmpty()) {
                    Log.d("MYTAG","loadDataFromCache")
                    mAdapter.setData(database[0].foodRecipe)
                }
            }
        }
    }


    private fun showShimmerEffect() {
        recipesFragmentBinding.sflSample.visibility = View.VISIBLE
    }

    private fun hideShimmerEffect() {
        recipesFragmentBinding.sflSample.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _recipesFragmentBinding = null // 메모리 누수 피하기 위함
    }

}