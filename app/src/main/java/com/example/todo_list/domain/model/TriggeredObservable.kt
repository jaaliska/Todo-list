package com.example.todo_list.domain.model

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface TriggeredObservable<T: Any> {
    val observable: Observable<T>
    fun startLoading(forceRefresh: Boolean): Completable
    fun stopLoading()
}