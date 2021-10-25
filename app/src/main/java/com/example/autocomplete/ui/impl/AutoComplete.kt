package com.example.autocomplete.ui.impl

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> AutoComplete(
    formControl: AutoCompleteFormControl<T>
) {
    val data by remember {
        formControl.dataState
    }
    var filteredData by remember {
        formControl.filteredValues
    }
    var inputDisplayValue by remember {
        formControl.inputDisplayValue
    }
    var isExpandedDropDown by remember {
        formControl.isExpanded
    }
    val isError by remember {
        mutableStateOf(formControl.errorState)
    }
    val alpha =
        animateFloatAsState(
            targetValue = if (isExpandedDropDown) 1f else 0f,
            animationSpec = tween(durationMillis = 300)
        )
    val alphaX = animateFloatAsState(
        targetValue = if (isExpandedDropDown) 0f else -90f,
        animationSpec = tween(durationMillis = 700)
    )
    var height by remember {
        mutableStateOf(0.dp)
    }
    height = if (isExpandedDropDown) 200.dp else 0.dp
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            if (formControl.behavior == AutoCompleteBehavior.DEFAULT || formControl.behavior == AutoCompleteBehavior.SOFT_SELECT) {
                OutlinedTextField(
                    value = inputDisplayValue,
                    onValueChange = {
                        inputDisplayValue = it
                        isExpandedDropDown = true
                        filteredData = data.filter { item ->
                            formControl.display(item)
                                .lowercase().contains(it.lowercase())
                        }
                    },
                    isError = if (formControl.behavior == AutoCompleteBehavior.SOFT_SELECT) isError.value else false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .onFocusChanged {
                            isExpandedDropDown = it.isFocused
                        }
                )
            }
        }
        Box(contentAlignment = Alignment.TopStart, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .graphicsLayer {
                transformOrigin = TransformOrigin(-0.3f, 0f)
                rotationX = alphaX.value
            }
            .alpha(alpha.value)
            .height(height)) {
            Card(elevation = 2.dp, modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    if (data.isEmpty()) {
                        items(3) {
                            LinearProgressIndicator(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                                    .height(8.dp),
                                color = Color.LightGray
                            )
                        }
                    } else {
                        items(if (inputDisplayValue.isEmpty()) data else filteredData) {
                            Card(elevation = 0.dp, modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth()
                                .clickable {
                                    formControl.emitValue(it)
                                    isExpandedDropDown = false
                                }) {
                                Text(
                                    text = formControl.display(it),
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}


