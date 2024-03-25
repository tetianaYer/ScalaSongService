package com.clearscore.apitemplate.model

import io.circe.generic.semiauto.deriveCodec
import io.circe.{Codec, Encoder}

case class ProjectInformation(name: String, team: List[User])

object ProjectInformation {
  implicit val codec: Codec[ProjectInformation] =
    deriveCodec[ProjectInformation]
}
