package com.clearscore.apitemplate.db

import cats.effect.IO
import com.clearscore.apitemplate.model.TeamMember

trait StarterRepository {
  def getTeamMembers(): IO[List[TeamMember]]
  def addTeamMember(teamMemberName: String): IO[TeamMember]
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
  }

}
