package com.example.pc02diaz23100503.presentation.converter

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pc02diaz23100503.data.remote.Constants
import com.example.pc02diaz23100503.data.remote.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.launch

class ConverterViewModel : ViewModel() {
    private val _amount = mutableStateOf("")
    val amount: State<String> = _amount

    private val _fromCurrency = mutableStateOf("USD")
    val fromCurrency: State<String> = _fromCurrency

    private val _toCurrency = mutableStateOf("PEN")
    val toCurrency: State<String> = _toCurrency

    private val _resultText = mutableStateOf("")
    val resultText: State<String> = _resultText

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    val currencies = listOf("USD", "EUR", "PEN", "GBP", "JPY")

    fun onAmountChange(value: String) {
        _amount.value = value
    }

    fun onFromCurrencyChange(value: String) {
        _fromCurrency.value = value
    }

    fun onToCurrencyChange(value: String) {
        _toCurrency.value = value
    }

    fun convert() {
        val amountValue = _amount.value.toDoubleOrNull()
        
        if (amountValue == null || amountValue <= 0) {
            _errorMessage.value = "Ingresa un monto válido mayor a cero"
            return
        }

        if (_fromCurrency.value == _toCurrency.value) {
            _errorMessage.value = "Las monedas deben ser diferentes"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val response = RetrofitClient.instance.getExchangeRate(
                    apiKey = Constants.API_KEY,
                    from = _fromCurrency.value,
                    to = _toCurrency.value,
                    amount = amountValue
                )

                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    _resultText.value = "${String.format("%.2f", amountValue)} ${_fromCurrency.value} = ${String.format("%.2f", data.conversionResult)} ${_toCurrency.value}"
                    saveConversionToFirestore(data, amountValue)
                } else {
                    _errorMessage.value = "Error al obtener la tasa de cambio"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun saveConversionToFirestore(data: com.example.pc02diaz23100503.data.model.ExchangeRateResponse, amount: Double) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val db = FirebaseFirestore.getInstance()

        val conversion = hashMapOf(
            "uid" to user.uid,
            "email" to user.email,
            "amount" to amount,
            "from" to data.baseCode,
            "to" to data.targetCode,
            "rate" to data.conversionRate,
            "result" to data.conversionResult,
            "createdAt" to FieldValue.serverTimestamp()
        )

        db.collection("conversions")
            .add(conversion)
            .addOnFailureListener {
                // Silently fail or log
            }
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }
}
