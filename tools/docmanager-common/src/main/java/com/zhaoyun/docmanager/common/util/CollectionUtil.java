package com.zhaoyun.docmanager.common.util;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class CollectionUtil {
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static void copy(List<String> source, List<Long> target) {
        if (source == null || target == null) {
            return;
        }
        for (String str : source) {
            if (!NumberUtils.isNumber(str)) {
                throw new RuntimeException("params error");
            }
            target.add(Long.parseLong(str));

        }
    }

    /**
     * 从集合中剔除满足fiter条件的元素
     *
     * @param collection 集合
     * @param filter     满足的条件
     * @param <T>        集合里面的泛型
     * @author: xiafei
     */
    public static <T> void filter(Collection<T> collection, Filter<T> filter) {

        for (Iterator<T> iter = collection.iterator(); iter.hasNext(); ) {
            T t = iter.next();
            if (filter.isRemove(t)) {
                iter.remove();
            }
        }
    }
}

interface Filter<T> {
    public boolean isRemove(T t);
}
