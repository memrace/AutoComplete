package com.example.autocomplete.ui.impl

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.autocomplete.ui.api.FormControl
import com.example.autocomplete.ui.api.IFormControlValidator

class AutoCompleteFormControl<T>(
    internal val dataState: State<List<T>>,
    internal val display: (T) -> String,
    internal val behavior: AutoCompleteBehavior,
    validators: List<IFormControlValidator<T>> = listOf()
) : FormControl<T>(null, validators) {
    internal val isExpanded: MutableState<Boolean> = mutableStateOf(false)
    internal val inputDisplayValue: MutableState<String> = mutableStateOf("")

    override fun emitValue(value: T?) {
        inputDisplayValue.value = if (value == null) "" else display(value)
        super.emitValue(value)
    }
}


enum class AutoCompleteBehavior {
    DEFAULT,
    SOFT_SELECT,
    HARD_SELECT
}