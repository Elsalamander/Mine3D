package it.elsalamander.mine3d

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/****************************************************************
 * Activity di launch dell'app
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //imposta lo schermo
        setContentView(R.layout.activity_main)
    }
}