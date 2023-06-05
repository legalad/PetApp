package com.example.petapp.model

import android.net.Uri

interface HandleCameraEvents {
    fun handleImageCapture (uri: Uri)
    fun handleShowCamera()
}