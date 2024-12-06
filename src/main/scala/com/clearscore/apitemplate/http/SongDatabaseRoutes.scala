package com.clearscore.apitemplate.http

import cats.effect.IO
import com.clearscore.apitemplate.model.SongRequest
import com.clearscore.apitemplate.service.{SongDatabaseService, UserService}
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.*
import org.http4s.server.middleware.CORSConfig
import org.http4s.server.middleware.CORS

import java.util.UUID
import scala.concurrent.duration.*

/*
 TODO:
   6. Update songs
 */

class SongDatabaseRoutes(
    songDatabaseService: SongDatabaseService,
    userService: UserService
) extends Http4sDsl[IO] {
  private val corsPolicy = CORS.policy
    .withAllowOriginAll
    .withAllowMethodsIn(Set(Method.GET, Method.POST, Method.DELETE))
    .withMaxAge(1.day)

  def routes: HttpRoutes[IO] =  {
    corsPolicy(HttpRoutes.of[IO] {
      // Add a song
      case req @ POST -> Root / "songs" => {
        for {
          decodedRequest <- req.as[SongRequest]
          song <- songDatabaseService.addSong(decodedRequest)
          response <- Created(s"Song added with uuid: $song")
        } yield response
      }
      // DELETE song
      case DELETE -> Root / "songs" / UUIDVar(songUUID) =>
        songDatabaseService.deleteSong(songUUID)
            .map(_ => Ok(s"Song deleted"))
            .handleError(_ => NotFound())
            .flatten

      case GET -> Root / "songs" => {
        for {
          songs <- songDatabaseService.getAllSongs()
          response <- Ok(songs)
        } yield response
      }

      case GET -> Root / "songs" / UUIDVar(songUUID)   => {
        songDatabaseService.getSongById(songUUID)
          .flatMap {
            case Some(song) => Ok(song)
            case None => NotFound()
          }
      }
    }
    )
  }
}
