package com.example.todo_list.data.repository

import com.example.todo_list.data.model.RoomNote
import com.example.todo_list.domain.model.Note
import com.example.todo_list.testTools.TestSchedulerSetup
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class CachedNotesRepositoryTest {

    companion object: TestSchedulerSetup()

    private lateinit var repo: CachedNotesRepository
    private lateinit var dao: NoteDaoFake

    @Before
    fun setUp() {
        dao = spy(NoteDaoFake(1))
        repo = CachedNotesRepository(dao)
    }

    @Test
    fun cachesData() {
        dao.saveDirect(RoomNote(null, "abc", isCompleted = true, isReminderActive = false))

        val allObserver = repo.observeAll()
        allObserver.observable.test().assertNoValues()
        allObserver.startLoading(false)
        jumpMsAhead(2)
        allObserver.observable.test().assertValue(listOf(
            Note(1, "abc", isCompleted = true, isReminderActive = false)
        ))
        verify(dao).getAll()
        clearInvocations(dao)

        repo.getById(1).test().assertValue(
            Note(1, "abc", isCompleted = true, isReminderActive = false)
        )
        verifyNoInteractions(dao)

        repo.deleteNote(1).test().assertComplete()
        allObserver.observable.test().assertValue(listOf())
        verify(dao).delete(1)
        verifyNoMoreInteractions(dao)

        allObserver.startLoading(false)
        verifyNoMoreInteractions(dao)
    }

    @Test
    fun callsOnlyNeededUpdateMethods() {
        val createObserver = repo.create("abc", true).test()
        createObserver.assertNotComplete()
        verify(dao).save(RoomNote(null, "abc", isCompleted = false, isReminderActive = true))
        verifyNoMoreInteractions(dao)
        jumpMsAhead(2)

        verify(dao).getAll()
        verifyNoMoreInteractions(dao)
        clearInvocations(dao)
        createObserver.assertComplete()

        repo.update(1, null, isCompleted = null, isReminderActive = null)
            .test().assertComplete()
        verifyNoMoreInteractions(dao)

        repo.update(1, null, isCompleted = null, isReminderActive = false)
        verify(dao).updateReminderState(1, false)
        verifyNoMoreInteractions(dao)
        clearInvocations(dao)

        repo.update(1, null, isCompleted = true, isReminderActive = null)
        verify(dao).updateCompletionState(1, true)
        verifyNoMoreInteractions(dao)
        clearInvocations(dao)

        repo.update(1, "def", isCompleted = null, isReminderActive = null)
        verify(dao).updateText(1, "def")
        verifyNoMoreInteractions(dao)
        clearInvocations(dao)

        repo.update(1, "x", isCompleted = false, isReminderActive = true)
        verify(dao).updateReminderState(1, true)
        verify(dao).updateCompletionState(1, false)
        verify(dao).updateText(1, "x")
        verifyNoMoreInteractions(dao)
        clearInvocations(dao)
    }
}