package com.clearscore.apitemplate.http

import cats.effect.Sync
import cats.implicits.*
import com.clearscore.apitemplate.service.GetStartedService
import org.http4s.*
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.dsl.Http4sDsl

class GetStartedRoutes[F[_]: Sync](getStartedService: GetStartedService[F])
    extends Http4sDsl[F] {
  val routes: HttpRoutes[F] =
    HttpRoutes.of[F] {
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
}
