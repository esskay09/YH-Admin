package com.terranullius.yellowheart.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import terranullius.yellowheart.R

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
                    mutableStateOf(" ")
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
                            }
                            textFieldText.value = it
                        },
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = "Amount") },
                        keyboardOptions =
                        KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        )
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Button(modifier = Modifier
                    .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    ),
                    onClick = {
                        if (isDonatable) {
                            if (!isTextFieldTextError.value) {
                                onHelpClicked(
                                    textFieldText.value.toInt()
                                )
                            }
                        } else {
                            onHelpClicked(0)
                        }
                    }) {
                    Text(text = "Send")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_heart_filled),
                        tint = MaterialTheme.colors.primary,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}
