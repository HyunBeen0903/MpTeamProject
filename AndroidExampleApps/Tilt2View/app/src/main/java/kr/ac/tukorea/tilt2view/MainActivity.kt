package kr.ac.tukorea.tilt2view

import TlitView
import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.abs

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var tiltView: TlitView

    private val sensorManager by lazy{
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private val audioManager by lazy{
        getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private val vibrator by lazy{
        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        tiltView = TlitView(this)
        setContentView(tiltView)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        event.let{
            //Log.d("MainActivity", "onSensorChanged: " + "x: ${event.values[0]}, y : ${event.values[1]}, z: ${event.values[2]}" )
        }
        tiltView.onSensorEvent(event)
        if (abs(event.values[0] * 40) > 200 || abs(event.values[1] * 40) > 200) {
            audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK)

            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            Log.d("Make event","vibrator!!!!!!")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("MainActivity", "Sensor accuracy changed: $accuracy")
    }
}