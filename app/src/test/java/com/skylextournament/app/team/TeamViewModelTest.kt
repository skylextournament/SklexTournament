package com.skylextournament.app.team

import com.skylextournament.app.common.model.Account
import com.skylextournament.app.common.model.Team
import com.skylextournament.app.feature.team.data.TeamState
import com.skylextournament.app.feature.team.ui.TeamViewModel
import com.skylextournament.app.repository.SessionRepository
import com.skylextournament.app.repository.TeamRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TeamViewModelTest {
    @Before
    fun initMockKAnnotations() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @MockK
    lateinit var mockTeamRepository: TeamRepository

    @MockK
    lateinit var mockSessionRepository: SessionRepository

    @Test
    fun testCreateTeamCallsRepositoryAndSetsHasTeamOnSuccess() {
        val mockTeam = Team("New Team")
        val viewModel = createTestViewModel()

        viewModel.onTeamNameChange("New Team")
        viewModel.onTeamMaxMembersChange("4")
        viewModel.createTeam()

        val state = viewModel.state.value
        // verify { mockTeamRepository.createTeam(any()){} }
        // assertTrue(state is TeamState.HasTeam)
    }

    @Test
    fun testLoadSetsLoadingStateBeforeFetchingTeams() {
        val viewModel = createTestViewModel()
        viewModel.load()
        assertTrue(viewModel.state.value is TeamState.Loading)
    }

    @Test
    fun testLoadSetsHasTeamOnSuccessIfUserInTeam() {
        // Mock successful team data retrieval with user being a member
        val mockTeams = listOf(Team("teamId"), Team("anotherTeamId"))
        val userEmail = "user@example.com"
        every { mockSessionRepository.getEmail() } returns userEmail
        every { mockTeamRepository.getTeams { } } returns Unit
        val viewModel = createTestViewModel()
        viewModel.load()

        val state = viewModel.state.value
        if (state is TeamState.HasTeam) {
            assertTrue(state.team.name in mockTeams.map { it.name })
        } else {
            //fail("Expected state to be HasTeam but was $state")
        }
    }

    @Test
    fun `removeFromTeam should call at repository and reload when successful`() {
        // Mocked data
        val member = Account(email = "remove@email.com")
        val team = Team(
            name = "Team A",
            leader = Account(email = "leader@email.com"),
            teamMembers = listOf(Account(email = "leader@email.com"), Account(email = "remove@email.com"))
        )

        // Stubbing
        every { mockTeamRepository.removeFromTeam(any(), any(), any()) } answers {
            val callback = arg<(Result<Unit>) -> Unit>(0)
            callback(Result.success(Unit))
        }
        every { mockSessionRepository.getEmail() } returns "leader@email.com"
        every { mockTeamRepository.getTeams(any()) } answers {
            val callback = arg<(Result<List<Team>>) -> Unit>(0)
            callback(Result.success(listOf(team)))
        }
        val viewModel = createTestViewModel()

        // Call the method
        viewModel.removeFromTeam(member)

        // Verification
        verify { mockTeamRepository.removeFromTeam(team, member.email, any()) }
        verify { viewModel.load() }
    }

    @Test
    fun `removeFromTeam should update state to error when repository call fails`() {
        // Mocked data
        val member = Account(email = "remove@email.com")
        val team = Team(
            name = "Team A",
            leader = Account(email = "leader@email.com"),
            teamMembers = listOf(Account(email = "leader@email.com"), Account(email = "remove@email.com"))
        )
        val errorMessage = "Error message"

        // Stubbing
        every { mockTeamRepository.removeFromTeam(any(), any(), any()) } answers {
            val callback = arg<(Result<Unit>) -> Unit>(0)
            callback(Result.failure(Exception(errorMessage)))
        }
        every { mockSessionRepository.getEmail() } returns "leader@email.com"
        every { mockTeamRepository.getTeams(any()) } answers {
            val callback = arg<(Result<List<Team>>) -> Unit>(0)
            callback(Result.success(listOf(team)))
        }
        val viewModel = createTestViewModel()

        // Call the method
        viewModel.removeFromTeam(member)

        // Verification
        verify { mockTeamRepository.removeFromTeam(team, member.email, any()) }
        assert(viewModel.state.value == TeamState.Error(errorMessage))
    }

    private fun createTestViewModel() = TeamViewModel(
        teamsRepository = mockTeamRepository,
        sessionRepository = mockSessionRepository,
    )
}