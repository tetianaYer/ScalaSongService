package com.clearscore.apitemplate.model

import io.circe.Codec

import java.util.UUID

case class User(
  userUuid: UUID,
  userName: String,
  age: Option[Int],
  favouriteSong: Option[Song]
) derives Codec.AsObject

case class UserRequest(
                        userName: String,
                        age: Option[Int],
                        favouriteSong: Option[Song]
                      )  derives Codec.AsObject
