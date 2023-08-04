package rot.fzzzy.tree.orderedtree;

/**
 * ClassName: AVLTree
 * Description:
 *
 * @author fzy
 * @create 2023-08-04-9:52
 */
public class AVLTree<E> extends BSTTree<E> {

    //    public AVLNode<E> root;
    private AVLNode<E> root;

    public void insert(E data) {
        if (root == null) {
            root = new AVLNode<E>(data);
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

    public void remove(E data) {
        AVLNode<E> node = search(data);
        if (node == null) return;
        int degree0 = degree(node);

        if (degree0 == 2) {
            node = next(node);
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

        //------------------------------------------------
//        remove_justify(parent);
    }


    public void remove_justify(AVLNode<E> parent, AVLNode<E> node) {
        if (parent.left == null && parent.right == null) {
            parent.bf = 0;
            node = parent;
            parent = parent.parent;
        }

        while (parent != null) {
            if (parent.left == node) {
                parent.bf--;
            } else {
                parent.bf++;
            }

            //todo 旋转后的bf还没有修改
            if (Math.abs(parent.bf) == 1) break;
            if (parent.bf == 2) {
                if (node.bf == 1) {
                    rotationRight(parent);
                } else {
                    rotationLeftThenRight(parent);
                }
            } else if (parent.bf == -2) {
                if(node.bf == -1){
                    rotationLeft(parent);
                }else{
                    rotationRightThenLeft(parent);
                }
            } else {
                node = parent;
                parent = parent.parent;
            }
        }
    }

    public void insert_justify(AVLNode<E> node) {
        AVLNode<E> parent = node.parent;

        while (parent != null) {
            if (parent.left == node) parent.bf++;
            else parent.bf--;

            if (parent.bf == 0) break;
            if (parent.bf == 2) {
                if (node.bf == 1) {//LL
                    rotationRight(parent);
                } else {//LR
                    rotationLeftThenRight(parent);
                }
            } else if (parent.bf == -2) {
                if (node.bf == -1) {//RR
                    rotationLeft(parent);
                } else {//RL
                    rotationRightThenLeft(parent);
                }
            } else {//bf == 1
                node = parent;
                parent = parent.parent;
            }
        }
    }

    public int isBalanced(AVLNode<E> node, boolean flag) {
        if (node == null) return 0;
        int hl = isBalanced(node.left, flag);
        int hr = isBalanced(node.right, flag);

        if (Math.abs(hl - hr) > 1) flag = false;
        return Math.max(hl, hr) + 1;
    }

    public boolean isSorted(AVLNode<E> pre, AVLNode<E> node) {
        if (node == null) return true;
        boolean lb = isSorted(pre, node.left);

        boolean flag = true;
        if (pre == null) pre = node;
        else flag = compareTo(node.data, pre.data) >= 0 ? true : false;

        boolean rb = isSorted(node, node.right);
        return flag && lb && rb;
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

    /**
     * parent节点向右旋转
     *
     * @param parent
     */
    private void rotationRight(AVLNode<E> parent) {//LL
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

        node.left = parent;
        parent.parent = node;
    }

    /**
     * parent节点向左旋转
     *
     * @param parent
     */
    private void rotationLeft(AVLNode<E> parent) {//RR
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

    /**
     * parent.left节点先向左旋转
     * parent节点再向右旋转
     *
     * @param parent
     */
    private void rotationLeftThenRight(AVLNode<E> parent) {//LR
        rotationLeft(parent.left);
        rotationRight(parent);
    }

    /**
     * parent.right节点先向右旋转
     * parent节点再向左旋转
     *
     * @param parent
     */
    private void rotationRightThenLeft(AVLNode<E> parent) {//RL
        rotationLeft(parent.left);
        rotationRight(parent);
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
