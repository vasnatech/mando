package com.vasnatech.mando.expression.function;

import com.vasnatech.commons.collection.Iterables;
import com.vasnatech.commons.collection.Iterators;
import com.vasnatech.commons.type.tuple.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface CollectionFunctions {

    static Set<?> setOf(Object... objects) {
        return new LinkedHashSet<>(Set.of(objects));
    }

    static List<?> listOf(Object... objects) {
        return new ArrayList<>(List.of(objects));
    }

    static Queue<?> queueOf(Object... objects) {
        Queue<Object> queue = new LinkedList<>();
        Stream.of(objects).forEach(queue::offer);
        return queue;
    }

    static Deque<?> dequeOf(Object... objects) {
        Deque<Object> deque = new LinkedList<>();
        Stream.of(objects).forEach(deque::push);
        return deque;
    }

    static Map<?, ?> mapOf(Map.Entry<?, ?>... entries) {
        return new LinkedHashMap<>(Stream.of(entries).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    static Single<?> singleOf(Object first) {
        return Single.mutable(first);
    }

    static Pair<?, ?> pairOf(Object first, Object second) {
        return Pair.mutable(first, second);
    }

    static Triple<?, ?, ?> tripleOf(Object first, Object second, Object third) {
        return Triple.mutable(first, second, third);
    }

    static Quadruple<?, ?, ?, ?> quadrupleOf(Object first, Object second, Object third, Object fourth) {
        return Quadruple.mutable(first, second, third, fourth);
    }

    static Quintuple<?, ?, ?, ?, ?> quintupleOf(Object first, Object second, Object third, Object fourth, Object fifth) {
        return Quintuple.mutable(first, second, third, fourth, fifth);
    }

    static Tuple tupleOf(Object first, Object second, Object third, Object fourth, Object fifth, Object sixth, Object... elements) {
        return Tuple.mutable(first, second, third, fourth, fifth, sixth, elements);
    }

    static Object first(Object obj) {
        if (obj instanceof LinkedList<?> list)
            return list.isEmpty() ? null : list.getFirst();
        if (obj instanceof List<?> list)
            return list.isEmpty() ? null : list.get(0);
        if (obj instanceof SortedSet<?> set)
            return set.isEmpty() ? null : set.first();
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            return length == 0 ? null : Array.get(obj, 0);
        }
        if (obj instanceof Tuple tuple)
            return tuple.head();
        if (obj instanceof Iterable<?> iterable)
            return Iterables.first(iterable);
        if (obj instanceof Iterator<?> iterator)
            return Iterators.first(iterator);
        if (obj instanceof Map<?, ?> map)
            return first(map.entrySet());
        return null;
    }

    static Object last(Object obj) {
        if (obj instanceof LinkedList<?> list)
            return list.isEmpty() ? null : list.getLast();
        if (obj instanceof List<?> list)
            return list.isEmpty() ? null : list.get(list.size() - 1);
        if (obj instanceof SortedSet<?> set)
            return set.isEmpty() ? null : set.last();
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            return length == 0 ? null : Array.get(obj, length - 1);
        }
        if (obj instanceof Tuple tuple)
            return tuple.tail();
        if (obj instanceof Iterable<?> iterable)
            return Iterables.last(iterable);
        if (obj instanceof Iterator<?> iterator)
            return Iterators.last(iterator);
        if (obj instanceof Map<?, ?> map)
            return last(map.entrySet());
        return null;
    }
}
