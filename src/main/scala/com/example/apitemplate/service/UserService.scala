package com.example.apitemplate.service

import cats.effect.IO
import com.example.apitemplate.db.UserRepository
import com.example.apitemplate.model.{User, UserRequest}

import java.util.UUID

trait UserService {
//  def getProjectInformation(): IO[Option[ProjectInformation]]
  def addUser(user: UserRequest): IO[User]

  def getUsers(): IO[List[User]]
  def deleteUser(user: String): IO[Option[User]]
  def addFaveSong(userUuid: UUID, songUuid: UUID): IO[Unit]
}

object UserService {
  def apply(userRepository: UserRepository) = new UserServiceImpl(userRepository: UserRepository)
}

class UserServiceImpl(
                       userRepository: UserRepository
) extends UserService {

  override def getUsers(): IO[List[User]] = userRepository.getUsers()
  override def addUser(user: UserRequest): IO[User] = {
    userRepository.addUser(user)

  }

  override def deleteUser(user: String): IO[Option[User]] = {
    userRepository.deleteUser(user)
  }

  override def addFaveSong(userUuid: UUID, songUuid: UUID): IO[Unit] = {
    userRepository.addFaveSong(userUuid, songUuid)
  }
}
