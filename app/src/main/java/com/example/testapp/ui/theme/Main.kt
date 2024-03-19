package com.example.testapp.ui.theme

import androidx.compose.ui.text.toUpperCase
import java.util.Scanner
import kotlin.math.max
import kotlin.random.Random

const val num = 20

fun main() {
}

suspend fun myFunc(a: Int,callBack: ()-> Unit){
    println("함수 시작!!")
    callBack()
    println("함수 끝!!")
}
