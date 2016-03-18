package lt.neworld.eventbustest

import android.util.Log
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.subjects.PublishSubject

/**
 * @author Andrius Semionovas
 * @since 2016-03-18
 */

class MultiEventBus(val samples: Int = 100000, val subscriptions: Int = 1000) {
    fun run(): Pair<Long, Long> {
        val startTime = System.nanoTime()

        for (i in 0..subscriptions) {
            val type = Type.get(i % Type.size)
            subjects[type]!!.observeOn(mainThread()).subscribe({ Log.d(TAG, "Value $it") }, { Log.e(TAG, "Error: $it") })
        }

        val middleTime = System.nanoTime()

        for (i in 0..samples) {
            val type = Type.get(i % Type.size)
            (subjects[type]!! as PublishSubject<Any>).onNext(createValueByType(type))
        }

        val endTime = System.nanoTime()

        return endTime - startTime to endTime - middleTime
    }

    private fun createValueByType(type: Type): Any {
        return when (type) {
            Type.BOOLEAN -> true
            Type.INT -> 1
            Type.STRING -> "foo"
            else -> Throwable()
        }
    }

    companion object {
        private const val TAG = "MultiEventBus"

        val subjects: Map<Type, PublishSubject<*>> = mapOf(
                Type.INT to PublishSubject.create<Int>(),
                Type.BOOLEAN to PublishSubject.create<Boolean>(),
                Type.STRING to PublishSubject.create<String>()
        )
    }

    enum class Type {
        INT, STRING, BOOLEAN;

        companion object {
            val size: Int = values().size
            fun get(index: Int) = values()[index]
        }
    }
}