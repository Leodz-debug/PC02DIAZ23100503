package com.example.pc02diaz23100503.presentation.history

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pc02diaz23100503.data.model.ConversionRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HistoryViewModel : ViewModel() {
    private val _historyList = mutableStateOf<List<ConversionRecord>>(emptyList())
    val historyList: State<List<ConversionRecord>> = _historyList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    init {
        fetchHistory()
    }

    fun fetchHistory() {
        val user = auth.currentUser ?: return
        
        _isLoading.value = true
        _errorMessage.value = null

        db.collection("conversions")
            .whereEqualTo("uid", user.uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                _isLoading.value = false
                if (e != null) {
                    _errorMessage.value = "Error al cargar el historial: ${e.message}"
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.toObjects(ConversionRecord::class.java)
                    _historyList.value = list
                }
            }
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }
}
