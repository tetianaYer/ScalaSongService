package com.clearscore.apitemplate.model

import io.circe.Codec

case class Song(length: Double, title: String, artist: String)
    derives Codec.AsObject
