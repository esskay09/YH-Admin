package com.terranullius.yellowheartadmin.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun HelpDialog(
    modifier: Modifier = Modifier,
    isDonatable: Boolean = true,
    description: String,
    link: String? = null,
    onHelpClicked: (Int) -> Unit
) {
    Surface() {
        Box(modifier = modifier.padding(12.dp)) {
            Column(
            ) {
                val textFieldText = remember {
                    mutableStateOf("")
                }
                val isTextFieldTextError = remember {
                    mutableStateOf(false)
                }
                Text(text = description)
                Divider(Modifier.height(12.dp), color = Color.Transparent)
                if (isDonatable) {
                    OutlinedTextField(
                        isError = isTextFieldTextError.value,
                        value = textFieldText.value,
                        onValueChange = {
                            if (it.toIntOrNull() == null) {
                                isTextFieldTextError.value = true
                                textFieldText.value = it
                            } else {
                                isTextFieldTextError.value = false
                                textFieldText.value = it
                            }
                        },
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = "Amount") },
                        keyboardOptions =
                        KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))

                HelpButton(modifier = Modifier.fillMaxWidth()) {
                    if (isDonatable) {
                        if (!isTextFieldTextError.value && textFieldText.value.isNotBlank()) {
                            onHelpClicked(
                                textFieldText.value.toInt()
                            )
                        }
                    } else {
                        onHelpClicked(10)
                    }
                }
        }
    }
}}
