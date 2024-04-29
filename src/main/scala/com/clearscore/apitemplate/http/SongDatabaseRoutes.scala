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
          song <- songDatabaseService.addSong(decodedRequest)
          response <- Ok(song)
        } yield response
      }
      // DELETE song
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
    }
  }
}
