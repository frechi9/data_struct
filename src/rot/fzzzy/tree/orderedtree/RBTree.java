package rot.fzzzy.tree.orderedtree;

import java.util.Comparator;

/**
 * ClassName: RBTree
 * Description: 简略版本的红黑树实现
 *
 * @author fzy
 * @create 2023-08-04-9:51
 */
public class RBTree<E>{

    private static final int black = 1;
    private static final int red = 0;


    //todo 0 代表红色、1代表黑色

    public RBNode<E> root;
//    private RBTree<E> root;
    private boolean comparableFlag;
    private Comparator<E> comparator;

    public RBTree() {
        this.root = null;
        this.comparator = null;
    }

    public RBTree(Comparator<E> comparator) {
        this.root = null;
        this.comparator = comparator;
    }

    void insert(E data){
        if (root == null) {//0 红色 / 1 黑色
            root = new RBNode<>(data);
            root.color = black;
            this.comparableFlag = isComparable();
        } else {
            RBNode<E> parent = null, current = root;
            int compare = 0;
            while (current != null) {
                parent = current;
                compare = compareTo(current.data, data);

                if (compare > 0) {
                    current = current.left;
                } else if (compare < 0) {
                    current = current.right;
                } else {
                    return;
                }
            }

            compare = compareTo(parent.data, data);
            RBNode<E> newNode = new RBNode<>(data, parent);
            if (compare > 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }

            insert_justify(newNode);
        }
    }

    private void insert_justify(RBNode<E> node){//0 红色 / 1 黑色
        RBNode<E> parent = node.parent;
        if(parent == null){
            node.color = black;
            return;
        }

        int parentColor = parent.color;
        if(node.color != parentColor) return;

        RBNode<E> gp = parent.parent;
        RBNode<E> uncle = gp.left == parent ? gp.right : gp.left;
        int uncleColor = uncle == null ? black : uncle.color;

        if(uncleColor == red){
            uncle.color = parent.color = black;
            gp.color = red;
            insert_justify(gp);
        }else{


        }

    }


    private void rotationLL(RBNode<E> parent) {//LL
        RBNode<E> node = parent.left;
        RBNode<E> gp = parent.parent;

        node.parent = gp;
        if (gp == null) {
            root = node;
        } else if (gp.left == parent) {
            gp.left = node;
        } else {
            gp.right = node;
        }

        parent.left = node.right;
        if (node.right != null) node.right.parent = parent;

        node.right = parent;
        parent.parent = node;
    }

    private void rotationRR(RBNode<E> parent) {//RR
        RBNode<E> node = parent.right;
        RBNode<E> gp = parent.parent;

        node.parent = gp;
        if (gp == null) {
            root = node;
        } else if (gp.left == parent) {
            gp.left = node;
        } else {
            gp.right = node;
        }

        parent.right = node.left;
        if (node.left != null) node.left.parent = parent;

        node.left = parent;
        parent.parent = node;

    }

    private void rotationLR(RBNode<E> parent) {//LR
        rotationRR(parent.left);
        rotationLL(parent);
    }

    private void rotationRL(RBNode<E> parent) {//RL
        rotationLL(parent.right);
        rotationRR(parent);
    }

    private int compareTo(E e1, E e2) {
        if (comparator != null) return comparator.compare(e1, e2);
        if (comparableFlag == true) {
            return ((Comparable<E>) e1).compareTo(e2);
        }
        throw new RuntimeException("can not convert incomparable to comparable");
    }

    private boolean isComparable() {
        if (root == null) return false;
        if (comparator == null) {
            Class<?> clazz = root.data.getClass();
            String ComparableName = Comparable.class.getName();
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class Interface : interfaces) {
                String interfaceName = Interface.getName();
                if (ComparableName.equals(interfaceName)) return true;
            }
        }
        return false;
    }

    //fixme
    public void clear() {
        root = null;
    }

    //fixme
    public boolean isSorted(RBNode<E> pre, RBNode<E> node) {
        if (node == null) return true;
        boolean lb = isSorted(pre, node.left);

        boolean flag = true;
        if (pre == null) pre = node;
        else flag = compareTo(node.data, pre.data) >= 0 ? true : false;

        boolean rb = isSorted(node, node.right);
        return flag && lb && rb;
    }

    private static class RBNode<E>{
        E data;
        int color;
        RBNode<E> parent ,left ,right;

        public RBNode(E data) {
            this.data = data;
            this.color = 0;
            this.parent = this.left = this.right = null;
        }

        public RBNode(E data, RBNode<E> parent) {
            this.data = data;
            this.color = 0;
            this.parent = parent;
            this.left = this.right = null;
        }
    }

}
