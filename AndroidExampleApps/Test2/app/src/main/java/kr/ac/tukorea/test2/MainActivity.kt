package kr.ac.tukorea.test2

import android.app.Activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kr.ac.tukorea.test2.databinding.ActivityMainBinding
import android.content.Intent
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, Default::class.java)

        binding.button.setOnClickListener{
            intent.putExtra("data1",binding.editText1.text.toString().toInt())
            intent.putExtra("data2", binding.editText2.text.toString().toInt())

            startActivityForResult(intent,10)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==10 && resultCode == Activity.RESULT_OK){
            val result = data!!.getIntExtra("resultData",0)
            Toast.makeText(this.applicationContext,result.toString(),Toast.LENGTH_SHORT).show()
        }
    }
}