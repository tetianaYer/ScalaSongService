package com.clearscore.apitemplate.http

import cats.effect.IO
import com.clearscore.apitemplate.model.BasicsUserModel
import com.clearscore.apitemplate.model.Song
import com.clearscore.apitemplate.service.SongDatabaseService
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.{
  circeEntityDecoder,
  circeEntityEncoder
}
import org.http4s.dsl.Http4sDsl
/*
 TODO:
   1. Add user
   2. Delete user
   3. Update user
   6. Update songs
*/

class SongDatabaseRoutes (songDatabaseService: SongDatabaseService)
  extends Http4sDsl[IO] {
  def routes: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
//      case req@POST -> Root / "team-member" => {
//        for {
//          decodedRequest <- req.as[BasicsUserModel]
//          response <- Ok(s"You sent, $decodedRequest.")
//        } yield response
//      }
//      case DELETE -> Root / "team-member" / teamMember => {
//        for {
//          _ <- IO.println(s"deleting user: $teamMember")
//          deletedUser <- songDatabaseService.deleteTeamMember(teamMember)
//          response <- deletedUser match {
//            case Some(deletedUser) => Ok(deletedUser)
//            case None =>
//              NotFound("uh oh, no team mates are working on this project")
//          }
//          //            response <-  Ok(getStartedService.deleteTeamMember(teamMember))
//          //          Ok(s"deleted user: $teamMember")
//        } yield response
//      }

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
      case GET -> Root / teamMember / "favourite-song" => {
//        for {
//          decodedRequest <- cheese
//          response <- cheese
//        } yield cheese
          Ok()
      }
    }
  }
}
