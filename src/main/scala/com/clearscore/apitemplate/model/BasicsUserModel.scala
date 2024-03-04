package com.clearscore.apitemplate.model

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

case class BasicsUserModel(userName: String, age: Int, favouriteSong: Song)
object BasicsUserModel {
  implicit val codec: Codec[BasicsUserModel] =
    deriveCodec[BasicsUserModel]
}
