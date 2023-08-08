package com.example.plumbus

import com.airbnb.epoxy.EpoxyController
import com.example.plumbus.databinding.ModelCharacterDetailsDataPointBinding
import com.example.plumbus.databinding.ModelCharacterDetailsHeaderBinding
import com.example.plumbus.databinding.ModelCharacterDetailsImageBinding
import com.example.plumbus.epoxy.LoadingEpoxyModel
import com.example.plumbus.epoxy.ViewBindingKotlinModel
import com.example.plumbus.network.response.GetCharacterByIdResponse
import com.squareup.picasso.Picasso

class CharacterDetailsEpoxyController : EpoxyController() {

    var isLoading: Boolean = true
        set(value) {
            field = value
            if (field) {
                requestModelBuild()
            }
        }

    var characterResponse: GetCharacterByIdResponse? = null
        set(value) {
            field = value
            if (field != null) {
                isLoading = false
                requestModelBuild()
            }
        }


    override fun buildModels() {
        if (isLoading) {
            LoadingEpoxyModel().id("loading").addTo(this)
            return
        }

        if(characterResponse == null){
            //todo error state
            return
        }

        //add header model
        HeaderEpoxyModel(
            name = characterResponse!!.name,
            gender = characterResponse!!.gender,
            status = characterResponse!!.status
        ).id("header").addTo(this)

        //add image model
        ImageEpoxyModel(imageUrl = characterResponse!!.image).id("image").addTo(this)

        //add data points model(s)
        DataPointEpoxyModel(title = "Origin", description = characterResponse!!.origin.name)
            .id("data_point_1").addTo(this)

        DataPointEpoxyModel(title = "Species", description = characterResponse!!.species)
            .id("data_point_2").addTo(this)
    }

    data class HeaderEpoxyModel(
        val name: String,
        val gender: String,
        val status: String
    ) : ViewBindingKotlinModel<ModelCharacterDetailsHeaderBinding>(R.layout.model_character_details_header) {

        override fun ModelCharacterDetailsHeaderBinding.bind() {
            nameTextView.text = name
            aliveTextView.text = status

            when {
                gender.equals("male", ignoreCase = true) ->
                    genderImageView.setImageResource(R.drawable.ic_male_24)

                gender.equals("female", ignoreCase = true) ->
                    genderImageView.setImageResource(R.drawable.ic_female_24)

                else -> genderImageView.setImageResource(R.drawable.ic_unknown_24)
            }
        }
    }

    data class ImageEpoxyModel(val imageUrl: String) :
        ViewBindingKotlinModel<ModelCharacterDetailsImageBinding>(R.layout.model_character_details_image) {

        override fun ModelCharacterDetailsImageBinding.bind() {
            Picasso.get().load(imageUrl).into(headerImageView)
        }
    }

    data class DataPointEpoxyModel(
        val title: String,
        val description: String
    ): ViewBindingKotlinModel<ModelCharacterDetailsDataPointBinding>(R.layout.model_character_details_data_point){
        override fun ModelCharacterDetailsDataPointBinding.bind() {
            labelTextView.text = title
            descriptionTextView.text = description
        }
    }
}