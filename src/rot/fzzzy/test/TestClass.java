package rot.fzzzy.test;

import org.junit.Test;
import rot.fzzzy.linklist.DCLinkList;

/**
 * ClassName: TestClass
 * Description:
 *
 * @author fzy
 * @create 2023-08-04-10:11
 */
public class TestClass {

    private final static int SIZE = 100;
    private final static int TIME = 100;


//    @Test
//    public void test1() {
//        BSTTree<Integer> tree = new BSTTree<>();
//        int arr[] = new int[SIZE];
//
//        for (int i = 0; i < TIME; i++) {
//            boolean flag = true;
//            for (int j = 0; j < SIZE; j++) {
//                arr[j] = (int) (Math.random() * 100 + 1);
//                tree.insert(arr[j]);
//            }
//            tree.isSorted(null, tree.root, flag);
//            if (!flag) {
//                System.out.println("排序失败");
//                throw new RuntimeException("失败");
//            }
//        }
//
//        System.out.println("排序成功");
//    }

    @Test
    public void test2() {
        new AAA<Integer>(null);

    }

    @Test
    public void test3(){
        DCLinkList<String> list = new DCLinkList<>();
        list.addFront("11");
        list.addFront("22");
        list.addLast("33");
        list.addLast("44");
        list.print_list();


        list.removeFront();
        list.print_list();

        list.removeLast();
        list.print_list();

        list.removeLast();
        list.print_list();

        list.removeFront();
        list.print_list();
    }


    public static class AAA<E> {
        E data;

        AAA(E data) {
            this.data = data;
        }
    }
}
