package com.example.plumbus

import TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.plumbus.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this).get(SharedViewModel::class.java)
    }

    private val epoxyController = CharacterDetailsEpoxyController()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel.characterByIdLiveData.observe(this){ response ->

            epoxyController.characterResponse = response

            if(response == null){
                Toast.makeText(this@MainActivity, "Unsucessful Network call", Toast.LENGTH_SHORT).show()
                return@observe
            }
        }

        viewModel.refreshCharacter(54)

        binding.epoxyRecyclerView.setControllerAndBuildModels(epoxyController)
    }
}