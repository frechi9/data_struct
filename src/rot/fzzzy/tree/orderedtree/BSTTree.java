package rot.fzzzy.tree.orderedtree;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * ClassName: BSTTree
 * Description: 二叉搜索树，只满足插入元素的排序性
 *
 * @author fzy
 * @create 2023-08-04-9:52
 */
public class BSTTree<E> {
    private BSTNode<E> root;
//    public BSTNode<E> root;
    protected Comparator<E> comparator;
    protected boolean comparableFlag;

    public BSTTree() {
        this.root = null;
        this.comparator = null;
    }

    public BSTTree(Comparator<E> comparator) {
        this.root = null;
        this.comparator = comparator;
    }

    public void insert(E data) {
        if (root == null) {
            root = new BSTNode<>(data);
            this.comparableFlag = isComparable();
        } else {
            BSTNode<E> parent = null, current = root;
            int compare = 0;
            while (current != null) {
                compare = compareTo(current.data, data);
                parent = current;
                if (compare > 0) {
                    current = current.left;
                } else if (compare < 0) {
                    current = current.right;
                } else {
                    return;//can not insert same value
                }
            }

            compare = compareTo(parent.data, data);
            if (compare > 0) {
                parent.left = new BSTNode<>(data);
            } else {
                parent.right = new BSTNode<>(data);
            }
        }
    }

    public void remove(E data) {
        BSTNode<E> node = search(data);
        if (node == null) return;
        int degree0 = degree(node);

        if (degree0 == 2) {//删除的节点度为2则要他的后继节点来代替它，后继节点的度必然为0或者1
            node = next(node);
        }

        BSTNode<E> parent = parent(node);
        BSTNode<E> child = node.left == null ? node.right : node.left;

        //不管度为0还是1，他们的操作都是让parent从原来指向node改为指向node的某一个孩子（包括null）
        if(parent == null){
            root = child;
        }else if(parent.left == node){
            parent.left = child;
        }else{
            parent.right = child;
        }
    }

    public boolean ifExist(E data) {
        return search(data) == null ? false : true;
    }

    private BSTNode<E> parent(BSTNode<E> node) {
        BSTNode<E> parent = null, current = root;
        while (current != node) {
            int compare = compareTo(current.data, node.data);
            if (compare == 0) return parent;
            parent = current;
            if (compare > 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return parent;
    }

    private BSTNode<E> next(BSTNode<E> node) {
        BSTNode<E> next = node.right;
        while (next.left != null) {
            next = next.left;
        }
        return next;
    }

    private BSTNode<E> search(E data) {
        BSTNode<E> current = root;
        while (current != null) {
            int compare = compareTo(current.data, data);
            if (compare > 0) {
                current = current.left;
            } else if (compare < 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    private int degree(BSTNode<E> node) {
        if (node.left != null && node.right != null) return 2;
        else if (node.left == null && node.right == null) return 0;
        else return 1;
    }

    protected int compareTo(E e1, E e2) {
        if (comparator != null) return comparator.compare(e1, e2);
        if (comparableFlag == true) {
            return ((Comparable<E>) e1).compareTo(e2);
        }
        throw new RuntimeException("can not convert incomparable to comparable");
    }

    protected boolean isComparable() {
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

    private static class BSTNode<E> {
        E data;
        BSTNode<E> left, right;

        BSTNode(E data) {
            this.data = data;
            this.left = this.right = null;
        }

        BSTNode(E data, BSTNode<E> left, BSTNode<E> right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }

    public void level_print() {
        if (root == null) return;
        Queue<BSTNode<E>> queue = new LinkedList<>();
        queue.offer(root);
        int size = 1;
        while (!queue.isEmpty()) {
            var poll = queue.poll();
            size--;

            if (poll.left != null) {
                System.out.print("(");
                System.out.print(poll.left.data + ")");
                queue.offer(poll.left);
            }

            System.out.print(poll.data);

            if (poll.right != null) {
                System.out.print("(");
                System.out.print(poll.right.data + ")");
                queue.offer(poll.right);
            }

            System.out.print("\t");
            if (size == 0) {
                size = queue.size();
                System.out.println();
            }
        }
    }

    public void isSorted(BSTNode<E> prev, BSTNode<E> root, boolean flag) {
        if (root == null) return;
        isSorted(prev, root.left, flag);

        if(prev == null){
            prev = root;
        }else if(compareTo(root.data , prev.data) < 0){
            flag = false;
        }

        isSorted(root, root.right, flag);
    }
}
