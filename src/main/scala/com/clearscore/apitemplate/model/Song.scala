package com.clearscore.apitemplate.model

import io.circe.Codec

import java.util.UUID

case class Song(uuid: UUID, length: Double, title: String, artist: String)
    derives Codec.AsObject

case class SongRequest(
                        length: Double, title: String, artist: String
                      )  derives Codec.AsObject
