package com.clearscore.apitemplate.db

import cats.effect.IO
import com.clearscore.apitemplate.model.{User, Song, SongRequest}

import java.util.UUID

class DeletionException(msg: String) extends Exception
trait SongRepository {
  def addSong(song: SongRequest): IO[Song]
  def getAllSongs(): IO[List[Song]]
  def deleteSong(songUUID: UUID): IO[Unit]
}

class SongRepositoryImpl extends SongRepository {
  // Wrapping the entire operation in an IO monad to simulate a database connection :)
  override def addSong(song: SongRequest): IO[Song] = {
    for {
      currentSongs <- IO(StarterFakeDB.songsTable)
      uuid = UUID.randomUUID()
      newSong = Song(uuid, song.length, song.title, song.artist)
      _ <- IO(StarterFakeDB.addNewSong(newSong).toList)
    } yield newSong
  }

  override def getAllSongs(): IO[List[Song]] = IO {
    StarterFakeDB.songsTable.toList
  }

  override def deleteSong(songUUID: UUID): IO[Unit] = IO {
    for {
      _ <- IO.println("Initiated deleting of song in db: " + songUUID)
      index = StarterFakeDB.songsTable.indexWhere(
        _.uuid == songUUID
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
