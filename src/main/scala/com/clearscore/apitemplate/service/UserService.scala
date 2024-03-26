package com.clearscore.apitemplate.service

import cats.effect.kernel.Sync
import cats.effect.{IO, Sync}
import cats.implicits.*
import com.clearscore.apitemplate.db.UserRepository
import com.clearscore.apitemplate.model.{ProjectInformation, User}

import scala.util.{Failure, Success, Try}

trait UserService {
//  def getProjectInformation(): IO[Option[ProjectInformation]]
  def addUser(user: User): IO[User]
  def deleteUser(user: String): IO[Option[User]]
  // TODO: USER ID NOT USER NAME ^
}

class UserServiceImpl(
                       userRepository: UserRepository
) extends UserService {
  private val PROJECT_NAME = "New Project"

//  override def getProjectInformation(): IO[Option[ProjectInformation]] = {
//    userRepository.getUsers().map { Users =>
//      if (Users.isEmpty) {
//        None
//      } else {
//        Some(ProjectInformation(PROJECT_NAME, Users))
//      }
//    }
//  }

  override def addUser(user: User): IO[User] = {
    userRepository.addUser(user)
  }

  override def deleteUser(user: String): IO[Option[User]] = {
    userRepository.deleteUser(user)
  }
}
