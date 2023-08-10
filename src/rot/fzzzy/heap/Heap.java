package rot.fzzzy.heap;

import java.util.Comparator;

/**
 * ClassName:
 * Description:
 *
 * @author fzy
 * @create 2023-08-05-19:42
 */
public class Heap<E> {

    private Object[] elems;
    private int size;
    private Comparator<E> comparator;
    private boolean isComparable;

    public Heap() {
    }

    public Heap(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public void offer(E data){

    }
}
