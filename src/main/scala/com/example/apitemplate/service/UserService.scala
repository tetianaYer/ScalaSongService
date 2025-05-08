package com.example.apitemplate.service

import cats.effect.IO
import com.example.apitemplate.db.{SongRepository, UserRepository}
import com.example.apitemplate.model.{User, UserRequest}
import doobie.util.transactor.Transactor
import doobie.postgres.implicits.*
import doobie.implicits.*

import java.util.UUID

trait UserService {
  def addUser(user: UserRequest): IO[User]

  def getUsers: IO[List[User]]
  def getUser(userUuid: UUID): IO[Option[User]]
  def deleteUser(userUuid: UUID): IO[Option[User]]
  def addFaveSong(userUuid: UUID, songUuid: UUID): IO[Option[User]]
}

class UserServiceImpl(
                       userRepository: UserRepository,
                       songRepository: SongRepository

) extends UserService {

  override def getUsers: IO[List[User]] = userRepository.getUsers
  override def addUser(user: UserRequest): IO[User] = {
    userRepository.addUser(user)

  }

  def getUser(userUuid: UUID): IO[Option[User]] = {
    userRepository.getUserByUuid(userUuid)
  }

  override def deleteUser(userUuid: UUID): IO[Option[User]] = {
    userRepository.deleteUser(userUuid)
  }

  override def addFaveSong(userUuid: UUID, songUuid: UUID): IO[Option[User]] = {
    userRepository.addFaveSong(userUuid, songUuid)
  }
}
