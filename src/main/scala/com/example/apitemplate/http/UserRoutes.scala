package com.example.apitemplate.http

import com.example.apitemplate.model.UserRequest
import cats.effect.IO
import com.example.apitemplate.service.{SongDatabaseService, UserService}
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher

import java.util.UUID

object SongQueryParamMatcher
    extends QueryParamDecoderMatcher[String]("songUuid")
object UserQueryParamMatcher
    extends QueryParamDecoderMatcher[String]("userUuid")

class UserRoutes(userService: UserService,
                 songDatabaseService: SongDatabaseService,
                ) extends Http4sDsl[IO] {
  def routes(): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case GET -> Root / "users" =>
        for {
          _ <- IO.println(s"getting users")
          users <- userService.getUsers
          response <- Ok(users)
        } yield response


      case req @ POST -> Root / "user" =>
        for {
          decodedRequest <- req.as[UserRequest]
          _ <- IO.println(s"adding user: $decodedRequest")
          user <- userService.addUser(decodedRequest)
          response <- Created(user)
        } yield response

      case PUT -> Root / "user" / UUIDVar(userUuid) =>
          userService.deleteUser(userUuid).map(
            user => Ok(s"User deleted ${user.map(_.userName)}")
          ).handleError(
            _ => NotFound(s"Error: no user $userUuid")
          ).flatten

      // TODO: 7  Add user's fav song
      case POST -> Root / "users" / "add-favourite-song" :? UserQueryParamMatcher(
            userUuid
          ) +& SongQueryParamMatcher(songUuid) =>
          userService.addFaveSong(
            UUID.fromString(userUuid),
            UUID.fromString(songUuid)
          ).map(
            updatedUser => Ok(s"The song ${updatedUser.map(_.favouriteSongUuid)} is now your favourite song!")
          ).handleError(
            _ => NotFound(s"Error: no user $userUuid or song $songUuid can be found")
          ).flatten

      // TODO: 8 Get a user's fav song
      case GET -> Root / "users" / userUuid / "favourite-songs" =>
        //        for {
        //          decodedRequest <- cheese
        //          response <- cheese
        //        } yield cheese
        Ok()

    }
  }
}
