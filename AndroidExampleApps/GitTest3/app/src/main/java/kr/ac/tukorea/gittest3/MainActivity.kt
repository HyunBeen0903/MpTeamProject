package kr.ac.tukorea.gittest3

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var layout_red: View
    private lateinit var layout_green: View
    private lateinit var layout_blue: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        InitializeView()
    }

    private fun InitializeView() {
        layout_red = findViewById(R.id.Linear_Red)
        layout_blue = findViewById(R.id.Linear_blue)
        layout_green = findViewById(R.id.Linear_green)

        findViewById<View>(R.id.btn_red).setOnClickListener(this)
        findViewById<View>(R.id.btn_blue).setOnClickListener(this)
        findViewById<View>(R.id.btn_green).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        layout_red.visibility = View.INVISIBLE
        layout_blue.visibility = View.INVISIBLE
        layout_green.visibility = View.INVISIBLE

        when (view.id) {
            R.id.btn_red -> layout_red.visibility = View.VISIBLE
            R.id.btn_blue -> layout_blue.visibility = View.VISIBLE
            R.id.btn_green -> layout_green.visibility = View.VISIBLE
        }
    }
}