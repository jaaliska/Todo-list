package com.example.todo_list.presentation.ui.base

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpAppCompatFragment

open class BaseFragment : MvpAppCompatFragment() {

    private val lifecycleDisposables = CompositeDisposable()

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleDisposables.clear()
    }

    private fun Disposable.bindToLifecycle(): Disposable {
        lifecycleDisposables.add(this)
        return this
    }

    fun <T : Any> Single<T>.subscribeInLifecycle(
        onError: (Throwable) -> Unit = {},
        onNext: (T) -> Unit = {}
    ): Disposable {
        return this
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext, onError)
            .bindToLifecycle()
    }

    fun <T : Any> Maybe<T>.subscribeInLifecycle(
        onError: (Throwable) -> Unit = {},
        onNext: (T) -> Unit = {}
    ): Disposable {
        return this
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext, onError)
            .bindToLifecycle()
    }

    fun Completable.subscribeInLifecycle(
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {}
    ): Disposable {
        return this.observeOn(AndroidSchedulers.mainThread())
            .subscribe(onComplete, onError)
            .bindToLifecycle()
    }

    fun <T : Any> Observable<T>.subscribeInLifecycle(
        onError: (Throwable) -> Unit = {},
        onComplete: (T) -> Unit = {}
    ): Disposable {
        return this.observeOn(AndroidSchedulers.mainThread())
            .subscribe(onComplete, onError)
            .bindToLifecycle()
    }

}