package com.example.apitemplate.db

import cats.effect.IO
import cats.syntax.all.*
import com.example.apitemplate.model.{Song, User, UserRequest}
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.transactor.Transactor

import java.util.UUID

trait UserRepository {
  def getUsers: IO[List[User]]
  def addUser(user: UserRequest): IO[User]
  def deleteUser(userUuid: UUID): IO[Option[User]]
  def addFaveSong(userUuid: UUID, songUuid: UUID): IO[Option[User]]
}

class UserRepositoryImpl(songRepository: SongRepository) extends UserRepository {

    val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
      driver = "org.postgresql.Driver", // JDBC connector
      url = "jdbc:postgresql:songsdb",
      user = "docker",
      pass = "docker"
    )

    override def getUsers: IO[List[User]] = {
      sql"SELECT * FROM users".query[User].to[List].transact(xa)
    }

    override def addUser(user: UserRequest): IO[User] = {
      val uuid = UUID.randomUUID()
      val newUser = User(uuid, user.userName, user.age, user.favouriteSongUuid)
      val query =
        sql"INSERT INTO users (id, name, age, song_id) VALUES ($uuid, ${user.userName}, ${user.age}, ${user.favouriteSongUuid})"
      val action = query.update.run
      action.transact(xa).map(_ => newUser)
    }

    def getUserByUuid(userUuid: UUID): IO[Option[User]] = {
      sql"SELECT * FROM users WHERE id = $userUuid".query[User].option.transact(xa)
    }

    override def deleteUser(
        userUuid: UUID
    ): IO[Option[User]] = {
        sql"DELETE FROM users WHERE id = $userUuid RETURNING *".query[User].option.transact(xa)
    }
    override def addFaveSong(userUuid: UUID, songUuid: UUID): IO[Option[User]] = {
      // 1. Get the User
      val userId = getUserByUuid(userUuid).map(_.getOrElse(throw new Exception("User not found")))
//      println(s"User: $userId")
      // 2. Get the song
      val songId = songRepository.getById(songUuid).map(_.getOrElse(throw new Exception("Song not found")))
//      println(s"Song: $songId")
      // 3. Set users fave song to song
      sql"UPDATE users SET song_id = $songUuid WHERE id = $userUuid RETURNING *".query[User].option.transact(xa)
    }


  }

