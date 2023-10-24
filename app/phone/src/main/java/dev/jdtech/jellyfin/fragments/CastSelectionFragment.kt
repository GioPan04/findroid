package dev.jdtech.jellyfin.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.jdtech.jellyfin.adapters.SessionListAdapter
import dev.jdtech.jellyfin.databinding.FragmentCastSelectionBinding
import dev.jdtech.jellyfin.dialogs.ErrorDialogFragment
import dev.jdtech.jellyfin.models.FindroidSession
import dev.jdtech.jellyfin.utils.checkIfLoginRequired
import dev.jdtech.jellyfin.viewmodels.SessionsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CastSelectionFragment : DialogFragment() {

    private lateinit var binding: FragmentCastSelectionBinding
    private val viewModel: SessionsViewModel by viewModels()

    private lateinit var errorDialog: ErrorDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("test")
        binding = FragmentCastSelectionBinding.inflate(inflater)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect() { uiState ->
                    when (uiState) {
                        is SessionsViewModel.UiState.Normal -> bindUiStateNormal(uiState)
                        is SessionsViewModel.UiState.Loading -> bindUiStateLoading()
                        is SessionsViewModel.UiState.Error -> bindUiStateError(uiState)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if(viewModel.itemsloaded) return@repeatOnLifecycle

                viewModel.loadData()
            }
        }

        binding.sessionsView.adapter = SessionListAdapter(
            onClickListener = { session ->
                Timber.d("Connecting to ${session.deviceName}")
            }
        )

        return binding.root
    }

    private fun bindUiStateNormal(uiState: SessionsViewModel.UiState.Normal) {
        binding.loadingIndicator.isVisible = false
        binding.errorLayout.errorPanel.isVisible = false
        binding.sessionsView.isVisible = true

        uiState.apply {
            (binding.sessionsView.adapter as SessionListAdapter).submitList(uiState.sessions)
        }
    }

    private fun bindUiStateError(uiState: SessionsViewModel.UiState.Error) {
        errorDialog = ErrorDialogFragment.newInstance(uiState.error)
        binding.loadingIndicator.isVisible = false
        binding.sessionsView.isVisible = false
        binding.errorLayout.errorPanel.isVisible = true
        checkIfLoginRequired(uiState.error.message)
    }

    private fun bindUiStateLoading() {
        binding.loadingIndicator.isVisible = true
        binding.sessionsView.isVisible = false
        binding.errorLayout.errorPanel.isVisible = false
    }
}