package com.example.todo_list.presentation.ui.base

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import moxy.MvpView

open class BasePresenter<T : MvpView> : MvpPresenter<T>() {

    private val lifecycleDisposables = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        lifecycleDisposables.clear()
    }

    private fun Disposable.bindToLifecycle(): Disposable {
        lifecycleDisposables.add(this)
        return this
    }

    fun <T : Any> Single<T>.subscribeByPresenter(
        onError: (Throwable) -> Unit = {},
        onSuccess: (T) -> Unit = {},
    ): Disposable {
        return this
            .subscribe(onSuccess, onError)
            .bindToLifecycle()
    }

    fun <T : Any> Maybe<T>.subscribeByPresenter(
        onError: (Throwable) -> Unit = {},
        onSuccess: (T) -> Unit = {},
    ): Disposable {
        return this
            .subscribe(onSuccess, onError)
            .bindToLifecycle()
    }

    fun Completable.subscribeByPresenter(
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
    ): Disposable {
        return this.subscribe(onComplete, onError)
            .bindToLifecycle()
    }

    fun <T : Any> Observable<T>.subscribeByPresenter(
        onError: (Throwable) -> Unit = {},
        onNext: (T) -> Unit = {}
    ): Disposable {
        return this.subscribe(onNext, onError)
            .bindToLifecycle()
    }

}