package com.example.autocomplete.ui.api

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class FormControl<T>(
    initValue: T? = null,
    private val validators: List<IFormControlValidator<T>> = listOf()
) {
    val isValid: Boolean
        get() {
            return if (validators.isNotEmpty()) validators.all { control ->
                control.validate(_valueChange.value)
            } else true
        }
    private val _valueChange = MutableStateFlow(initValue)
    val valueChange = _valueChange.asStateFlow()
    open fun emitValue(value: T?) {
        _valueChange.tryEmit(value)
    }
}
