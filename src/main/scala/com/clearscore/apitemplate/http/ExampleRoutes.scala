package com.clearscore.apitemplate.http

import cats.effect.IO
import com.clearscore.apitemplate.service.ExampleService
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Response}

class ExampleRoutes(exampleService: ExampleService) extends Http4sDsl[IO] {
  val routes: HttpRoutes[IO] = {
    HttpRoutes
      .of[IO] {
        case GET -> Root / exampleId =>
          exampleService.getExampleById(exampleId).flatMap {
            case Some(example) => Ok(example)
            case None => NotFound()
          }
      }
  }
}
