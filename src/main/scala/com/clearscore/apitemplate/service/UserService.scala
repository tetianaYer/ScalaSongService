package com.clearscore.apitemplate.service

import cats.effect.IO
import com.clearscore.apitemplate.db.UserRepository
import com.clearscore.apitemplate.model.{User, UserRequest}

trait UserService {
//  def getProjectInformation(): IO[Option[ProjectInformation]]
  def addUser(user: UserRequest): IO[User]
  def deleteUser(user: String): IO[Option[User]]
}

object UserService {
  def apply(userRepository: UserRepository) = new UserServiceImpl(userRepository: UserRepository)
  
}

class UserServiceImpl(
                       userRepository: UserRepository
) extends UserService {
  override def addUser(user: UserRequest): IO[User] = {
    userRepository.addUser(user)
    
  }
  
  

  override def deleteUser(user: String): IO[Option[User]] = {
    userRepository.deleteUser(user)
  }

//  override def addFaveSong(userUuid: String, songUuid: String): Unit = {
//    userRepository.addFaveSong(userUuid,songUuid)
//  }
}
