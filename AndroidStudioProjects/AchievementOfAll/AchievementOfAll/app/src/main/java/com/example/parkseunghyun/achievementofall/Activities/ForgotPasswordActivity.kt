package com.example.parkseunghyun.achievementofall.Activities

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.parkseunghyun.achievementofall.Configurations.VolleyHttpService
import com.example.parkseunghyun.achievementofall.R
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.jetbrains.anko.startActivity
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        bt_send_email.setOnClickListener{
            println("이메일 전송~~")
        }
        goLogin.setOnClickListener {
            println("다시 로그인 창~~")

            startActivity<LoginActivity>()
            finish()
        }

    }

    // SharedPreferences
    private fun saveData(email: String, password: String){
        var auto = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = auto.edit()

        editor.putString("email", email)
                .putString("password",password)
                .apply()
    }


    // SharedPreferences (jwt-token)
    private fun saveToken(token: String){
        var auto = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = auto.edit()
        editor.putString("token", token)
                .apply()
    }


    private fun sendToken(jsonObject: JSONObject){

        VolleyHttpService.sendToken(this, jsonObject) { success ->
            if (success) {
                Toast.makeText(this, "FCM 토큰 성공", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "FCM 토큰 실패", Toast.LENGTH_LONG).show()
            }
        }
    }
}


