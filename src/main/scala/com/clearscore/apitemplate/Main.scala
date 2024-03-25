package com.clearscore.apitemplate

import cats.data.Kleisli
import cats.effect.*
import cats.syntax.all.*
import com.clearscore.apitemplate.db.{ExampleRepositoryImpl, SongRepositoryImpl, UserRepository}
import com.clearscore.apitemplate.http.SongDatabaseRoutes
import com.clearscore.apitemplate.model.{Song, User}
import com.clearscore.apitemplate.service.{SongDatabaseService, SongDatabaseServiceImpl, UserService}
import com.clearscore.apitemplate.utils.SongDB
import org.http4s.*
import org.http4s.implicits.*
import org.http4s.jetty.server.JettyBuilder
import org.http4s.server.Router

object Main extends IOApp {
  val userDefault = User(
    id = 0,
    userName = "Obama",
    age = Some(59),
    favouriteSong = Song(3.22, "Hey Jude", "The Beatles").some
  )
  IO.println("Server Starting")


  private val songRepository = new SongRepositoryImpl(new SongDB())
  private val songDatabaseService = new SongDatabaseServiceImpl(songRepository)
//  private val userService = new UserService()
  private val songDatabaseRoutes = new SongDatabaseRoutes(songDatabaseService)
  
  override def run(args: List[String]): IO[ExitCode] = {

    val apis = Router(
      "/v1" -> songDatabaseRoutes.routes
    )
    buildServer(apis) as (ExitCode.Success)
  }

  private def buildServer(app: HttpRoutes[IO]) =
    JettyBuilder[IO]
      .bindHttp(8081, "localhost")
      .mountHttpApp(app.orNotFound, "/")
      .serve
      .compile
      .drain
}
