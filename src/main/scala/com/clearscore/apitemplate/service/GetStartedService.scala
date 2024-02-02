package com.clearscore.apitemplate.service

import cats.effect.Sync
import cats.effect.kernel.Sync
import cats.implicits.*
import com.clearscore.apitemplate.db.StarterRepository
import com.clearscore.apitemplate.model.{ProjectInformation, TeamMember}

import scala.util.{Failure, Success, Try}

trait GetStartedService[F[_]] {
  def getProjectInformation(): F[Option[ProjectInformation]]
  def addTeamMember(teamMember: String): F[TeamMember]
}

class GetStartedServiceImpl[F[_]: Sync](
    startRepository: StarterRepository[F]
) extends GetStartedService[F] {
  private val PROJECT_NAME = "New Project"

  override def getProjectInformation(): F[Option[ProjectInformation]] = {
    startRepository.getTeamMembers().map { teamMembers =>
      if (teamMembers.isEmpty) {
        None
      } else {
        Some(ProjectInformation(PROJECT_NAME, teamMembers))
      }
    }
  }

  override def addTeamMember(teamMember: String): F[TeamMember] = {
    startRepository.addTeamMember(teamMember)
  }
}
