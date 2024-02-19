package com.clearscore.apitemplate.model

import io.circe.generic.semiauto.deriveCodec
import io.circe.{Codec, Encoder}

case class TeamMember(id: Int, name: String)

object TeamMember {
  implicit val codec: Codec[TeamMember] =
    deriveCodec[TeamMember]
}
