import kotlinx.atomicfu.*

/**
 * @author Алентьев Кирилл
 */

enum class OutComeStatus {
    UNKNOWN, FAILED, COMPLETE
}

interface Signature<E> {
    fun dcss(reference :Reference<E>, expected :E, update :Any?, outcomeSignature: Cas2Signature<E>) : Boolean

    fun complete()
}

class Reference<E>(startVal :E) {
    val temp = atomic<Any?>(startVal)

    var cell: E
        get() {
            while (true) {
                val currentValue = temp.value
                if (currentValue is Signature<*>) {
                    currentValue.complete()
                } else {
                    return currentValue as E
                }
            }
        }
        set(value) {
            while (true) {
                val currentValue = temp.value
                if (currentValue is Signature<*>) {
                    currentValue.complete()
                } else {
                    if (temp.compareAndSet(currentValue, value)) {
                        return
                    }
                }
            }
        }

    fun cAS(expected: Any?, update: Any?): Boolean {
        while (true) {
            val currentValue = temp.value
            if (currentValue is Signature<*>) {
                currentValue.complete()
                continue
            }
            if (currentValue == expected) {
                if (temp.compareAndSet(currentValue, update)) {
                    return true
                }
                continue
            }
            return false
        }
    }
}

class RdcssSignature<E>(
    private val reference: Reference<E>, private val expected: E, private val update: Any?,
    private val outcomeSignature: Cas2Signature<E>
    ) : Signature<E> {
    val outComeStatus: AtomicRef<OutComeStatus> = atomic(OutComeStatus.UNKNOWN)

    override fun complete() {
        val currentStatus :OutComeStatus = if (outcomeSignature.outComeStatus.value == OutComeStatus.UNKNOWN) {
            OutComeStatus.COMPLETE
        } else {
            OutComeStatus.FAILED
        }
        outComeStatus.compareAndSet(OutComeStatus.UNKNOWN, currentStatus)
        val up: Any? = if (outComeStatus.value == OutComeStatus.COMPLETE) {
            update
        } else {
            expected
        }
        reference.temp.compareAndSet(this, up)
    }

    override fun dcss(reference: Reference<E>, expected: E, update: Any?, outcomeSignature: Cas2Signature<E>): Boolean {
        val sign = RdcssSignature(reference, expected, update, outcomeSignature)
        return if (reference.temp.value?.equals(update) == null) {
            false
        } else {
            reference.temp.value!!.equals(update) || if (reference.cAS(expected, sign)) {
                sign.complete()
                sign.outComeStatus.value == OutComeStatus.COMPLETE
            } else {
                false
            }
        }
    }
}

class Cas2Signature<E>(
    private val reference1: Reference<E>, private val expected1: E, private val update1: E,
    private val reference2: Reference<E>, private val expected2: E, private val update2: E
) : Signature<E> {
    val outComeStatus: AtomicRef<OutComeStatus> = atomic(OutComeStatus.UNKNOWN)

    override fun complete() {
        if (dcss(reference2, expected2, this, this)) {
            this.outComeStatus.compareAndSet(OutComeStatus.UNKNOWN, OutComeStatus.COMPLETE)
        } else {
            val outcome = if (reference2.temp.value != this) OutComeStatus.FAILED else OutComeStatus.COMPLETE
            this.outComeStatus.compareAndSet(OutComeStatus.UNKNOWN, outcome)
        }
        val res1: E
        val res2: E
        if (this.outComeStatus.value == OutComeStatus.FAILED) {
            res1 = expected1
            res2 = expected2
        } else {
            res1 = update1
            res2 = update2
        }
        reference1.temp.compareAndSet(this, res1)
        reference2.temp.compareAndSet(this, res2)
    }

    override fun dcss(reference: Reference<E>, expected: E, update: Any?, outcomeSignature: Cas2Signature<E>): Boolean {
        val sign = RdcssSignature(reference, expected, update, outcomeSignature)
        return if (reference.temp.value?.equals(update) == null) {
            false
        } else {
            reference.temp.value!!.equals(update) || if (reference.cAS(expected, sign)) {
                sign.complete()
                sign.outComeStatus.value == OutComeStatus.COMPLETE
            } else {
                false
            }
        }
    }
}





class AtomicArray<E>(size: Int, initialValue: E) {
    private val a = atomicArrayOfNulls<Reference<E>>(size)

    init {
        for (i in 0 until size) a[i].value = Reference(initialValue)
    }

    fun get(index: Int) :E =
        a[index].value!!.cell

    fun set(index: Int, value: E) {
        a[index].value?.cell = value
    }

    fun cas(index: Int, expected: E, update: E): Boolean =
        a[index].value?.cAS(expected, update) ?: false

    fun cas2(index1: Int, expected1: E, update1: E,
             index2: Int, expected2: E, update2: E): Boolean {
        // TODO this implementation is not linearizable,
        // TODO a multi-word CAS algorithm should be used here.
        if (index1 == index2) {
            return if (expected1 == expected2) {
                cas(index1, expected1, update2)
            } else {
                false
            }
        } else {
            val idx: Int
            val expected: E
            val signature: Cas2Signature<E>
            if (index1 > index2) {
                idx = index2
                expected = expected2
                signature = Cas2Signature(a[index2].value!!, expected2, update2, a[index1].value!!, expected1, update1)
            } else {
                idx = index1
                expected = expected1
                signature = Cas2Signature(a[index1].value!!, expected1, update1, a[index2].value!!, expected2, update2)
            }
            if (a[idx].value!!.cAS(expected, signature)) {
                signature.complete()
                return signature.outComeStatus.value == OutComeStatus.COMPLETE
            } else {
                return false
            }
        }
    }
}
