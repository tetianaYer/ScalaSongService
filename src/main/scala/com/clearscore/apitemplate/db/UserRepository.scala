package com.clearscore.apitemplate.db

import cats.effect.IO
import com.clearscore.apitemplate.model.{User, UserRequest}

import java.util.UUID

trait UserRepository {
  def getUsers(): IO[List[User]]
  def addUser(user: UserRequest): IO[User]
  def deleteUser(UserName: String): IO[Option[User]]
  def addFaveSong(userUuid: String, songUuid: String): IO[Unit]
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
        newUser = User(uuid, user.userName, user.age, user.favouriteSong)
        _ <- IO(StarterFakeDB.addNewUser(newUser).toList)
      } yield newUser
    }
    override def deleteUser(
        UserName: String
    ): IO[Option[User]] = {
      for {
        _ <- IO.println("Initiated deleting of user in db: " + UserName)
        index = StarterFakeDB.usersTable.indexWhere(
          _.userName == UserName
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
    override def addFaveSong(userUuid: String, songUuid: String): IO[Unit] = IO {}

  }

}
