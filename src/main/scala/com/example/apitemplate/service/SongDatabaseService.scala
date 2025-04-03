package com.example.apitemplate.service

import cats.effect.IO
import com.example.apitemplate.db.SongRepository
import com.example.apitemplate.model.{Song, SongRequest}

import java.util.UUID

trait SongDatabaseService {
  def addSong(song: SongRequest): IO[Song]
//  def deleteSong(song: Song): IO[Option[Song]]
  def getAllSongs: IO[List[Song]]
  def deleteSong(uuid: UUID): IO[Int]
  def getSongById(UUID: UUID): IO[Option[Song]]
}

class SongDatabaseServiceImpl(
    songRepository: SongRepository
) extends SongDatabaseService {


  override def addSong(song: SongRequest): IO[Song] = {
    songRepository.addSong(song)
  }
  override def deleteSong(songUUID: UUID): IO[Int] = {
  // We need to throw an error if the song doesnt exist
    songRepository.deleteSong(songUUID)
  }

  override def getAllSongs: IO[List[Song]] =
    songRepository.getAllSongs

  override def getSongById(songUUID: UUID): IO[Option[Song]] = {
    // We need to throw an error if the song doesnt exist
    songRepository.getById(songUUID)
  }
}
