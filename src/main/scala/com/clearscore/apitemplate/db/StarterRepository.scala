package com.clearscore.apitemplate.db

import cats.effect.Sync
import cats.implicits.*
import com.clearscore.apitemplate.model.TeamMember

trait StarterRepository[F[_]] {
  def getTeamMembers(): F[List[TeamMember]]
  def addTeamMember(teamMemberName: String): F[TeamMember]
  def removeTeamMember(teamMember: Int): F[Unit]
}

class StarterRepositoryImpl[F[_]: Sync] extends StarterRepository[F] {
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

  override def removeTeamMember(teamMemberId: Int): F[Unit] = {
    Sync[F].delay(StarterFakeDB.removeTeamMember(teamMemberId)).void
  }
}
