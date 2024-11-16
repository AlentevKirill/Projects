import java.util.concurrent.atomic.*

/**
 * @author Алентьев Кирилл
 */
class MSQueue<E> : Queue<E> {
    private val head: AtomicReference<Node<E>>
    private val tail: AtomicReference<Node<E>>

    init {
        val dummy = Node<E>(null)
        head = AtomicReference(dummy)
        tail = AtomicReference(dummy)
    }

    override fun enqueue(element: E) {
        val node = Node(element)
        while (true) {
            val curTail = tail.get()
            val nextTail = curTail.next
            if (nextTail.get() != null) {
                tail.compareAndSet(curTail, nextTail.get())
                continue
            }
            if (nextTail.compareAndSet(null, node)) {
                tail.compareAndSet(curTail, node)
                break
            } else {
                tail.compareAndSet(curTail, nextTail.get())
            }
        }
    }

    override fun dequeue(): E? {
        while (true) {
            val curHead = head.get()
            val curTail = tail.get()
            val curHeadNext = curHead.next.get()
            if (curHead == curTail) {
                if (curHeadNext == null) {
                    return null
                }
                tail.compareAndSet(curTail, curHeadNext)
            } else {
                if (head.compareAndSet(curHead, curHeadNext)) {
                    val result = curHeadNext?.element
                    curHeadNext?.element = null
                    return result
                }
            }
        }
    }

    // FOR TEST PURPOSE, DO NOT CHANGE IT.
    override fun validate() {
        check(tail.get().next.get() == null) {
            "At the end of the execution, `tail.next` must be `null`"
        }
        check(head.get().element == null) {
            "At the end of the execution, the dummy node shouldn't store an element"
        }
    }

    private class Node<E>(var element: E?) {
        val next = AtomicReference<Node<E>?>(null)
    }
}