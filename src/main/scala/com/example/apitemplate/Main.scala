package com.example.apitemplate

import cats.effect.*
import com.example.apitemplate.db.*
import com.example.apitemplate.http.*
import com.example.apitemplate.service.{SongDatabaseServiceImpl, UserService, UserServiceImpl}
import org.http4s.*
import org.http4s.implicits.*
import org.http4s.jetty.server.JettyBuilder
import org.http4s.server.Router
import org.http4s.server.middleware.CORS

object Main extends IOApp {
  IO.println("Server Starting")
  private val songRepository = SongRepositoryImpl()
  private val songDatabaseService = SongDatabaseServiceImpl(songRepository)
  private val userRepository = UserRepositoryImpl(songRepository)
  private val userService = UserServiceImpl(userRepository, songRepository)
  private val songDatabaseRoutes =
    new SongDatabaseRoutes(songDatabaseService, userService)
  private val userRoutes = new UserRoutes(userService, songDatabaseService)

  override def run(args: List[String]): IO[ExitCode] = {

    val apis = Router(
      "/v1" -> songDatabaseRoutes.routes,
      "/v1" -> userRoutes.routes()
    )
    val corsPolicy = CORS.policy.withAllowOriginAll
    val corsApis = corsPolicy(apis)
    buildServer(corsApis) as (ExitCode.Success)
  }

  private def buildServer(app: HttpRoutes[IO]) =
    JettyBuilder[IO]
      .bindHttp(8080, "localhost")
      .mountHttpApp(app.orNotFound, "/")
      .serve
      .compile
      .drain
}
