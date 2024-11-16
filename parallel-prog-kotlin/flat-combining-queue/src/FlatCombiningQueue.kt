import java.util.concurrent.*
import java.util.concurrent.atomic.*

/**
 * @author Алентьев Кирилл
 */

class FlatCombiningQueue<E> : Queue<E> {
    private val queue = ArrayDeque<E>() // sequential queue
    private val combinerLock = AtomicBoolean(false) // unlocked initially
    private val tasksForCombiner = AtomicReferenceArray<Any?>(TASKS_FOR_COMBINER_SIZE)

    private fun tryLock() :Boolean {
        return combinerLock.compareAndSet(false, true)
    }

    private fun unLock() {
        combinerLock.set(false)
    }

    @Suppress("UNCHECKED_CAST")
    override fun enqueue(element: E) {
        // TODO: Make this code thread-safe using the flat-combining technique.
        // TODO: 1.  Try to become a combiner by
        // TODO:     changing `combinerLock` from `false` (unlocked) to `true` (locked).
        // TODO: 2a. On success, apply this operation and help others by traversing
        // TODO:     `tasksForCombiner`, performing the announced operations, and
        // TODO:      updating the corresponding cells to `Result`.
        // TODO: 2b. If the lock is already acquired, announce this operation in
        // TODO:     `tasksForCombiner` by replacing a random cell state from
        // TODO:      `null` with the element. Wait until either the cell state
        // TODO:      updates to `Result` (do not forget to clean it in this case),
        // TODO:      or `combinerLock` becomes available to acquire.
        var success = false
        val index = randomCellIndex()
        while (true) {
            if (tryLock()) {
                try {
                    if (!success) {
                        queue.addLast(element)
                    }
                    for (i in 0 until TASKS_FOR_COMBINER_SIZE) {
                        val cell = tasksForCombiner.get(i)
                        if (cell != null && cell !is Result<*>) {
                            if (cell == Dequeue) {
                                tasksForCombiner.compareAndSet(i, cell, Result<E>(queue.removeFirstOrNull() as E))
                            } else {
                                tasksForCombiner.compareAndSet(i, cell, Result(null))
                                queue.addLast(cell as E)
                            }
                        }
                    }
                } finally {
                    unLock()
                    break
                }
            } else {
                if (!success) {
                    if (tasksForCombiner.compareAndSet(index, null, element)) {
                        success = true
                        continue
                    }
                } else {
                    if (tasksForCombiner.get(index) is Result<*>) {
                        tasksForCombiner.set(index, null)
                        break
                    }
                }
            }
        }
    }

    override fun dequeue(): E? {
        // TODO: Make this code thread-safe using the flat-combining technique.
        // TODO: 1.  Try to become a combiner by
        // TODO:     changing `combinerLock` from `false` (unlocked) to `true` (locked).
        // TODO: 2a. On success, apply this operation and help others by traversing
        // TODO:     `tasksForCombiner`, performing the announced operations, and
        // TODO:      updating the corresponding cells to `Result`.
        // TODO: 2b. If the lock is already acquired, announce this operation in
        // TODO:     `tasksForCombiner` by replacing a random cell state from
        // TODO:      `null` with `Dequeue`. Wait until either the cell state
        // TODO:      updates to `Result` (do not forget to clean it in this case),
        // TODO:      or `combinerLock` becomes available to acquire.
        var success = false
        val index = randomCellIndex()
        var saveResult :E? = null
        while (true) {
            if (tryLock()) {
                try {
                    if (!success) {
                        saveResult = queue.removeFirstOrNull()
                    }
                    for (i in 0 until TASKS_FOR_COMBINER_SIZE) {
                        val cell = tasksForCombiner.get(i)
                        if (cell != null && cell !is Result<*>) {
                            if (cell == Dequeue) {
                                tasksForCombiner.compareAndSet(i, cell, Result<E>(queue.removeFirstOrNull() as E))
                            } else {
                                tasksForCombiner.compareAndSet(i, cell, Result(null))
                                queue.addLast(cell as E)
                            }
                        }
                    }
                } finally {
                    unLock()
                    break
                }
            } else {
                if (!success) {
                    if (tasksForCombiner.compareAndSet(index, null, Dequeue)) {
                        success = true
                        continue
                    }
                } else {
                    val temp = tasksForCombiner.get(index)
                    if (temp is Result<*>) {
                        tasksForCombiner.set(index, null)
                        return temp.value as E
                    }
                }
            }
        }
        if (!success) {
            return saveResult
        } else {
            val temp = tasksForCombiner.get(index)
            if (temp is Result<*>) {
                tasksForCombiner.set(index, null)
                return temp.value as E
            }
        }
        return null
    }

    private fun randomCellIndex(): Int =
        ThreadLocalRandom.current().nextInt(tasksForCombiner.length())
}

private const val TASKS_FOR_COMBINER_SIZE = 3 // Do not change this constant!

// TODO: Put this token in `tasksForCombiner` for dequeue().
// TODO: enqueue()-s should put the inserting element.
private object Dequeue

// TODO: Put the result wrapped with `Result` when the operation in `tasksForCombiner` is processed.
private class Result<V>(
    val value: V
)