package com.clearscore.apitemplate.model

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

case class Song(length: Double, title: String, artist: String)

object Song {
  implicit val codec: Codec[Song] =
    deriveCodec[Song]
}
