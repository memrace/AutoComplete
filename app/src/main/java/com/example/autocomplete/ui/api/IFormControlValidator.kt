package com.example.autocomplete.ui.api

interface IFormControlValidator<T> {
    fun validate(value: T?): Boolean
}