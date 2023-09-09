package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource.dessertList
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertClickerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DessertClickerUiState())
    val uiState: StateFlow<DessertClickerUiState> = _uiState.asStateFlow()

    private var desserts: MutableSet<Dessert> = mutableSetOf()
    private var currentDessertIndex :  Int = 0;

    init {
        desserts = dessertList.toMutableSet()
        currentDessertIndex = 0
        _uiState.value = DessertClickerUiState(currentDessertImageId = desserts.elementAt(currentDessertIndex).imageId)
    }

    fun userClickedDessert() {
        val dessertToShow = determineDessertToShow()
        _uiState.update { currentState ->
            currentState.copy(
                revenue = dessertToShow.price.plus(currentState.revenue),
                dessertsSold = currentState.dessertsSold.inc(),
                currentDessertImageId = dessertToShow.imageId,
            )
        }
        currentDessertIndex++
    }
    
    fun determineDessertToShow(): Dessert{
        var dessertToShow = desserts.elementAt(currentDessertIndex)
        for(dessert in desserts){
            if(_uiState.value.dessertsSold >= dessert.startProductionAmount){
                dessertToShow = dessert
            }else{
                break
            }
        }
        return dessertToShow
    }
}