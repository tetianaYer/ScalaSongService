package com.clearscore.apitemplate.http

import cats.effect.IO
import com.clearscore.apitemplate.model.SongRequest
import com.clearscore.apitemplate.service.{SongDatabaseService, UserService}
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.Http4sDsl

import java.util.UUID
/*
 TODO:
   6. Update songs
 */

class SongDatabaseRoutes(
    songDatabaseService: SongDatabaseService,
    userService: UserService
) extends Http4sDsl[IO] {
  def routes: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      // Add a song
      case req @ POST -> Root / "songs" => {
        for {
          decodedRequest <- req.as[SongRequest]
          _ <- songDatabaseService.addSong(decodedRequest)
          response <- Ok(s"The song: ${decodedRequest.title} is added")
        } yield response
      }
      case req @ DELETE -> Root / "songs" / songUUID => {
        val uuid = UUID.fromString(songUUID)
        Ok(songDatabaseService.deleteSong(uuid))
      }

      case GET -> Root / "songs" => {
        for {
          songs <- songDatabaseService.getAllSongs()
          response <- Ok(songs)
        } yield response
      }
      // TODO: 7  Add user's fav song
//      case POST -> Root / "users" / userUuid /  "favourite-songs" / songUuid => {
//        for {
//          _ <- userService.addFaveSong(userUuid, songUuid)
//          response <- Ok(s"The song: ${decodedRequest.title} is now your favourite song!")
//        } yield response
//        Ok()
//      }
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
