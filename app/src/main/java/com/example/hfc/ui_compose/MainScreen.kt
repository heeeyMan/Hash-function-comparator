package com.example.hfc.ui_compose

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hfc.R
import com.example.hfc.data_models.ServiceTypes
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MainScreen(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(35.dp))
        TableResult(
            timeCppData = viewModel.workingCppTimeText,
            timeKotlinData = viewModel.workingKotlinTimeText,
            isShowCppProgressBar = viewModel.isShowCppProgressBar,
            isShowKotlinProgressBar = viewModel.isShowKotlinProgressBar
        )
        Spacer(modifier = Modifier.height(15.dp))
        InputEditText(
            inputTextState = viewModel.inputText,
            changeInputText = {viewModel.changeInputText(it)}
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = stringResource(id = R.string.current_service),
            color = Color.Black
        )
        CustomExposedDropdownMenuBox(
            currentServiceType = viewModel.currentServiceType,
            serviceTypes = viewModel.serviceTypes,
            changeCurrentServiceType = {viewModel.changeServiceType(it)}
        )
        Spacer(modifier = Modifier.height(25.dp))
        CalculateButton(
            isKotlinActiveState = viewModel.isShowKotlinProgressBar,
            isCppActiveState = viewModel.isShowCppProgressBar
        ) {
            viewModel.getTimeHashFunction()
        }
    }
}

@Composable
private fun TableResult(
    timeCppData: StateFlow<String>,
    timeKotlinData: StateFlow<String>,
    isShowKotlinProgressBar: StateFlow<Boolean>,
    isShowCppProgressBar: StateFlow<Boolean>
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StaticTextField(
            headerText = stringResource(id = R.string.cpp_time),
            messageState = timeCppData,
            modifier = Modifier.weight(1f),
            isShowProgressBarState = isShowCppProgressBar
        )
        StaticTextField(
            headerText = stringResource(id = R.string.kotlin_time),
            messageState = timeKotlinData,
            modifier = Modifier.weight(1f),
            isShowProgressBarState = isShowKotlinProgressBar
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomExposedDropdownMenuBox(
    currentServiceType: StateFlow<ServiceTypes>,
    serviceTypes: Array<ServiceTypes>,
    changeCurrentServiceType: (type: ServiceTypes) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val selectedText by currentServiceType.collectAsState()
    Box {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText.valueName,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.None
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                serviceTypes.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.valueName) },
                        onClick = {
                            changeCurrentServiceType(item)
                            expanded = false
                            Toast.makeText(context, item.valueName, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InputEditText(
    inputTextState: StateFlow<String>,
    changeInputText: (text: String) -> Unit
) {
    val text by inputTextState.collectAsState()
    Box {
        TextField(
            value = text,
            onValueChange = {
                changeInputText(it)
            },
            label = { Text(stringResource(id = R.string.input_text_placeholder)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}

@Composable
fun StaticTextField(
    headerText: String,
    messageState: StateFlow<String>,
    isShowProgressBarState: StateFlow<Boolean>,
    modifier: Modifier = Modifier
) {
    val isShow by isShowProgressBarState.collectAsState()
    val text by messageState.collectAsState()
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .then(modifier)
    ) {
        Text(
            text = headerText,
            modifier = Modifier
                .background(color = Color.LightGray)
                .align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            color = Color.DarkGray,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Box(modifier = Modifier
            .heightIn(min = 80.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(10.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            if(isShow) {
                CustomCircularProgressBar()
            } else {
                Text(
                    text = text,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                )
            }
        }
    }
}

@Composable
private fun CustomCircularProgressBar(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = Modifier
            .size(30.dp)
            .then(modifier),
        color = Color.Black,
        strokeWidth = 2.dp)

}

@Composable
fun CalculateButton(
    isKotlinActiveState: StateFlow<Boolean>,
    isCppActiveState: StateFlow<Boolean>,
    onClick: () -> Unit
) {
    val isKotlinActive by isKotlinActiveState.collectAsState()
    val isCppActive by isCppActiveState.collectAsState()
    val isActive = !isKotlinActive || !isCppActive
    OutlinedButton(
        onClick = onClick,
        enabled = !isKotlinActive,
        modifier= Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(12.dp),
        border= BorderStroke(
            width = 1.dp,
            color = getStateColor(isActive)
        ),
        colors = ButtonDefaults.outlinedButtonColors(contentColor =  getStateColor(isActive))
    ) {
        Text(
            text = stringResource(id = R.string.calculate_button_text),
            color = getStateColor(isActive)
        )
    }
}

private fun getStateColor(
    isActive: Boolean,
    activeColor: Color = Color.Black,
    passiveColor: Color = Color.Gray
): Color = if(isActive) activeColor else passiveColor

@Preview
@Composable
fun Preview() {
    //InputEditText()
    //StaticTextField("Скорость функции С++", "1243")
}