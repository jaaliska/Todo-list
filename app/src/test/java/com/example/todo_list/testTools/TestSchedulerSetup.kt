package com.example.todo_list.testTools

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.AfterClass
import java.util.concurrent.TimeUnit

open class TestSchedulerSetup {
    private val scheduler = TestScheduler()

    init {
        RxJavaPlugins.setIoSchedulerHandler { scheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { scheduler }
        RxJavaPlugins.setComputationSchedulerHandler { scheduler }
        RxJavaPlugins.setSingleSchedulerHandler { scheduler }
        RxAndroidPlugins.setMainThreadSchedulerHandler { scheduler }
    }

    @AfterClass
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    fun jumpMsAhead(ms: Long) {
        scheduler.advanceTimeBy(ms, TimeUnit.MILLISECONDS)
    }
}