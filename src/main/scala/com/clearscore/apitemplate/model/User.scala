package com.clearscore.apitemplate.model

import io.circe.Codec

case class User(
    id: Int,
    userName: String,
    age: Option[Int],
    favouriteSong: Option[Song]
) derives Codec.AsObject
