package kr.ac.tukorea.gittest3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kr.ac.tukorea.gittest3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var layout_red: View
    private lateinit var layout_blue: View

    override fun onCreate(savedInstanceState: Bundle?) {
        val intent: Intent = Intent(this, MapsActivity::class.java)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layout_red = binding.LinearRed
        layout_blue = binding.LinearBlue
        var loginbutton = binding.loginbutton
        binding.btnRed.setOnClickListener(this)
        binding.btnBlue.setOnClickListener(this)
        loginbutton.setOnClickListener {
            startActivity(intent)
        }


    }

    override fun onClick(view: View) {
        layout_red.visibility = View.INVISIBLE
        layout_blue.visibility = View.INVISIBLE

        when (view.id) {
            R.id.btn_red -> layout_red.visibility = View.VISIBLE
            R.id.btn_blue -> layout_blue.visibility = View.VISIBLE
        }
    }
}