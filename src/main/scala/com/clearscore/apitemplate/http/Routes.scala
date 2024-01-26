package com.clearscore.apitemplate.http

import cats.effect.Concurrent
import cats.syntax.applicative._
import org.http4s._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl
import org.typelevel.vault.Vault

class Routes[F[_]: Concurrent] extends Http4sDsl[F] {

  val testRoute1: HttpRoutes[F] =
    HttpRoutes.of[F] { case req @ POST -> Root / "test" =>
      Ok(s"You sent, ${req.body}.")
    }

  val testRoute2: HttpRoutes[F] = {
    HttpRoutes
      .of[F] {
        case GET -> Root / "hello" / name =>
          Ok(s"Hello, $name.")

        case req @ POST -> Root / "teapot" =>
          Response(
            status = Status.ImATeapot,
            httpVersion = HttpVersion.`HTTP/3`,
            headers = Headers.empty,
            body = req.body,
            attributes = Vault.empty
          ).pure
      }
  }
}
