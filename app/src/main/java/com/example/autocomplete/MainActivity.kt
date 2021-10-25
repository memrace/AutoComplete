package com.example.autocomplete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autocomplete.ui.api.IFormControlValidator
import com.example.autocomplete.ui.impl.AutoComplete
import com.example.autocomplete.ui.impl.AutoCompleteBehavior
import com.example.autocomplete.ui.impl.AutoCompleteFormControl
import com.example.autocomplete.ui.theme.AutoCompleteTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


internal data class UserModel(val name: String, val age: Int)


internal class MainActivity : ComponentActivity() {
    private val _viewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel.loadUsers()
        setContent {
            AutoCompleteTheme {
                Surface(color = MaterialTheme.colors.background) {
                    LazyColumn {
                        item {
                            AutoComplete(formControl = _viewModel.userFormControl)
                        }
                    }
                }
            }
        }
    }
}


internal class UserViewModel : ViewModel() {
    private val _userState: MutableState<List<UserModel>> = mutableStateOf(listOf())
    val userFormControl = AutoCompleteFormControl(
        dataState = _userState,
        display = { it.name },
        behavior = AutoCompleteBehavior.DEFAULT,
        validators = listOf(object : IFormControlValidator<UserModel> {
            override fun validate(value: UserModel?): Boolean {
                return false
            }

        })
    )

    fun loadUsers() {
        val users = arrayListOf<UserModel>()
        viewModelScope.launch {
            for (i: Int in 1..4500) {
                users.add(UserModel("Name $i", i))
            }
            delay(3500)
            _userState.value = users
        }
    }
}


