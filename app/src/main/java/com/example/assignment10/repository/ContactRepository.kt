package com.example.assignment10.repository

import androidx.lifecycle.LiveData
import com.example.assignment10.database.dao.ContactDao
import com.example.assignment10.model.Contact
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactRepository @Inject constructor(private val contactDao: ContactDao) {

    fun insertContact(contact: Contact): Completable {
        return Completable.fromAction {
            contactDao.insertContact(contact)
        }
    }

    fun deleteContact(contact: Contact): Completable {
        return Completable.fromAction {
            contactDao.deleteContact(contact)
        }
    }

    fun updateContact(contact: Contact): Completable {
        return Completable.fromAction {   // dung fromAction thay vi fromCallable
            contactDao.updateContact(contact)
        }
    }

    fun getAllContact(): Observable<Flowable<List<Contact>>> {
        return Observable.create { emitter: ObservableEmitter<Flowable<List<Contact>>> ->
            val contacts = contactDao.getAllContact()
            emitter.onNext(contacts)
            emitter.onComplete()
        }
    }

}
