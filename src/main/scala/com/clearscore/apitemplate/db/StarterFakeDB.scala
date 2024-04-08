package com.clearscore.apitemplate.db

import com.clearscore.apitemplate.model.{Song, User}

import scala.collection.mutable.ListBuffer

object StarterFakeDB {
  // Note - this is a mutable val, we do not use these in practice, this is just to demo a basic fake database in memory
  val usersTable = ListBuffer.empty[User]
  val songsTable = ListBuffer.empty[Song]

  def addNewUser(user: User): ListBuffer[User] =
    usersTable += user

  def deleteUser(user: User): ListBuffer[User] =
    usersTable -= user


  def addNewSong(song: Song): ListBuffer[Song] =
    songsTable += song

}
