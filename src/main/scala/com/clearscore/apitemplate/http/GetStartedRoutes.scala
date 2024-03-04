package com.clearscore.apitemplate.http

import cats.effect.IO
import com.clearscore.apitemplate.model.BasicsUserModel
import com.clearscore.apitemplate.service.GetStartedService
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.{
  circeEntityDecoder,
  circeEntityEncoder
}
import org.http4s.dsl.Http4sDsl

class GetStartedRoutes(getStartedService: GetStartedService)
    extends Http4sDsl[IO] {
  val routes: HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root => Ok("What are you actually looking for?")
      case GET -> Root / "project-information" => {
        for {
          maybeProjectInformation <- getStartedService.getProjectInformation()
          response <- maybeProjectInformation match {
            case Some(projectInformation) => Ok(projectInformation)
            case None =>
              NotFound("uh oh, no team mates are working on this project")
          }
        } yield response
      }
      case req @ POST -> Root / "team-member" / teamMember => {
        Created(getStartedService.addTeamMember(teamMember))
      }
    }

  def teamMemberRoutes(): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case req @ POST -> Root / "user" => {
        for {
          decodedRequest <- req.as[BasicsUserModel]
          response <- Ok(s"You sent, $decodedRequest.")
        } yield response
      }
      //      case  DELETE -> Root / "user" / userName => {
      //        for {
      //          decodedDeleteRequest <-
      //            response <- Ok(s"deleted user: $user ")
      //        } yield response
      //      }
    }
  }
}
