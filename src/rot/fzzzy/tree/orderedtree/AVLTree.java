package rot.fzzzy.tree.orderedtree;

import java.util.Comparator;

/**
 * ClassName: AVLTree
 * Description:
 * <p>
 * @author fzy
 * @create 2023-08-04-9:52
 */

public class AVLTree<E> {

    //    private AVLNode<E> root;
    public AVLNode<E> root;
    private boolean comparableFlag;
    private Comparator<E> comparator;

    public AVLTree() {
        this.root = null;
        this.comparator = null;
    }

    public AVLTree(Comparator<E> comparator) {
        this.root = null;
        this.comparator = comparator;
    }

    public void insert(E data) {
        if (root == null) {
            root = new AVLNode<E>(data);
            this.comparableFlag = isComparable();
        } else {
            AVLNode<E> parent = null, current = root;
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
            AVLNode<E> newNode = new AVLNode<>(data, parent, null, null);
            if (compare > 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }

            insert_justify(newNode);
        }
    }

    private void insert_justify(AVLNode<E> node) {
        AVLNode<E> parent = node.parent;

        while (parent != null) {
            if (parent.left == node) parent.bf++;
            else parent.bf--;

            if (parent.bf == 0) return;
            if (parent.bf == 2) {
                if (node.bf == 1) {//LL
                    changeBfLL(parent, node);
                    rotationLL(parent);
                } else {//LR
                    changeBfLR(parent, node, node.right);
                    rotationLR(parent);
                }
                return;
            } else if (parent.bf == -2) {
                if (node.bf == -1) {//RR
                    changeBfRR(parent, node);
                    rotationRR(parent);
                } else {//RL
                    changeBfRL(parent, node, node.left);
                    rotationRL(parent);
                }
                return;
            } else {//bf == 1
                node = parent;
                parent = parent.parent;
            }
        }
    }

    public void remove(E data) {
        AVLNode<E> node = search(data);
        if (node == null) return;
        int degree0 = degree(node);

        if (degree0 == 2) {
            AVLNode<E> minMax = next(node);
            node.data = minMax.data;
            node = minMax;
        }

        AVLNode<E> parent = node.parent;
        AVLNode<E> child = node.left == null ? node.right : node.left;

        //不管度为0还是1，他们的操作都是让parent从原来指向node改为指向node的某一个孩子（包括null）
        if (parent == null) {
            root = child;
        } else if (parent.left == node) {
            parent.left = child;
        } else {
            parent.right = child;
        }
        if (child != null) child.parent = parent;

        if (parent != null && parent.left == null && parent.right == null) {//因为后续是通过对比左右指针确定孩子位置的
            //只有在第一次判断时可能出现左右指针均为空，此时直接设置parent的bf为0即可
            parent.bf = 0;
            remove_justify(parent.parent, parent);
        } else {
            remove_justify(parent, child);
        }
    }

    private void remove_justify(AVLNode<E> parent, AVLNode<E> node) {
        while (parent != null) {
            if (parent.left == node) {
                parent.bf--;
            } else {
                parent.bf++;
            }
            //等于1代表当前以parent为根的子树高度没有发生变化，自此往上也就不存在失衡，推出调整循环
            if (Math.abs(parent.bf) == 1) return;

            AVLNode nextParent = parent.parent, nextNode = parent;
            //等于2代表删除后向上调整时出现了失衡，需要按照插入时的调整方式进行调整
            if (parent.bf == 2) {
                AVLNode<E> ubfNode = parent.left;
                if (ubfNode.bf == 0) {
                    parent.bf = 1;
                    ubfNode.bf = -1;
                    rotationLL(parent);
                    return;//非常有意思的一点
                } else if (ubfNode.bf == 1) {
                    nextNode = ubfNode;
                    changeBfLL(parent, ubfNode);
                    rotationLL(parent);
                } else {// -1
                    nextNode = ubfNode.right;
                    changeBfLR(parent, ubfNode, ubfNode.right);
                    rotationLR(parent);
                }
            } else if (parent.bf == -2) {
                AVLNode<E> ubfNode = parent.right;
                if (ubfNode.bf == 0) {
                    parent.bf = -1;
                    ubfNode.bf = 1;
                    rotationRR(parent);
                    return;//同上
                } else if (ubfNode.bf == -1) {
                    nextNode = ubfNode;
                    changeBfRR(parent, ubfNode);
                    rotationRR(parent);
                } else {
                    nextNode = ubfNode.left;
                    changeBfRL(parent, ubfNode, ubfNode.left);
                    rotationRL(parent);
                }
            }

            parent = nextParent;
            node = nextNode;
        }
    }

