package com.example.petapp.ui.addpet


interface AddPetDataValidation {
    fun validateName(value: String): Int
    fun validateGender()
    fun validateBirthDate()
    fun validateSpecies()
    fun validateBreed()
    fun validateWeight()
    fun validateHeight()
    fun validateLength()
    fun validateCircuit()
    fun validateDescription()
}