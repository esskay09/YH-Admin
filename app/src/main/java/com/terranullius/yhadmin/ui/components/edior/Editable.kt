package com.terranullius.yhadmin.ui.components.edior

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction

@Composable
fun Editable(isEditing: Boolean, modifier: Modifier = Modifier, initialText: String= "Placeholder", textStyle: TextStyle = LocalTextStyle.current,label : @Composable () -> Unit = {}, errorCondition: (String) -> Boolean = {false},  updatedText: (String) -> Unit = {} ) {
    var isTextFieldTextError by remember{
        mutableStateOf(false)
    }
    var textFieldText by remember(initialText) {
       mutableStateOf(initialText)
    }
    if(isEditing)TextField(
        isError = isTextFieldTextError,
        value = textFieldText,
        onValueChange = {
            if (errorCondition(it)) isTextFieldTextError = true
            else {
                isTextFieldTextError = false
                textFieldText = it
                updatedText(it)
            }
        },
        label = label,
        modifier = modifier,
        textStyle = textStyle,
        keyboardActions =KeyboardActions{
            this.defaultKeyboardAction(ImeAction.Done)
        }
    ) else{
        Text(text = textFieldText, style = textStyle, modifier= modifier)
    }
}