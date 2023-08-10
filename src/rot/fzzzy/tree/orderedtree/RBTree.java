package rot.fzzzy.tree.orderedtree;

import java.util.Base64;
import java.util.Comparator;

/**
 * ClassName: RBTree
 * Description: 简略版本的红黑树实现
 *
 * @author fzy
 * @create 2023-08-04-9:51
 */
public class RBTree<E> {

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

    public void insert(E data) {
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

    private void insert_justify(RBNode<E> node) {//0 红色 / 1 黑色
        RBNode<E> parent = node.parent;
        if (parent == null) {
            node.color = black;
            return;
        }

        int parentColor = parent.color;
        if (node.color != parentColor) return;

        RBNode<E> gp = parent.parent;
        RBNode<E> uncle = gp.left == parent ? gp.right : gp.left;
        int uncleColor = uncle == null ? black : uncle.color;

        if (uncleColor == red) {
            uncle.color = parent.color = black;
            gp.color = red;
            insert_justify(gp);
        } else {
            gp.color = red;
            if (gp.left == parent) {//L
                if (parent.left == node) {//LL
                    parent.color = black;
                    rotationLL(gp);
                } else {//LR
                    node.color = black;
                    rotationLR(gp);
                }
            } else {//R
                if (parent.right == node) {//RR
                    parent.color = black;
                    rotationRR(gp);
                } else {//RL
                    node.color = black;
                    rotationRL(gp);
                }
            }
        }
    }

    public void remove(E data) {
        RBNode<E> node = search(data);
        if (node == null) return;
        int degree0 = degree(node);

        if (degree0 == 2) {
            RBNode<E> minMax = next(node);
            node.data = minMax.data;
            node = minMax;
        }

        RBNode<E> parent = node.parent;
        RBNode<E> child = node.left == null ? node.right : node.left;
        int ncolor = node.color, ccolor = child == null ? black : child.color;

        if ((ncolor ^ ccolor) == 1) {//删除节点与孩子节点一黑一红
            delete(parent, node, child);
            if (child != null) child.color = black;
        } else {//删除节点与孩子节点双黑
            if (parent == null) {
                delete(parent, node, child);
            } else {
                RBNode<E> sibling = parent.left == node ? parent.right : parent.left;
                delete(parent, node, child);

                //??? why nullptr
                if (sibling.color == black) {//这边分支相当于双黑，要想保证红黑树的黑高一致，显然一定存在一个兄弟节点

                    RBNode<E> lcousin = sibling.left, rcousin = sibling.right;
                    int lcousinColor = lcousin == null ? black : lcousin.color;
                    int rcousinColor = rcousin == null ? black : rcousin.color;

                    if (lcousinColor == red || rcousinColor == red) {//兄弟节点的左子节点为红色，如果左右都为红色也可以
                        if (sibling == parent.left) {
                            if (lcousinColor == red) {

                                if (parent.color == black) lcousin.color = black;
                                rotationLL(parent);
                            } else {

                                if (parent.color == black) rcousin.color = black;
                                else parent.color = black;
                                rotationLR(parent);
                            }
                        } else {
                            if (rcousinColor == red) {

                                if (parent.color == black) rcousin.color = black;
                                rotationRR(parent);
                            } else {

                                if (parent.color == black) lcousin.color = black;
                                else parent.color = black;
                                rotationRL(parent);
                            }
                        }
                    } else {//兄弟节点的子节点为黑色

                        handle_recur(parent, sibling);
                    }
                } else {
                    parent.color = red;
                    sibling.color = black;

                    if (sibling == parent.left) {
                        rotationLL(parent);
                    } else {
                        rotationRR(parent);
                    }

                    sibling = parent.left == child ? parent.right : parent.left;
                    handle_recur(parent, sibling);
                }
            }
        }
    }

    private void delete(RBNode<E> parent, RBNode<E> node, RBNode<E> child) {//只负责删除
        if (parent == null) {
            root = child;
        } else if (parent.left == node) {
            parent.left = child;
        } else {
            parent.right = child;
        }

        if (child != null) {
            child.parent = parent;
        }
        node.parent = node.left = node.right = null;
    }

    private void handle_recur(RBNode<E> parent, RBNode<E> node) {
        RBNode<E> np = null, nn = null;
        while (parent != null) {
            np = parent.parent;
            if (np != null) nn = np.left == parent ? np.right : np.left;

            node.color = red;
            if (parent.color == red) {
                parent.color = black;
                return;
            }

            parent = np;
            node = nn;
        }
    }

    public boolean ifExist(E data) {
        return search(data) == null ? false : true;
    }

    private RBNode<E> search(E data) {
        RBNode<E> current = root;
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

    private RBNode<E> next(RBNode<E> node) {
        RBNode<E> next = node.right;
        while (next.left != null) {
            next = next.left;
        }
        return next;
    }

    private int degree(RBNode<E> node) {
        if (node.left != null && node.right != null) return 2;
        else if (node.left == null && node.right == null) return 0;
        else return 1;
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

    //fixme
    public int isRBTree(RBNode<E> node) {
        if (node == null) return 0;
        int lbh = isRBTree(node.left);
        int rbh = isRBTree(node.right);

        if (lbh != rbh) throw new RuntimeException("傻逼代码颜色出错了");

        return lbh + node.color == black ? 1 : 0;
    }

    private static class RBNode<E> {
        E data;
        int color;
        RBNode<E> parent, left, right;

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

/*

    public void remove(E data) {
        RBNode<E> node = search(data);
        if (node == null) return;
        int degree0 = degree(node);

        if (degree0 == 2) {
            RBNode<E> minMax = next(node);
            node.data = minMax.data;
            node = minMax;
        }

        RBNode<E> parent = node.parent;
        RBNode<E> child = node.left == null ? node.right : node.left;
        int ncolor = node.color, ccolor = child == null ? black : child.color;

        if ((ncolor ^ ccolor) == 1) {//删除节点与孩子节点一黑一红
            delete(parent, node, child);
            if (child != null) child.color = black;
        } else {//删除节点与孩子节点双黑
            if (parent == null) {
                delete(parent, node, child);
            } else {
                RBNode<E> sibling = parent.left == node ? parent.right : parent.left;
                delete(parent, node, child);

                if (sibling.color == black) {//这边分支相当于双黑，要想保证红黑树的黑高一致，显然一定存在一个兄弟节点

                    RBNode<E> lcousin = sibling.left, rcousin = sibling.right;
                    int lcousinColor = lcousin == null ? black : lcousin.color;
                    int rcousinColor = rcousin == null ? black : rcousin.color;

                    if (lcousinColor == red) {//兄弟节点的左子节点为红色，如果左右都为红色也可以
                        if (parent.left == sibling) {

                            lcousin.color = sibling.color;
                            sibling.color = parent.color;
                            parent.color = black;
                            rotationLL(parent);

                        } else {

                            lcousin.color = parent.color;
                            parent.color = black;
                            rotationRL(parent);
                        }
                    } else if (rcousinColor == red) {//兄弟节点的右子节点为红色，显然能来到这里证明至少左子节点不能为红色
                        if (parent.right == sibling) {

                            rcousin.color = sibling.color;
                            sibling.color = parent.color;
                            parent.color = black;
                            rotationRR(parent);

                        } else {

                            rcousin.color = parent.color;
                            parent.color = black;
                            rotationLR(parent);
                        }
                    } else {//兄弟节点的子节点为黑色（包括null）

                        handle_recur(parent, sibling);
                    }
                }
                else {
                    parent.color = red;
                    sibling.color = black;

                    if (sibling == parent.left) {
                        rotationLL(parent);
                    } else {
                        rotationRR(parent);
                    }

                    sibling = parent.left == child ? parent.right : parent.left;
                    handle_recur(parent, sibling);
                }
            }
        }
    }


* */