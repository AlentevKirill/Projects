import java.lang.ClassCastException
import java.util.concurrent.atomic.*

/**
 * @author Алентьев Кирилл
 *
 * TODO: Copy the code from `FAABasedQueueSimplified`
 * TODO: and implement the infinite array on a linked list
 * TODO: of fixed-size `Segment`s.
 */



class FAABasedQueue<E> : Queue<E> {
    private val head: AtomicReference<Segment>
    private val tail: AtomicReference<Segment>
    private val enqIdx = AtomicLong(0)
    private val deqIdx = AtomicLong(0)

    init {
        val dummy = Segment(0)
        head = AtomicReference(dummy)
        tail = AtomicReference(dummy)
    }

    override fun enqueue(element: E) {
        while (true) {
            var curTail = tail.get()
            if (curTail.next.get() != null) {
                tail.compareAndSet(curTail, curTail.next.get())
                continue
            }
            val i = enqIdx.getAndIncrement()
            val idx = i / SEGMENT_SIZE
            if (curTail.id > idx) {
                continue
            }
            var segment: Segment = curTail
            while (true) {
                segment = curTail
                val curId = curTail.id
                if (curId != idx) {
                    val newSeg = Segment(curId + 1)
                    if (curTail.next.compareAndSet(null, newSeg)) {
                        tail.compareAndSet(curTail, newSeg)
                        if (curId + 1 != idx) {
                            curTail = newSeg
                            continue
                        }
                        segment = newSeg
                        break
                    } else {
                        tail.compareAndSet(curTail, curTail.next.get())
                        curTail = curTail.next.get()
                    }
                } else {
                    break
                }
            }
            if (segment.cells.compareAndSet((i % SEGMENT_SIZE).toInt(), null, element)) {
                return
            }
        }
    }

    override fun dequeue(): E? {
        while (true) {
            if (!shouldTryToDequeue()) {
                return null
            }
            var curHead = head.get()
            val i = deqIdx.getAndIncrement()
            val idx = i / SEGMENT_SIZE
            //val segment = findAndDeleteSegment(curHead, i / SEGMENT_SIZE)
            var segment: Segment? = curHead
            while (true) {
                segment = curHead
                val curId = curHead.id
                if (curId != idx) {
                    if (curHead.next.get() == null) {
                        val newSeg = Segment(curId + 1)
                        //val curTail = tail.get()
                        if (curHead.next.compareAndSet(null, newSeg)) {
                        //if (curTail.next.compareAndSet(null, newSeg)) {
                            //tail.compareAndSet(curTail, newSeg)
                            //curHead.next.compareAndSet(null, newSeg)
                            head.compareAndSet(curHead, newSeg)
                            if (curId + 1 != idx) {
                                curHead = newSeg
                                continue
                            }
                            segment = newSeg
                            break
                        }
                    }
                    head.compareAndSet(curHead, curHead.next.get())
                    curHead = curHead.next.get()
                } else {
                    break
                }
            }
            if (segment!!.cells.compareAndSet((i % SEGMENT_SIZE).toInt(), null, POISONED) ||
                segment.cells.compareAndSet((i % SEGMENT_SIZE).toInt(), POISONED, POISONED)) {
                continue
            } else {
                return segment.cells.getAndSet((i % SEGMENT_SIZE).toInt(), POISONED) as E
            }
        }
    }

    private fun shouldTryToDequeue(): Boolean {
        while (true) {
            val curEnqIdx = enqIdx.get()
            val curDeqIdx = deqIdx.get()
            if (curEnqIdx != enqIdx.get() || curDeqIdx != deqIdx.get()) {
                continue
            }
            return curDeqIdx < curEnqIdx
        }
    }

    /*private fun findAndDeleteSegment(headSeg: Segment, idx : Long) : Segment? {
        if (headSeg.id == idx) {
            return headSeg
        } else {
            while (true) {
                if (head.compareAndSet(headSeg, null)) {
                    head.compareAndSet(headSeg, headSeg.next.get())
                    return headSeg.next.get()
                }
            }
        }
    }*/

    /*private fun findAndCreateSegment(tailSeg: Segment, idx : Long) : Segment {
        if (tailSeg.id == idx) {
            return tailSeg
        } else {
            val newSeg = Segment(idx)
            while (true) {
                if (tailSeg.next.compareAndSet(null, newSeg)) {
                    tail.compareAndSet(tailSeg, newSeg)
                    return newSeg
                } else {
                    tail.compareAndSet(tailSeg, tailSeg.next.get())
                }
            }
        }
    }*/
}



private class Segment(val id: Long) {
    val next = AtomicReference<Segment?>(null)
    val cells = AtomicReferenceArray<Any?>(SEGMENT_SIZE)
}

// DO NOT CHANGE THIS CONSTANT
private const val SEGMENT_SIZE = 2

private val POISONED = Any()