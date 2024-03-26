package com.clearscore.apitemplate.service

import cats.effect.kernel.Sync
import cats.effect.{IO, Sync}
import cats.implicits.*
import com.clearscore.apitemplate.db.{SongRepository, UserRepository}
import com.clearscore.apitemplate.model.{ProjectInformation, Song, User}
import com.clearscore.apitemplate.utils.SongDB

import scala.util.{Failure, Success, Try}

trait SongDatabaseService {
  def addSong(song: Song): IO[Song]
//  def deleteSong(song: Song): IO[Option[Song]]
  def getAllSongs(): IO[List[Song]]
}

class SongDatabaseServiceImpl(
    songRepository: SongRepository
) extends SongDatabaseService {


  override def addSong(song: Song): IO[Song] = {
  // TODO: Check if song exists before adding
  // This logic should be handled in this service, making multiple calls to the repo if necessary
  // e.g.
//  {
//    if !(songRepo.songExists(song)) then {
//      songRepo.addSong(song) 
//    }
//    song
//  }
    songRepository.addSong(song)
  }

  override def getAllSongs(): IO[List[Song]] =
    songRepository.getAllSongs
}
