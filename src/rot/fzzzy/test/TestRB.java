package rot.fzzzy.test;

import org.junit.Test;
import rot.fzzzy.tree.orderedtree.AVLTree;
import rot.fzzzy.tree.orderedtree.RBTree;

/**
 * ClassName:
 * Description:
 *
 * @author fzy
 * @create 2023-08-05-18:54
 */
public class TestRB {

    private static final int TIME = 10;

    @Test
    public void test() {
        for (int i = 0; i < TIME; i++) {
            int len = (int) (Math.random() * 200 + 200);
            int arr[] = new int[len];
            RBTree<Integer> rbTree = new RBTree<>();
            for (int j = 0; j < len; j++) {
                //建立RB树
                arr[j] = (int) (Math.random() * 500 + 1);
                rbTree.insert(arr[j]);
            }
            int circle = (int) (Math.random() * 200 + 100);

            for (int j = 0; j < circle; j++) {
                if (j % 10 == 0) {
                    rbTree.isRBTree(rbTree.root);
                    if (!rbTree.isSorted(null, rbTree.root)) {
                        throw new RuntimeException("傻逼排序代码出错辣！");
                    }
                    System.out.println("居然没错~");
                }

                int choice = (int) (Math.random() * 10);
                if (choice < 3) {
                    rbTree.remove((int) (Math.random() * 500 + 1));
                } else if(choice < 8){
                    rbTree.insert((int) (Math.random() * 500 + 1));
                }else{
                    rbTree.isRBTree(rbTree.root);
                    if (!rbTree.isSorted(null, rbTree.root)) {
                        throw new RuntimeException("傻逼排序代码出错辣！");
                    }
                }
            }

            System.out.println("-------------------------算你狗运过一次");
            rbTree.clear();
        }

        System.out.println("ok ，算你牛逼");
    }
}
