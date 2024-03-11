package com.clearscore.apitemplate.db

import com.clearscore.apitemplate.model.TeamMember

import scala.collection.mutable.ListBuffer

object StarterFakeDB {
  // Note - this is a mutable val, we do not use these in practice, this is just to demo a basic fake database in memory
  val teamMembersTable = ListBuffer.empty[TeamMember]

  def addNewTeamMember(teamMember: TeamMember): ListBuffer[TeamMember] =
    teamMembersTable += teamMember

  def deleteTeamMember(teamMember: TeamMember): ListBuffer[TeamMember] =
    teamMembersTable -= teamMember


}
