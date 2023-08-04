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
            if (compare > 0) {
                parent.left = new AVLNode<>(data , parent , null,null);
            } else {
                parent.right = new AVLNode<>(data , parent , null,null);
            }



        }
    }

    public void remove(E data) {


    }

    public void modify(E data) {


    }


}
