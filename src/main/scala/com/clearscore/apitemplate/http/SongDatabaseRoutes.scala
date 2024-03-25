package com.clearscore.apitemplate.http

import cats.effect.IO
import com.clearscore.apitemplate.model.User
import com.clearscore.apitemplate.model.Song
import com.clearscore.apitemplate.service.{UserService, SongDatabaseService}
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.Http4sDsl
/*
 TODO:
   6. Update songs
 */

class SongDatabaseRoutes(
    songDatabaseService: SongDatabaseService,
//    getStartedService: UserService
) extends Http4sDsl[IO] {
  def routes: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      // Add a song
      case req @ POST -> Root / "song" => {
        for {
          decodedRequest <- req.as[Song]
          _ <- songDatabaseService.addSong(decodedRequest)
          response <- Ok(s"The song: ${decodedRequest.title} is added")
        } yield response
      }
      // TODO: 5. Delete song
//      case DELETE -> Root / "songs" / songName => {
//        Ok(songDatabaseService.getAllSongs)
//      }

      case GET -> Root / "songs" => {
        for {
          songs <- songDatabaseService.getAllSongs()
          response <- Ok(songs)
        } yield response
      }

      // TODO: 8. Get a user's fav song
      case GET -> Root / user / "favourite-song" => {
//        for {
//          decodedRequest <- cheese
//          response <- cheese
//        } yield cheese
        Ok()
      }
    }
  }
}
