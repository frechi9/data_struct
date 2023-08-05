package rot.fzzzy.test;

import org.junit.Test;
import rot.fzzzy.tree.orderedtree.AVLTree;

/**
 * ClassName: TestAVL
 * Description: 测试AVL树的建立、新增、删除操作
 *
 * @author fzy
 * @create 2023-08-05-16:26
 */
public class TestAVL {

    private static final int TIME = 20;

    @Test
    public void test() {
        for (int i = 0; i < TIME; i++) {
            int len = (int) (Math.random() * 200 + 20);
            int arr[] = new int[len];
            AVLTree<Integer> avlTree = new AVLTree<>();
            for (int j = 0; j < len; j++) {
                //建立AVL树
                arr[j] = (int) (Math.random() * 500 + 1);
                avlTree.insert(arr[j]);
            }
            int circle = (int) (Math.random() * 200 + 100);

            for (int j = 0; j < circle; j++) {
                if (j % 10 == 0) {
                    avlTree.isBalanced(avlTree.root);
                    if (!avlTree.isSorted(null, avlTree.root)) {
                        throw new RuntimeException("傻逼排序代码出错辣！");
                    }
                    System.out.println("我来过一次");
                }

                int choice = (int) (Math.random() * 10);
                if (choice < 3) {
                    avlTree.remove((int) (Math.random() * 300 + 1));
                } else if(choice < 8){
                    avlTree.insert((int) (Math.random() * 500 + 1));
                }else{
                    avlTree.isBalanced(avlTree.root);
                    if (!avlTree.isSorted(null, avlTree.root)) {
                        throw new RuntimeException("傻逼排序代码出错辣！");
                    }
                }
            }

            avlTree.clear();
        }

        System.out.println("ok ，算你牛逼");
    }


}
