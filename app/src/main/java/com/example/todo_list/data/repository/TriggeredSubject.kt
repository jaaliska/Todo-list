package com.example.todo_list.data.repository

import android.util.Log
import com.example.todo_list.domain.model.TriggeredObservable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class TriggeredSubject<T: Any, R: Any>(
    val subject: BehaviorSubject<T>,
    override val observable: Observable<R>,
    private val loadSubjectData: () -> Single<T>
): TriggeredObservable<R> {

    private var dbLoading: Single<T>? = null
    private var dbLoadingDisposable: Disposable? = null

    override fun startLoading(forceRefresh: Boolean): Completable {
        return if (!forceRefresh && subject.hasValue())
            Completable.complete()
        else
            refreshSubject()
    }

    override fun stopLoading() {
        dbLoadingDisposable?.dispose()
    }

    fun getValue(): T? = subject.value

    fun updateOrRefresh(updateSubjValueCallback: (value: T) -> T): Completable {
        val value = subject.value
        return if (value != null) {
            subject.onNext(updateSubjValueCallback(value))
            Completable.complete()
        } else {
            refreshSubject()
        }
    }

    private fun refreshSubject(): Completable {
        var dbLoading = this.dbLoading
        if (dbLoading == null) {
            dbLoading = loadSubjectData()
                .doOnSuccess {
                    subject.onNext(it)
                }
                .doFinally {
                    this.dbLoading = null
                    this.dbLoadingDisposable = null
                }
            dbLoadingDisposable = dbLoading.subscribe({}) {
                Log.e("TriggeredSubject", "error refreshing subject: $it")
            }
            return Completable.fromSingle(dbLoading)
        }
        return Completable.fromSingle(dbLoading)
    }

    companion object {
        fun <T: Any, R: Any> create(
            loadSubjectData: () -> Single<T>,
            subjMapper: (T) -> R
        ): TriggeredSubject<T, R> {
            val subj = BehaviorSubject.create<T>()
            return TriggeredSubject(
                subj,
                subj.map(subjMapper),
                loadSubjectData
            )
        }
    }
}