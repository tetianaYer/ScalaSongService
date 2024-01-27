package com.clearscore.apitemplate

import cats.effect.*
import com.clearscore.apitemplate.db.ExampleRepositoryImpl
import com.clearscore.apitemplate.http.{ExampleRoutes, Routes}
import com.clearscore.apitemplate.service.{ExampleService, ExampleServiceImpl}
import org.http4s.*
import org.http4s.implicits.*
import org.http4s.jetty.server.JettyBuilder
import org.http4s.server.Router

object Main extends IOApp {

  IO.println("Server Starting")

  private val routes: Routes[IO] = new Routes[IO]

  private val exampleRepository = new ExampleRepositoryImpl
  private val exampleService = new ExampleServiceImpl(exampleRepository)
  private val exampleRoutes = new ExampleRoutes(exampleService)

  override def run(args: List[String]): IO[ExitCode] = {

    val apis = Router(
      "/" -> routes.testRoute1,
      "/" -> routes.testRoute2,
      "/example" -> exampleRoutes.routes
    )

    buildServer(apis)as(ExitCode.Success)
  }

  private def buildServer(app: HttpRoutes[IO]) =
    JettyBuilder[IO]
      .bindHttp(8081, "localhost")
      .mountHttpApp(app.orNotFound, "/")
      .serve
      .compile
      .drain
}
