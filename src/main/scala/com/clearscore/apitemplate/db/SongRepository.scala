package com.clearscore.apitemplate.db

import cats.effect.IO
import com.clearscore.apitemplate.model.{Song, User}
import com.clearscore.apitemplate.utils.SongDB

trait SongRepository {
  def addSong(song: Song): IO[Song]
  def getAllSongs: IO[List[Song]]
}

class SongRepositoryImpl(songDB: SongDB) extends SongRepository {
  // team member logic (deprecated) (Please rename to user and use for User routes!)
//    override def getUsers(): IO[List[User]] =
//      IO(StarterFakeDB.UsersTable.toList)
//
//    override def addUser(UserName: String): IO[User] = {
//      for {
//        currentUsers <- IO(StarterFakeDB.UsersTable)
//        id = currentUsers.length
//        newUser = User(id, UserName)
//        _ <- IO(StarterFakeDB.addNewUser(newUser).toList)
//      } yield newUser
//    }
//    override def deleteUser(
//        UserName: String
//    ): IO[Option[User]] = {
//      for {
//        _ <- IO.println("Initiated deleting of user in db: " + UserName)
//        index = StarterFakeDB.UsersTable.indexWhere(
//          _.name == UserName
//        )
//        deletedUser <- IO(
//          if index == -1 then None
//          else {
//            val deleted: User = StarterFakeDB.UsersTable.remove(index)
//            Some(deleted)
//          }
//        )
//        _ <- IO.println("Deleted user in db: " + deletedUser)
//        _ <- IO.println("Index: " + index)
//        _ <- IO.println(StarterFakeDB.UsersTable.toList)
//
//      } yield deletedUser
//    }

  // Wrapping the entire operation in an IO monad to simulate a database connection :)
  override def addSong(song: Song): IO[Song] = IO {
    songDB.songs += song
    song
  }

  override def getAllSongs: IO[List[Song]] = IO {
    songDB.songs.toList
  }

}
