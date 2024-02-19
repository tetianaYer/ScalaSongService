package com.clearscore.apitemplate.service

import cats.effect.IO
import com.clearscore.apitemplate.db.ExampleDB.Example
import com.clearscore.apitemplate.db.ExampleRepository

import scala.util.{Failure, Success, Try}

trait ExampleService(exampleRepository: ExampleRepository) {
  def getExampleById(id: String): IO[Option[Example]]
}

class ExampleServiceImpl(exampleRepository: ExampleRepository) extends ExampleService(exampleRepository) {
    override def getExampleById(id: String): IO[Option[Example]] = {
      Try(id.toInt) match {
        case Failure(_) => IO.pure(None)
        case Success(value) => exampleRepository.getExampleById(value)
      }
    }
}