    private AVLNode<E> next(AVLNode<E> node) {
        node = node.right;
        while (node.left != null) node = node.left;
        return node;
    }

    private int degree(AVLNode<E> node) {
        if (node == null) throw new RuntimeException("illegal operation");
        if (node.left != null && node.right != null) return 2;
        else if (node.left == null && node.right == null) return 0;
        else return 1;
    }

    private AVLNode<E> search(E data) {
        AVLNode<E> node = root;

        while (node != null) {
            int compare = compareTo(node.data, data);
            if (compare == 0) return node;
            else if (compare > 0) node = node.left;
            else node = node.right;
        }
        return null;
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

    /**
     * parent节点向右旋转
     *
     * @param parent
     */
    private void rotationLL(AVLNode<E> parent) {//LL
        AVLNode<E> node = parent.left;
        AVLNode<E> gp = parent.parent;

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

    private void changeBfLL(AVLNode<E> parent, AVLNode<E> node) {//LL
        parent.bf = node.bf = 0;
    }

    /**
     * parent节点向左旋转
     *
     * @param parent
     */
    private void rotationRR(AVLNode<E> parent) {//RR
        AVLNode<E> node = parent.right;
        AVLNode<E> gp = parent.parent;

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

    private void changeBfRR(AVLNode<E> parent, AVLNode<E> node) {//RR
        parent.bf = node.bf = 0;
    }

    /**
     * parent.left节点先向左旋转
     * parent节点再向右旋转
     *
     * @param parent
     */
    private void rotationLR(AVLNode<E> parent) {//LR
        rotationRR(parent.left);
        rotationLL(parent);
    }

    private void changeBfLR(AVLNode<E> parent, AVLNode<E> node, AVLNode<E> rchild) {//LR
        if (rchild.bf == 0) {//由于rchild的产生导致的失衡
            parent.bf = 0;
            node.bf = 0;
        } else if (rchild.bf == 1) {//由于rchild的左子树高度增加导致的失衡
            parent.bf = -1;
            node.bf = 0;
        } else {//由于rchild的右子树高度增加导致的失衡
            parent.bf = 0;
            node.bf = 1;
        }
        rchild.bf = 0;
    }

    /**
     * parent.right节点先向右旋转
     * parent节点再向左旋转
     *
     * @param parent
     */
    private void rotationRL(AVLNode<E> parent) {//RL
        rotationLL(parent.right);
        rotationRR(parent);
    }

    private void changeBfRL(AVLNode<E> parent, AVLNode<E> node, AVLNode<E> lchild) {//RL
        if (lchild.bf == 0) {
            parent.bf = 0;
            node.bf = 0;
        } else if (lchild.bf == 1) {
            parent.bf = 0;
            node.bf = -1;
        } else {
            parent.bf = 1;
            node.bf = 0;
        }
        lchild.bf = 0;
    }

    //fixme
    public void clear() {
        root = null;
    }

    //fixme
    public int isBalanced(AVLNode<E> node) {
        if (node == null) return 0;
        int hl = isBalanced(node.left);
        int hr = isBalanced(node.right);

        if (Math.abs(hl - hr) >= 2) throw new RuntimeException("傻逼代码失衡辣");
        return Math.max(hl, hr) + 1;
    }

    //fixme
    public boolean isSorted(AVLNode<E> pre, AVLNode<E> node) {
        if (node == null) return true;
        boolean lb = isSorted(pre, node.left);

        boolean flag = true;
        if (pre == null) pre = node;
        else flag = compareTo(node.data, pre.data) >= 0 ? true : false;

        boolean rb = isSorted(node, node.right);
        return flag && lb && rb;
    }

    private static class AVLNode<E> {
        E data;
        int bf;
        AVLNode<E> parent, left, right;

        AVLNode(E data) {
            this.data = data;
            this.bf = 0;
            this.parent = this.left = this.right = null;
        }

        AVLNode(E data, AVLNode<E> parent, AVLNode<E> left, AVLNode<E> right) {
            this.data = data;
            this.bf = 0;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }
    }
}
