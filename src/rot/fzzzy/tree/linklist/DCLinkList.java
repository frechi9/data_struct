package rot.fzzzy.tree.linklist;

/**
 * ClassName:DCLinkList
 * Description: 双向环链
 *
 * @author fzy
 * @create 2023-08-04-14:16
 */
public class DCLinkList<E> {

    private ListNode head;
    private int size;

    public DCLinkList() {
        this.head = new ListNode(null);
        this.size = 0;
        this.head.prev = this.head.next = this.head;
    }

    public void addFront(E data) {
        ListNode<E> node = new ListNode<>(data);
        add(node, head, head.next);
        size++;
    }

    public void addLast(E data) {
        ListNode<E> node = new ListNode<>(data);
        add(node, head.prev, head);
        size++;
    }

    private void add(ListNode<E> node, ListNode<E> prev, ListNode<E> next) {
        prev.next = node;
        next.prev = node;
        node.prev = prev;
        node.next = next;
    }

    public void removeLast(){

        size--;
    }


    public void removeFront(){
        remove(head , head.next);
        size--;
    }

    private void remove(ListNode<E> prev , ListNode<E> node){
        if(prev == node) return;
        prev.next = node.next;
        node.next.prev = prev;

        //也可以不用指空，java有自动回收机制
        node.prev = node.next = null;
    }


    private static class ListNode<E> {
        E data;
        ListNode<E> prev, next;

        ListNode(E data) {
            this.data = data;
            this.prev = this.next = null;
        }

        public ListNode(E data, ListNode<E> prev, ListNode<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}
