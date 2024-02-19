package com.clearscore.apitemplate.db

import cats.effect.Sync
import cats.syntax.all.*
import com.clearscore.apitemplate.model.TeamMember

trait StarterRepository[F[*]] {
  def getTeamMembers(): F[List[TeamMember]]
  def addTeamMember(teamMemberName: String): F[TeamMember]
}

object StarterRepository {
  def apply[F[*]: Sync](): StarterRepository[F] = new StarterRepositoryImpl[F]()
  private class StarterRepositoryImpl[F[*]: Sync]()
      extends StarterRepository[F] {
    override def getTeamMembers(): F[List[TeamMember]] =
      Sync[F].delay(StarterFakeDB.teamMembersTable.toList)

    override def addTeamMember(teamMemberName: String): F[TeamMember] = {
      for {
        currentTeamMembers <- Sync[F].delay(StarterFakeDB.teamMembersTable)
        id = currentTeamMembers.length
        newTeamMember = TeamMember(id, teamMemberName)
        _ <- Sync[F].delay(StarterFakeDB.addNewTeamMember(newTeamMember).toList)
      } yield newTeamMember
    }
  }

}
