package com.clearscore.apitemplate

import cats.data.Kleisli
import cats.effect.*
import cats.implicits.*
import com.clearscore.apitemplate.db.{ExampleRepositoryImpl, StarterRepository}
import com.clearscore.apitemplate.http.{ExampleRoutes, GetStartedRoutes}
import com.clearscore.apitemplate.service.{
  ExampleServiceImpl,
  GetStartedServiceImpl
}
import com.comcast.ip4s.*
import org.http4s.*
import org.http4s.implicits.*
import org.http4s.jetty.server.JettyBuilder
import org.http4s.server.Router
import org.http4s.server.middleware.Logger

object Main extends IOApp {

  IO.println("Server Starting")

  private val starterRepository = StarterRepository()
  private val getStartedService =
    new GetStartedServiceImpl(starterRepository)
  private val getStartedRoutes: GetStartedRoutes =
    new GetStartedRoutes(getStartedService)

  private val exampleRepository = new ExampleRepositoryImpl
  private val exampleService = new ExampleServiceImpl(exampleRepository)
  private val exampleRoutes = new ExampleRoutes(exampleService)

  override def run(args: List[String]): IO[ExitCode] = {

    val apis = Router(
      "/" -> (getStartedRoutes.routes <+> getStartedRoutes.teamMemberRoutes()),
      "/example" -> exampleRoutes.routes
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
