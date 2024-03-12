package com.clearscore.apitemplate.db

import cats.effect.IO
import com.clearscore.apitemplate.model.TeamMember

trait StarterRepository {
  def getTeamMembers(): IO[List[TeamMember]]
  def addTeamMember(teamMemberName: String): IO[TeamMember]
  def deleteTeamMember(teamMemberName: String): IO[Option[TeamMember]]
}

object StarterRepository {
  def apply(): StarterRepository = new StarterRepositoryImpl()
  private class StarterRepositoryImpl() extends StarterRepository {
    override def getTeamMembers(): IO[List[TeamMember]] =
      IO(StarterFakeDB.teamMembersTable.toList)

    override def addTeamMember(teamMemberName: String): IO[TeamMember] = {
      for {
        currentTeamMembers <- IO(StarterFakeDB.teamMembersTable)
        id = currentTeamMembers.length
        newTeamMember = TeamMember(id, teamMemberName)
        _ <- IO(StarterFakeDB.addNewTeamMember(newTeamMember).toList)
      } yield newTeamMember
    }
    override def deleteTeamMember(
        teamMemberName: String
    ): IO[Option[TeamMember]] = {
      for {
        _ <- IO.println("Initiated deleting of user in db: " + teamMemberName)
        index = StarterFakeDB.teamMembersTable.indexWhere(
          _.name == teamMemberName
        )
        deletedTeamMember <- IO(
          if index == -1 then None
          else {
            val deleted: TeamMember = StarterFakeDB.teamMembersTable.remove(index)
            Some(deleted)
          }
        )
        _ <- IO.println("Deleted user in db: " + deletedTeamMember)
        _ <- IO.println("Index: " + index)
        _ <- IO.println(StarterFakeDB.teamMembersTable.toList)

      } yield deletedTeamMember
    }
  }

}
