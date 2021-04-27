package chapters;

import objects.Dish;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChapterEight {
    private static byte[] calculateDigest(String key) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        return messageDigest.digest(key.getBytes(StandardCharsets.UTF_8));
    }

    public static void run() {
        List<String> names = Arrays.asList("Nick", "Jade", "Olivia");
        // names.add("Haley"); // names is fixed length list - unsupported exception.
        List<String> names2 = List.of("Nick", "Jade", "Olivia");
        // names2.add("Haley"); // names is fixed length list - unsupported exception.

        Set<String> nameSet = Set.of("a", "b", "c");

        List<Dish> dishes = Dish.randomDishGenerator(10);


        // concurrent modification exception
//        for (Dish dish : dishes) {
//            if (dish.isVegetarian()) {
//                dishes.remove(dish);
//            }
//        }
//
//        System.out.println(dishes);

        // resolve above problem with removeIf
        dishes.removeIf(d -> d.isVegetarian());
        System.out.println(dishes);

        // change all of list items with iterator
        for (ListIterator<Dish> iterator = dishes.listIterator(); iterator.hasNext();) {
            Dish dish = iterator.next();
            iterator.set(new Dish("NEW",100,false, Dish.Type.FISH));
        }

        System.out.println(dishes);

        // change all of list items with replaceAll
        dishes.replaceAll(d -> new Dish("NEWNEW", 100, false, Dish.Type.FISH));

        System.out.println(dishes);

        Map<String, Integer> m1 =  Map.of("a", 3, "b", 2, "c",1);
        Map<String, List<Integer>> m2 = new HashMap<>();

        m2.put("a", new ArrayList<>());

        m1.forEach((s, i) -> System.out.println(String.format("%s : %d", s, i)));

        m1.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEachOrdered(System.out::println);
        m1.entrySet().stream().sorted(Map.Entry.comparingByValue())
                .forEachOrdered(System.out::println);

        System.out.println(m1.getOrDefault("d", -1));

        List<String> strs = List.of("awefawefawefawfewf", "fawefawefawefawefawef", "fawefawefawefawef");

        Map<String, byte[]> dataToHash = new HashMap<>();
        strs.forEach(s -> dataToHash.computeIfAbsent(s, x -> {
            try {
                return calculateDigest(x);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }));

        dataToHash.forEach((k, v) -> System.out.println(String.format("%s:%s", k, v.length)));
        m2.computeIfAbsent("c", k -> new ArrayList<>()).add(9);

        System.out.println(m2);

        // map replaceAll
        dataToHash.replaceAll((k, v) -> v = new byte[]{0x1});

        dataToHash.forEach((k, v) -> System.out.println(String.format("%s:%s", k, v.length)));

        Map<String, Integer> mergeTest1 = new HashMap<>();
        Map<String, Integer> mergeTest2 = new HashMap<>();
        mergeTest1.put("a", 1);
        mergeTest1.put("b", 2);
        mergeTest2.put("a", 3);

        mergeTest1.forEach((k, v) -> mergeTest2.merge(k, v, (v1, v2) -> v1 + v2));

        System.out.println(mergeTest2);

        // ConcurrentHashMap
        ConcurrentHashMap<String, Integer> cm = new ConcurrentHashMap();
        cm.put("a", 1);
        cm.keySet().forEach(k -> System.out.println(k));



    }
}
