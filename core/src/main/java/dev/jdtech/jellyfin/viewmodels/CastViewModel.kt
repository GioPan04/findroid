package dev.jdtech.jellyfin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jdtech.jellyfin.repository.JellyfinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CastViewModel
@Inject
constructor(
    private val repository: JellyfinRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Disconnected)
    val uiState = _uiState.asStateFlow()

    sealed class UiState {
        data class Connected(
            val sessionId: String
        ) : UiState()
        data object Disconnected : UiState()
    }

    fun connect(sessionId: String) {
        viewModelScope.launch {
            _uiState.emit(UiState.Connected(sessionId))
        }
    }
}