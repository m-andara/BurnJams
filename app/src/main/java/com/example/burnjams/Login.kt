package com.example.burnjams

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.burnjams.databinding.ActivityLoginBinding
import com.example.burnjams.entities.Code
import com.example.burnjams.entities.Token
import com.example.burnjams.networking.SpotifyApiNetworking
import com.example.burnjams.repository.BurnJamsDatabase
import com.example.burnjams.repository.BurnJamsRepository
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse


class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var database: BurnJamsDatabase? = null
    private val authTokenRequestCode = 0x10
    private val authCodeRequestCode = 0x11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = BurnJamsRepository.inItDb(applicationContext)
        clearCredentialsClicked()
        login()
    }

    private fun login() {
        requestTokenClicked()
    }

    private fun onRequestCodeClicked() {
        val request = BurnJamsRepository.getAuthenticationRequest(AuthorizationResponse.Type.CODE, getClientId())
        AuthorizationClient.openLoginActivity(this, authCodeRequestCode, request)
    }

    private fun requestTokenClicked() {
        val request = BurnJamsRepository.getAuthenticationRequest(AuthorizationResponse.Type.TOKEN, getClientId())
        AuthorizationClient.openLoginActivity(this, authTokenRequestCode, request)
    }

    private fun clearCredentialsClicked() {
        AuthorizationClient.clearCookies(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthorizationClient.getResponse(resultCode, data)
        if (authTokenRequestCode == requestCode) {
            val mAccessToken = response.accessToken
            val token = Token(
                    tokenId = 1,
                    token = mAccessToken
            )

            SpotifyApiNetworking.getUserProfile(mAccessToken)
            AsyncTask.execute {
                val currentToken = database?.burnJamsDao()?.getToken()
                if (currentToken == null) database?.burnJamsDao()?.add(token)
                else database?.burnJamsDao()?.update(token)
            }
            onRequestCodeClicked()
        } else if (authCodeRequestCode == requestCode) {
            val mAccessCode = response.code
            val code = Code(
                    codeId = 1,
                    code = mAccessCode
            )
            AsyncTask.execute {
                val currentCode = database?.burnJamsDao()?.getCode()
                if (currentCode == null) database?.burnJamsDao()?.add(code)
                else database?.burnJamsDao()?.update(code)
            }
            goToHome()
        }
    }

    private fun getClientId(): String {
        return getString(R.string.spotify_client_id)
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}