package com.example.apitemplate.db

import cats.effect.IO
import cats.syntax.all.*
import com.example.apitemplate.model.{Song, User, UserRequest}

import java.util.UUID

trait UserRepository {
  def getUsers(): IO[List[User]]
  def addUser(user: UserRequest): IO[User]
  def deleteUser(name: String): IO[Option[User]]
  def addFaveSong(userUuid: UUID, songUuid: UUID): IO[Unit]

  def allUsers(): IO[Unit]
}

object UserRepository {
  def apply(): UserRepository = new StarterRepositoryImpl()
  private class StarterRepositoryImpl() extends UserRepository {
    override def getUsers(): IO[List[User]] =
      IO(StarterFakeDB.usersTable.toList)

    override def addUser(user: UserRequest): IO[User] = {
      for {
        currentUsers <- IO(StarterFakeDB.usersTable)
        uuid = UUID.randomUUID()
        newUser = User(uuid, user.userName, user.age, user.favouriteSongUuid)
        _ <- IO(StarterFakeDB.addNewUser(newUser).toList)
      } yield newUser
    }

    override def allUsers(): IO[Unit] = {
      IO(StarterFakeDB.usersTable.toList)
    }
    override def deleteUser(
        name: String
    ): IO[Option[User]] = {
      for {
        _ <- IO.println("Initiated deleting of user in db: " + name)
        index = StarterFakeDB.usersTable.indexWhere(
          _.userName == name
        )
        deletedUser <- IO(
          if index == -1 then None
          else {
            val deleted: User = StarterFakeDB.usersTable.remove(index)
            Some(deleted)
          }
        )
        _ <- IO.println("Deleted user in db: " + deletedUser)
        _ <- IO.println("Index: " + index)
        _ <- IO.println(StarterFakeDB.usersTable.toList)

      } yield deletedUser
    }
    override def addFaveSong(userUuid: UUID, songUuid: UUID): IO[Unit] = IO {
      // 2. Get the User
      val userIndex = StarterFakeDB.usersTable.indexWhere(_.userUuid === userUuid)
      val user: User = StarterFakeDB.usersTable.remove(userIndex)
      val newUser = user.copy(favouriteSongUuid = songUuid.some)
      // 3. Set users fave song to song
      StarterFakeDB.usersTable.addOne(newUser)
    }


  }

}
