package team.b5.moviezip.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.b5.moviezip.member.model.Member

@Repository
interface MemberRepository : JpaRepository<Member, Long>