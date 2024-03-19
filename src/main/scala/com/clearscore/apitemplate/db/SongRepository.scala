package com.clearscore.apitemplate.db

import cats.effect.IO
import com.clearscore.apitemplate.model.{Song, TeamMember}
import com.clearscore.apitemplate.utils.SongDB

trait SongRepository {
  def addSong(song: Song): IO[Song]
  def getAllSongs: IO[List[Song]]
}

class SongRepositoryImpl(songDB: SongDB) extends SongRepository {
  // team member logic (deprecated) (Please rename to user and use for User routes!)
//    override def getTeamMembers(): IO[List[TeamMember]] =
//      IO(StarterFakeDB.teamMembersTable.toList)
//
//    override def addTeamMember(teamMemberName: String): IO[TeamMember] = {
//      for {
//        currentTeamMembers <- IO(StarterFakeDB.teamMembersTable)
//        id = currentTeamMembers.length
//        newTeamMember = TeamMember(id, teamMemberName)
//        _ <- IO(StarterFakeDB.addNewTeamMember(newTeamMember).toList)
//      } yield newTeamMember
//    }
//    override def deleteTeamMember(
//        teamMemberName: String
//    ): IO[Option[TeamMember]] = {
//      for {
//        _ <- IO.println("Initiated deleting of user in db: " + teamMemberName)
//        index = StarterFakeDB.teamMembersTable.indexWhere(
//          _.name == teamMemberName
//        )
//        deletedTeamMember <- IO(
//          if index == -1 then None
//          else {
//            val deleted: TeamMember = StarterFakeDB.teamMembersTable.remove(index)
//            Some(deleted)
//          }
//        )
//        _ <- IO.println("Deleted user in db: " + deletedTeamMember)
//        _ <- IO.println("Index: " + index)
//        _ <- IO.println(StarterFakeDB.teamMembersTable.toList)
//
//      } yield deletedTeamMember
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
