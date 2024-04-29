package kr.ac.tukorea.test2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Default : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val num1 = intent.getIntExtra("data1",0)
        val num2 = intent.getIntExtra("data2",0)

        val result = num1 + num2
        intent.putExtra("resultData", result)
        setResult(RESULT_OK, intent)
        finish()
    }
}