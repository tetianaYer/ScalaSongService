package com.clearscore.apitemplate

import cats.effect.*
import com.clearscore.apitemplate.db.*
import com.clearscore.apitemplate.http.*
import com.clearscore.apitemplate.service.{SongDatabaseServiceImpl, UserService}
import org.http4s.*
import org.http4s.implicits.*
import org.http4s.jetty.server.JettyBuilder
import org.http4s.server.Router

object Main extends IOApp {
  IO.println("Server Starting")
  private val songRepository = SongRepositoryImpl()
  private val songDatabaseService = SongDatabaseServiceImpl(songRepository)
  private val userRepository = UserRepository()
  private val userService = UserService(userRepository)
  private val songDatabaseRoutes =
    new SongDatabaseRoutes(songDatabaseService, userService)
  private val userRoutes = new UserRoutes(userService)

  override def run(args: List[String]): IO[ExitCode] = {

    val apis = Router(
      "/v1" -> songDatabaseRoutes.routes,
      "/v1" -> userRoutes.routes()
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
