package com.clearscore.apitemplate.db

import cats.effect.IO
import com.clearscore.apitemplate.model.{Song, SongRequest}
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.transactor.Transactor

import java.util.UUID

class DeletionException(msg: String) extends Exception
trait SongRepository {
  def addSong(song: SongRequest): IO[UUID]
  def getAllSongs(): IO[List[Song]]
  def deleteSong(songUUID: UUID): IO[Int]
  def getById(songUUID: UUID): IO[Option[Song]]
}

class SongRepositoryImpl extends SongRepository {
  // Wrapping the entire operation in an IO monad to simulate a database connection :)

  val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver", // JDBC connector
    url = "jdbc:postgresql:songsdb",
    user = "docker",
    pass = "docker"
  )
  override def addSong(songRequest: SongRequest): IO[UUID] = {
    val uuid = UUID.randomUUID()
    val song = Song(uuid, songRequest.length, songRequest.title, songRequest.artist)
    val query =
      sql"INSERT INTO songs (id, length, title, artist) VALUES ($uuid, ${song.length}, ${song.title}, ${song.artist})"
    val action = query.update.run
    action.transact(xa).map(_ => uuid)
  }

  override def getAllSongs(): IO[List[Song]] = {
    sql"SELECT * FROM songs".query[Song].to[List].transact(xa)
  }

  override def deleteSong(songUUID: UUID): IO[Int] = {
    val query =
      sql"DELETE FROM songs WHERE id = $songUUID"
    val action = query.update.run
    action.transact(xa)
  }

    override def getById(songUUID: UUID): IO[Option[Song]] = {
      sql"SELECT * FROM songs WHERE id = $songUUID".query[Song].option.transact(xa)
    }
}
