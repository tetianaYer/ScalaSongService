package com.clearscore.apitemplate.http

import cats.effect.IO
import com.clearscore.apitemplate.model.UserRequest
import com.clearscore.apitemplate.service.UserService
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.{
  OptionalValidatingQueryParamDecoderMatcher,
  QueryParamDecoderMatcher
}

import java.util.UUID

object SongQueryParamMatcher
    extends QueryParamDecoderMatcher[String]("songUuid")
object UserQueryParamMatcher
    extends QueryParamDecoderMatcher[String]("userUuid")

class UserRoutes(userService: UserService) extends Http4sDsl[IO] {
  def routes(): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case GET -> Root / "users" => {
        for {
          _ <- IO.println(s"getting users")
          users <- userService.getUsers()
          response <- Ok(users)
        } yield response
      }

      case req @ POST -> Root / "user" => {
        for {
          decodedRequest <- req.as[UserRequest]
          _ <- IO.println(s"adding user: $decodedRequest")
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
      // TODO: 7  Add user's fav song
      case POST -> Root / "users" / "add-favourite-song" :? UserQueryParamMatcher(
            userUuid
          ) +& SongQueryParamMatcher(songUuid) => {
        for {
          _ <- userService.addFaveSong(
            UUID.fromString(userUuid),
            UUID.fromString(songUuid)
          )
          response <- Ok(s"The song: $songUuid is now your favourite song!")
        } yield response
      }
      // TODO: 8 Get a user's fav song
      case GET -> Root / "users" / userUuid / "favourite-songs" => {
        //        for {
        //          decodedRequest <- cheese
        //          response <- cheese
        //        } yield cheese
        Ok()
      }
    }
  }
}
