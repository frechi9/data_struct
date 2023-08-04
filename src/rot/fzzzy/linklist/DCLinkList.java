package rot.fzzzy.linklist;

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

    public void removeLast() {
        ListNode<E> last = head.prev;
        if (last == head) return;
        remove(last);
        size--;
    }

    public void removeFront() {
        ListNode<E> first = head.next;
        if (first == head) return;
        remove(first);
        size--;
    }

    //打印链表
    public void print_list(){
        ListNode<E> node = head.next;
        while(node != head){
            System.out.print(node.data + "\t");
            node = node.next;
        }
        System.out.println();
    }

    private void add(ListNode<E> node, ListNode<E> prev, ListNode<E> next) {
        prev.next = node;
        next.prev = node;
        node.prev = prev;
        node.next = next;
    }

    private void remove(ListNode<E> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
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