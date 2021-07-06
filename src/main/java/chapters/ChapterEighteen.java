package chapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChapterEighteen {
    public static void run() {
        factorialRecursive(10);
        factorialTailCall(0, 10);
    }

    public static List<List<Integer>> subsets(List<Integer> list) {
        if (list.isEmpty()) {
            return new ArrayList<>(Collections.emptyList());
        }
        Integer first = list.get(0);
        List<Integer> rest = list.subList(1, list.size());

        List<List<Integer>> subans = subsets(rest);
        List<List<Integer>> subans2 = insertAll(first, subans);

        // we use non pure version of concat function because list named subans will not be referenced after this function called.
        return concat(subans, subans2);
    }

    public static List<List<Integer>> insertAll(Integer first, List<List<Integer>> lists) {
        List<List<Integer>> res = new ArrayList<>();
        for (List<Integer> l : lists) {
            List<Integer> copyList = new ArrayList<>();
            copyList.add(first);
            copyList.addAll(l);
            res.add(copyList);
        }
        return res;
    }

    public static List<List<Integer>> concat(List<List<Integer>> a, List<List<Integer>> b) {
        // non pure version because function has a side-effect that function changes the list 'a'
        a.addAll(b);
        return a;
    }

    public static List<List<Integer>> pureConcat(List<List<Integer>>a, List<List<Integer>> b) {
        List<List<Integer>> res = new ArrayList<>();
        res.addAll(a);
        res.addAll(b);
        return res;
    }

    public static long factorialRecursive(long n) {
        System.out.println("factorialRecursive called, level : " + Thread.currentThread().getStackTrace().length);
        return n == 1 ? 1 : n * factorialRecursive(n-1);
    }

    public static long factorialTailCall(long acc, long n) {
        System.out.println("factorialTailCall called, level : " + Thread.currentThread().getStackTrace().length);
        return n == 1 ? acc : factorialTailCall(acc * n , n - 1);
    }
}
