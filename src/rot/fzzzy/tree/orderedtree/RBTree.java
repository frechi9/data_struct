package rot.fzzzy.tree.orderedtree;

/**
 * ClassName: RBTree
 * Description: 简略版本的红黑树实现
 *
 * @author fzy
 * @create 2023-08-04-9:51
 */
public class RBTree<E> extends BSTTree<E> {

    private RBTree<E> root;

    public RBTree() {
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
