package com.example.todo_list.data.repository

import com.example.todo_list.testTools.TestSchedulerSetup
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class TriggeredSubjectTest {

    companion object: TestSchedulerSetup()

    @Test
    fun loadsFromDb() {
        val ts = TriggeredSubject.create(
            { Single.just("abc").delay(1, TimeUnit.MILLISECONDS) },
            { v -> v.uppercase()}
        )

        assertNull(ts.getValue())
        val observer = ts.observable.test()

        ts.startLoading(false)
        jumpMsAhead(2)
        observer.assertValue("ABC")

        ts.updateOrRefresh { "cde" }
        observer.assertValues("ABC", "CDE")

        ts.startLoading(true)
        jumpMsAhead(2)
        observer.assertValues("ABC", "CDE", "ABC")

        ts.updateOrRefresh { "fgh" }
        observer.assertValues("ABC", "CDE", "ABC", "FGH")
        ts.startLoading(true)
        ts.stopLoading()
        jumpMsAhead(2)

        observer.assertValues("ABC", "CDE", "ABC", "FGH")

        observer.dispose()
    }
}