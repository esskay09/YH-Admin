package com.terranullius.yellowheartadmin.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.terranullius.yellowheartadmin.data.Initiative
import com.terranullius.yellowheartadmin.ui.components.edior.Editable

@Composable
fun HelpDialog(
    modifier: Modifier = Modifier,
    onHelpClicked: (Int) -> Unit,
    updatedInitative: MutableState<Initiative>,
    isEditing: Boolean
) {
    Surface() {
        Box(modifier = modifier.padding(12.dp)) {

            if (isEditing){
                var isSwitchChecked by remember {
                    mutableStateOf(updatedInitative.value.isDonatable)
                }
               Switch(modifier = Modifier.align(Alignment.TopEnd),checked = isSwitchChecked, onCheckedChange = {
                   isSwitchChecked = it
                   updatedInitative.value.isDonatable = it
               })
            }
            Column(
            ) {
                val textFieldText = remember {
                    mutableStateOf("")
                }
                val isTextFieldTextError = remember {
                    mutableStateOf(false)
                }
                Editable(isEditing = isEditing, initialText = updatedInitative.value.helpDescription ?: "placeholder"){
                    updatedInitative.value.helpDescription = it
                }
                Divider(Modifier.height(12.dp), color = Color.Transparent)
                if (updatedInitative.value.isDonatable) {
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
                } else {
                    if (isEditing){
                        Editable(isEditing = isEditing, initialText = "Help Link", modifier = Modifier
                            .fillMaxWidth()){
                            updatedInitative.value.helpLink = it
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))

                HelpButton(modifier = Modifier.fillMaxWidth()) {
                    if (updatedInitative.value.isDonatable) {
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
