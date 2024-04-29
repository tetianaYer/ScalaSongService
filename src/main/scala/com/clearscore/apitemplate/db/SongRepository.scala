package com.clearscore.apitemplate.db

import cats.effect.IO
import com.clearscore.apitemplate.model.{Song, SongRequest, User}
import cats.MonadThrow
import cats.effect.kernel.{MonadCancelThrow, Resource}
import cats.effect.{IO, IOApp}
import doobie.ExecutionContexts
import doobie.Fragments
import doobie.hikari.HikariTransactor
import doobie.util.transactor.Transactor
import doobie.implicits.*
import doobie.postgres.implicits._
import java.util.UUID

class DeletionException(msg: String) extends Exception
trait SongRepository {
  def addSong(song: SongRequest): IO[Int]
  def getAllSongs(): IO[List[Song]]
  def deleteSong(songUUID: UUID): IO[Unit]
}

class SongRepositoryImpl extends SongRepository {
  // Wrapping the entire operation in an IO monad to simulate a database connection :)

  val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver", // JDBC connector
    url = "jdbc:postgresql:songsdb",
    user = "docker",
    pass = "docker"
  )
  override def addSong(songRequest: SongRequest): IO[Int] = {
    val uuid = UUID.randomUUID()
    val song = Song(uuid, songRequest.length, songRequest.title, songRequest.artist)
    val query =
      sql"INSERT INTO songs (id, length, title, artist) VALUES ($uuid, ${song.length}, ${song.title}, ${song.artist})"
    val action = query.update.run
    action.transact(xa)
  }

  override def getAllSongs(): IO[List[Song]] = {
    sql"SELECT * FROM songs".query[Song].to[List].transact(xa)
  }



  override def deleteSong(songUUID: UUID): IO[Unit] = IO {
    for {
      _ <- IO.println("Initiated deleting of song in db: " + songUUID)
      index = StarterFakeDB.songsTable.indexWhere(
        _.songUuid == songUUID
      )
      deletedSong <- IO(
        if index == -1 then throw new DeletionException("Not found!!!!!")
        else {
          val deleted: Song = StarterFakeDB.songsTable.remove(index)
          Some(deleted)
        }
      )
      _ <- IO.println("Deleted song in db: " + deletedSong)
      _ <- IO.println("Index: " + index)
      _ <- IO.println(StarterFakeDB.usersTable.toList)

    } yield ()
  }
}
