package com.clearscore.apitemplate.db

import cats.effect.IO
import com.clearscore.apitemplate.model.User

trait UserRepository {
  def getUsers(): IO[List[User]]
  def addUser(user: User): IO[User]
  def deleteUser(UserName: String): IO[Option[User]]
}

object UserRepository {
  def apply(): UserRepository = new StarterRepositoryImpl()
  private class StarterRepositoryImpl() extends UserRepository {
    override def getUsers(): IO[List[User]] =
      IO(StarterFakeDB.usersTable.toList)

    override def addUser(user: User): IO[User] = {
      for {
        currentUsers <- IO(StarterFakeDB.usersTable)
        id = currentUsers.length
        newUser = User(id, user.userName, user.age, user.favouriteSong)
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
  }

}
