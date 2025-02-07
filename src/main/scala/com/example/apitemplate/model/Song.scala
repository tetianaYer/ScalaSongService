package com.example.apitemplate.model

import io.circe.Codec

import java.util.UUID

case class Song(songUuid: UUID, length: Double, title: String, artist: String)
    derives Codec.AsObject

case class SongRequest(
                        length: Double, title: String, artist: String
                      )  derives Codec.AsObject
