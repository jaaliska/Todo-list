package com.example.todo_list.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.todo_list.data.dao.NoteDao
import com.example.todo_list.data.mappers.DbModelMapper
import com.example.todo_list.data.model.RoomNote
import com.example.todo_list.domain.model.Note
import com.example.todo_list.domain.repository.NotesRepository
import com.example.todo_list.domain.model.TriggeredObservable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.TreeMap
import javax.inject.Inject

class CachedNotesRepository @Inject constructor(
    private val db: NoteDao
) : NotesRepository {

    private val cache = TriggeredSubject.create(this::loadAllFromDb) {
        it.values.toList()
    }

    override fun getById(id: Int): Single<Note> {
        val found = cache.subject.value?.get(id)
        if (found != null) {
            return Single.just(found)
        }
        return loadByIdFromDb(id)
    }

    override fun observeById(id: Int): TriggeredObservable<Note> {
        val observeByIdSubj = BehaviorSubject.create<Note>()

        val subscription = cache.subject
            .subscribeOn(Schedulers.io())
            .subscribe {
                val found = it[id]
                if (found != null) {
                    observeByIdSubj.onNext(found)
                }
            }
        val observable = observeByIdSubj.doOnDispose(subscription::dispose)

        return TriggeredSubject(observeByIdSubj, observable) { loadByIdFromDb(id) }
    }

    override fun observeAll(): TriggeredObservable<List<Note>> {
        return cache
    }

    override fun create(text: String, isReminderActive: Boolean): Completable {
        val roomNote = RoomNote(
            text = text,
            isReminderActive = isReminderActive,
            isCompleted = false,
        )
        return db.save(roomNote)
            .doOnSuccess { id ->
                cache.update {
                    it[id.toInt()] = Note(id.toInt(), text, false, isReminderActive)
                    it
                }
            }
            .doOnError {
                Log.e("CachedNotesRepository", "error creating '$text' note: $it")
            }
            .ignoreElement()
    }

    @SuppressLint("CheckResult")
    override fun update(
        id: Int,
        text: String?,
        isCompleted: Boolean?,
        isReminderActive: Boolean?
    ): Completable {
        val tree = cache.subject.value
        val foundNote = tree?.get(id)
        if (foundNote != null) {
            tree[id] = Note(
                id = foundNote.id,
                text = text ?: foundNote.text,
                isCompleted = isCompleted ?: foundNote.isCompleted,
                isReminderActive = isReminderActive ?: foundNote.isReminderActive
            )
            cache.subject.onNext(tree)

            updateByIdInDb(id, text, isCompleted, isCompleted)
                .subscribeOn(Schedulers.io())
                .subscribe({}) {
                    Log.e("CachedNotesRepository", "error updating note $id: $it")
                    cache.update { tree ->
                        // rollback on error
                        tree[id] = foundNote
                        tree
                    }
                }
            return Completable.complete()
        }

        return updateByIdInDb(id, text, isCompleted, isCompleted).doOnComplete {
            cache.startLoading(true)
        }
    }

    @SuppressLint("CheckResult")
    override fun deleteNote(id: Int): Completable {
        val tree = cache.subject.value
        val note = tree?.get(id)
        if (note != null) {
            tree.remove(id)
            cache.subject.onNext(tree)

            db.delete(id)
                .subscribeOn(Schedulers.io())
                .subscribe({}) {
                    Log.e("CachedNotesRepository", "error deleting note $id: $it")
                    // rollback on error
                    cache.update { tree ->
                        tree[id] = note
                        tree
                    }
                }
            return Completable.complete()
        }

        return db.delete(id).doOnComplete {
            cache.startLoading(true)
        }
    }

    private fun loadByIdFromDb(id: Int) = db.getNoteById(id).map(DbModelMapper::map)

    private fun loadAllFromDb(): Single<TreeMap<Int, Note>> {
        return db.getAll().map { list ->
            TreeMap(list.associate { it.id!! to DbModelMapper.map(it) })
        }
    }

    private fun updateByIdInDb(
        id: Int,
        text: String?,
        isCompleted: Boolean?,
        isReminderActive: Boolean?
    ): Completable {
        val actions = mutableListOf<Completable>()
        if (text != null) {
            actions.add(db.updateText(id, text))
        }
        if (isCompleted != null) {
            actions.add(db.updateCompletionState(id, isCompleted))
        }
        if (isReminderActive != null) {
            actions.add(db.updateReminderState(id, isReminderActive))
        }
        return Completable.merge(actions)
    }
}