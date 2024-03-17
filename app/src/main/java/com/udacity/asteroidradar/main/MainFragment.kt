package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.displaySnackbar
import com.udacity.asteroidradar.models.Asteroid

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: MainAsteroidAdapter

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupRecyclerViewAdapter()
        setupObservers()

        setHasOptionsMenu(true)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerViewAdapter() {
        adapter = MainAsteroidAdapter(MainAsteroidAdapter.AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupObservers() {
        viewModel.asteroids.observe(viewLifecycleOwner, { asteroids ->
            if (asteroids != null) {
                adapter.submitList(asteroids)
            }
        })

        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner, { asteroid ->
            if (asteroid != null) {
                navigateToDetailFragment(asteroid)
                viewModel.doneNavigating()
            }
        })

        viewModel.displaySnackbarEvent.observe(viewLifecycleOwner, { displaySnackbarEvent ->
            if (displaySnackbarEvent) {
                displaySnackbar(
                    "Online data is not loading. Displaying local database data",
                    requireView()
                )
                viewModel.doneDisplayingSnackbar()
            }
        })
    }

    private fun navigateToDetailFragment(asteroid: Asteroid) {
        findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.view_week_menu -> viewModel.onViewWeekMenuClicked()
            R.id.view_today_menu -> viewModel.onTodayMenuClicked()
            R.id.view_saved_menu -> viewModel.onSavedMenuClicked()
        }
        return true
    }
}
