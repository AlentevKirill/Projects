import kotlinx.atomicfu.*

/**
 * @author Алентьев Кирилл
 */
/**
 * Atomic block.
 */
fun <T> atomic(block: TxScope.() -> T): T {
    while (true) {
        val transaction = Transaction()
        try {
            val result = block(transaction)
            if (transaction.commit()) return result
            transaction.abort()
        } catch (e: AbortException) {
            transaction.abort()
        }
    }
}

/**
 * Transactional operations are performed in this scope.
 */
abstract class TxScope {
    abstract fun <T> TxVar<T>.read(): T
    abstract fun <T> TxVar<T>.write(x: T): T
}

/**
 * Transactional variable.
 */
class TxVar<T>(initial: T)  {
    private val loc = atomic(Loc(initial, initial, rootTx))

    /**
     * Opens this transactional variable in the specified transaction [tx] and applies
     * updating function [update] to it. Returns the updated value.
     */
    fun openIn(tx: Transaction, update: (T) -> T): T {
        // todo: FIXME: this implementation does not actually implement transactional update
        while (true) {
            val curLoc = loc.value
            val curValue = curLoc.valueIn(tx) {
                it.abort()
            }
            if (curValue === TxStatus.ACTIVE) {
                continue
            }
            val updValue = update(curValue as T)
            if (loc.compareAndSet(curLoc, Loc(curValue, updValue, tx))) {
                if (tx.status == TxStatus.ABORTED){
                    throw AbortException
                }
                return updValue
            }
        }
    }
}

/**
 * State of transactional value
 */
private class Loc<T>(
    val oldValue: T,
    val newValue: T,
    val owner: Transaction
) {
    fun valueIn(tx: Transaction, onActive: (Transaction) -> Unit): Any? {
        if (tx == owner) {
            return newValue
        } else {
            val curOwnerStatus = owner.status
            if (curOwnerStatus === TxStatus.ABORTED) {
                return oldValue
            }
            if (curOwnerStatus === TxStatus.COMMITTED) {
                return newValue
            }
            if (curOwnerStatus === TxStatus.ACTIVE) {
                onActive(owner)
                return TxStatus.ACTIVE
            }
            /*return when (owner.status) {
                TxStatus.ABORTED -> oldValue
                TxStatus.COMMITTED -> newValue
                TxStatus.ACTIVE -> {
                    onActive(owner)
                    TxStatus.ACTIVE
                }
            }*/
        }
        return null
    }

    /*fun valueIn(tx: Transaction, onActive: (Transaction) -> Unit): Any? {
        return when (tx) {
            owner -> newValue
            else -> when (owner.status) {
                TxStatus.ABORTED -> oldValue
                TxStatus.COMMITTED -> newValue
                TxStatus.ACTIVE -> {
                    onActive(owner)
                    TxStatus.ACTIVE
                }
            }
        }
    }*/

}

private val rootTx = Transaction().apply { commit() }

/**
 * Transaction status.
 */
enum class TxStatus { ACTIVE, COMMITTED, ABORTED }

/**
 * Transaction implementation.
 */
class Transaction : TxScope() {
    private val _status = atomic(TxStatus.ACTIVE)
    val status: TxStatus get() = _status.value

    fun commit(): Boolean =
        _status.compareAndSet(TxStatus.ACTIVE, TxStatus.COMMITTED)

    fun abort() {
        _status.compareAndSet(TxStatus.ACTIVE, TxStatus.ABORTED)
    }

    override fun <T> TxVar<T>.read(): T = openIn(this@Transaction) { it }
    override fun <T> TxVar<T>.write(x: T) = openIn(this@Transaction) { x }
}

/**
 * This exception is thrown when transaction is aborted.
 */
private object AbortException : Exception() {
    override fun fillInStackTrace(): Throwable = this
}