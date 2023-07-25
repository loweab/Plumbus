package com.example.plumbus

import TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.plumbus.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val rickAndMortyService: RickAndMortyService = retrofit.create(RickAndMortyService::class.java)

        rickAndMortyService.getCharacterById(244).enqueue(object : Callback<GetCharacterByIdResponse>{
            override fun onResponse(call: Call<GetCharacterByIdResponse>, response: Response<GetCharacterByIdResponse>) {
                Log.i(TAG, response.toString())
                
                if(!response.isSuccessful){
                    Toast.makeText(this@MainActivity, "Unsucessful Network call", Toast.LENGTH_SHORT).show()
                    return
                }

                val body = response.body()!!
                val name = body.name

                binding.textView.text = name
            }

            override fun onFailure(call: Call<GetCharacterByIdResponse>, t: Throwable) {
                Log.i(TAG, t.message ?: "null message")
            }
        })
    }
}