package com.example.todo_list.data.repository

import io.reactivex.rxjava3.core.Single
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class TriggeredSubjectTest {
    @Test
    fun loadsFromDb() {
        val ts = TriggeredSubject.create(
            { Single.just("abc").delay(1, TimeUnit.MILLISECONDS) },
            { v -> v.uppercase()}
        )

        val receivedValues = mutableListOf<String>()
        assertNull(ts.subject.value)
        val disposable = ts.observable.subscribe(
            { receivedValues.add(it) },
            { assertTrue(false) }
        )
        ts.startLoading(false)
        Thread.sleep(2)
        assertArrayEquals(arrayOf("ABC"), receivedValues.toTypedArray())
        receivedValues.clear()

        ts.update { "cde" }
        assertArrayEquals(arrayOf("CDE"), receivedValues.toTypedArray())
        receivedValues.clear()

        ts.startLoading(true)
        Thread.sleep(2)
        assertArrayEquals(arrayOf("ABC"), receivedValues.toTypedArray())
        receivedValues.clear()

        ts.startLoading(true)
        ts.stopLoading()
        Thread.sleep(2)
        assertArrayEquals(arrayOf(), receivedValues.toTypedArray())

        disposable.dispose()
    }
}