package dev.jdtech.jellyfin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jdtech.jellyfin.models.FindroidSession
import dev.jdtech.jellyfin.repository.JellyfinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SessionsViewModel
@Inject
constructor(
    private val repository: JellyfinRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    var itemsloaded = false

    sealed class UiState {
        data class Normal(val sessions: List<FindroidSession>) : UiState()
        data object Loading : UiState()
        data class Error(val error: Exception) : UiState()
    }

    fun loadData() {
        itemsloaded = true
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)

            try {
                val sessions = repository.getSessions()
                _uiState.emit(UiState.Normal(sessions))
            } catch (e: Exception) {
                _uiState.emit(UiState.Error(e))
            }
        }
    }
}