package com.clearscore.apitemplate.service

import cats.effect.IO
import com.clearscore.apitemplate.db.SongRepository
import com.clearscore.apitemplate.model.{Song, SongRequest}

import java.util.UUID

trait SongDatabaseService {
  def addSong(song: SongRequest): IO[Int]
//  def deleteSong(song: Song): IO[Option[Song]]
  def getAllSongs(): IO[List[Song]]
  def deleteSong(uuid: UUID): IO[Unit]
}

class SongDatabaseServiceImpl(
    songRepository: SongRepository
) extends SongDatabaseService {


  override def addSong(song: SongRequest): IO[Int] = {
  // TODO: Check if song exists before adding
  // This logic should be handled in this service, making multiple calls to the repo if necessary
  // e.g.
//  {
//    if !(songRepository.songExists(song)) then {
//      songRepository.addSong(song) 
//    }
//    song
//  }
    songRepository.addSong(song)
  }
  override def deleteSong(songUUID: UUID): IO[Unit] = {
  // This logic should be handled in this service, making multiple calls to the repo if necessary
  // e.g.
    songRepository.deleteSong(songUUID)
  }

  override def getAllSongs(): IO[List[Song]] =
    songRepository.getAllSongs()
}
