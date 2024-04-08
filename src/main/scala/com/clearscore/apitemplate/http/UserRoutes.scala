package com.clearscore.apitemplate.http

import cats.effect.IO
import com.clearscore.apitemplate.model.UserRequest
import com.clearscore.apitemplate.service.UserService
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.{
  circeEntityDecoder,
  circeEntityEncoder
}
import org.http4s.dsl.Http4sDsl

class UserRoutes(userService: UserService) extends Http4sDsl[IO] {
  def UserRoutes(): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
//      case GET -> Root / "user" / user => {
//        for {
//          _ <- IO.println(s"getting user: $user")
//          user <- userService.getUser(user)
//          response <- user match {
//            case Some(user) => Ok(user)
//            case None =>
//              NotFound(s"Error: no user $user")
//          }
//        } yield response
//      }
      case req @ POST -> Root / "user" => {
        for {
          decodedRequest <- req.as[UserRequest]
          response <- Created(userService.addUser(decodedRequest))
        } yield response
      }
      case DELETE -> Root / "user" / user => {
        for {
          _ <- IO.println(s"deleting user: $user")
          deletedUser <- userService.deleteUser(user)
          response <- deletedUser match {
            case Some(deletedUser) => Ok(deletedUser)
            case None =>
              NotFound(s"Error: no user $user")
          }
        } yield response
      }
    }
  }
}
