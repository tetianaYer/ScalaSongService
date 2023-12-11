package com.clearscore.apitemplate

import cats.effect._
import com.clearscore.apitemplate.http.Routes
import org.http4s._
import org.http4s.implicits._
import org.http4s.jetty.server.JettyBuilder
import org.http4s.server.Router

object Main extends IOApp:

  IO.println("Server Starting")

  private val routes: Routes[IO] = new Routes[IO]

  override def run(args: List[String]): IO[ExitCode] =

    val apis = Router(
      "/" -> routes.testRoute1,
      "/" -> routes.testRoute2
    )
    buildServer(apis)

  private def buildServer(app: HttpRoutes[IO]) =
    JettyBuilder[IO]
      .bindHttp(8081, "localhost")
      .mountHttpApp(app.orNotFound, "/")
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
