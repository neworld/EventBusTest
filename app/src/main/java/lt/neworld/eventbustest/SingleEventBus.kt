package lt.neworld.eventbustest

import android.util.Log
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.observers.Subscribers
import rx.subjects.PublishSubject

/**
 * @author Andrius Semionovas
 * @since 2016-03-18
 */

class SingleEventBus(val samples: Int = 100000, val subscriptions: Int = 1000) {

    fun run(): Pair<Long, Long> {
        val startTime = System.nanoTime()

        for (i in 0..subscriptions) {
            subject
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createSubscriberByType(Type.get(i % Type.size)))
        }

        val middleTime = System.nanoTime()

        for (i in 0..samples) {
            subject.onNext(createValueByType(Type.get(i % Type.size)))
        }

        val endTime = System.nanoTime()

        return endTime - startTime to endTime - middleTime
    }

    private fun createSubscriberByType(type: Type): Subscriber<Any> {
        val action: Action1<Any> = when (type) {
            Type.INT -> Action1 {
                when (it) {
                    is Int -> Log.d(TAG, "This is int")
                }
            }
            Type.BOOLEAN -> Action1 {
                when (it) {
                    is Boolean -> Log.d(TAG, "This is boolean")
                }
            }
            Type.STRING -> Action1 {
                when (it) {
                    is String -> Log.d(TAG, "This is string")
                }
            }
            Type.MULTI -> Action1 {
                when (it) {
                    is Int -> Log.d(TAG, "This is multi and int")
                    is Boolean -> Log.d(TAG, "This is multi and boolean")
                }
            }
            else -> throw RuntimeException("Not supported")
        }

        return Subscribers.create<Any>(action, Action1 { Log.e(TAG, "Error: $it") })
    }

    private fun createValueByType(type: Type): Any {
        return when (type) {
            Type.BOOLEAN -> true
            Type.INT -> 1
            Type.MULTI -> true
            Type.STRING -> "foo"
            else -> Throwable()
        }
    }

    companion object {
        private const val TAG = "SingleEventBus"

        val subject = PublishSubject.create<Any>()
    }

    enum class Type {
        INT, STRING, BOOLEAN, MULTI;

        companion object {
            val size: Int = values().size
            fun get(index: Int) = values()[index]
        }
    }
}