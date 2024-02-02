package com.clearscore.apitemplate.model

import io.circe.generic.semiauto.deriveCodec
import io.circe.{Codec, Encoder, Json}
import org.http4s.EntityEncoder

case class ProjectInformation(name: String, team: List[TeamMember])

object ProjectInformation {
  implicit val codec: Codec[ProjectInformation] =
    deriveCodec[ProjectInformation]
}
