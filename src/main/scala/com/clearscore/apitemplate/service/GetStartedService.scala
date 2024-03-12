package com.clearscore.apitemplate.service

import cats.effect.kernel.Sync
import cats.effect.{IO, Sync}
import cats.implicits.*
import com.clearscore.apitemplate.db.StarterRepository
import com.clearscore.apitemplate.model.{ProjectInformation, TeamMember}

import scala.util.{Failure, Success, Try}

trait GetStartedService {
  def getProjectInformation(): IO[Option[ProjectInformation]]
  def addTeamMember(teamMember: String): IO[TeamMember]
  def deleteTeamMember(teamMember: String): IO[Option[TeamMember]]
}

class GetStartedServiceImpl(
    startRepository: StarterRepository
) extends GetStartedService {
  private val PROJECT_NAME = "New Project"

  override def getProjectInformation(): IO[Option[ProjectInformation]] = {
    startRepository.getTeamMembers().map { teamMembers =>
      if (teamMembers.isEmpty) {
        None
      } else {
        Some(ProjectInformation(PROJECT_NAME, teamMembers))
      }
    }
  }

  override def addTeamMember(teamMember: String): IO[TeamMember] = {
    startRepository.addTeamMember(teamMember)
  }

  override def deleteTeamMember(teamMember: String): IO[Option[TeamMember]] = {
    startRepository.deleteTeamMember(teamMember)
  }
}
