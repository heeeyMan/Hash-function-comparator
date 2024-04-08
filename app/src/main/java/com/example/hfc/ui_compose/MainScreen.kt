package com.example.hfc.ui_compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(35.dp))
        Row {
            StaticTextField("Text1", "1234")
            StaticTextField("Text2", "4321")
        }
        Spacer(modifier = Modifier.height(15.dp))
        InputEditText()
        Spacer(modifier = Modifier.height(25.dp))
        ControlButton(onClick = {})
    }
}

@Composable
fun InputEditText() {
    var text by rememberSaveable { mutableStateOf("") }
    Box {
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = { Text("Введите message для hash функции") },
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        )
        CustomCircularProgressBar(Modifier.align(Alignment.TopEnd).padding(end = 5.dp))
    }
}

@Composable
fun StaticTextField(headerText: String, message: String) {
    Column(
        modifier = Modifier.background(Color.LightGray)
    ) {
        Text(
            text = headerText,
            modifier = Modifier
                .background(
                    color = Color.LightGray,
                )
                .align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            color = Color.DarkGray,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = message,
            modifier = Modifier
                .wrapContentHeight()
                .padding(10.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black,
        )
    }
}

@Composable
private fun CustomCircularProgressBar(modifier: Modifier){
    CircularProgressIndicator(
        modifier = Modifier.size(25.dp).then(modifier),
        color = Color.Magenta,
        strokeWidth = 2.dp)

}

@Composable
fun ControlButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier= Modifier.fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(12.dp),
        border= BorderStroke(1.dp, Color.Black),
        contentPadding = PaddingValues(0.dp),  //avoid the little icon
        colors = ButtonDefaults.outlinedButtonColors(contentColor =  Color.Black)
    ) {
        Text(text = "Рассчитать скорость функции")
    }
}

@Preview
@Composable
fun Preview() {
    InputEditText()
    //StaticTextField("Скорость функции С++", "1243")
}