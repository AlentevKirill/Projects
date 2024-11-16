import java.util.concurrent.atomic.*

/**
 * @author Алентьев Кирилл
 */

class Solution(private val env: Environment) : Lock<Solution.Node> {
    private val tail = AtomicReference<Node>()

    override fun lock(): Node {
        val my = Node()
        my.isLocked.set(true)
        val pred = tail.getAndSet(my)
        if (pred != null) {
            pred.next.set(my)
            while (my.isLocked.get()){
                env.park()
            }
        }
        return my
    }

    override fun unlock(node: Node) {
        if (node.next.get() == null) {
            if (tail.compareAndSet(node, null)) {
                return
            } else {
                while (node.next.get() == null) {
                    continue
                }
            }
        }
        node.next.get().isLocked.set(false)
        env.unpark(node.next.get().thread)
    }

    class Node {
        val thread :Thread = Thread.currentThread()
        val next = AtomicReference<Node>()
        val isLocked = AtomicReference<Boolean>(false)
    }
}