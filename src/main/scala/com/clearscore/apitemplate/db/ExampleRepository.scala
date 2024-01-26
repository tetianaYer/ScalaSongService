package com.clearscore.apitemplate.db

import cats.effect.IO
import cats.implicits.*
import com.clearscore.apitemplate.db.ExampleDB.Example
import cats.implicits.catsSyntaxApplicativeId
import cats.syntax.all.catsSyntaxApplicativeId
import cats.syntax.applicative.catsSyntaxApplicativeId
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

object ExampleDB {
    case class Example(id: Int, name: String)

    val db = Seq(
      Example(1, "example1"),
      Example(2, "example2"),
      Example(3, "example3"),
    )
    object Example {
      implicit val exampleEncoder: Encoder[Example] = new Encoder[Example] {
        final def apply(a: Example): Json = Json.obj(
          ("id", Json.fromInt(a.id)),
          ("name", Json.fromString(a.name)),
        )
      }
      implicit val exampleEntityEncoder: EntityEncoder[IO, Example] =
        jsonEncoderOf[IO, Example]
      }
}

trait ExampleRepository {
  def getExampleById(id: Int): IO[Option[Example]]
}

class ExampleRepositoryImpl extends ExampleRepository {
  def getExampleById(id: Int): IO[Option[Example]] = {
    ExampleDB.db.find(_.id == id).pure[IO]
  }

}
