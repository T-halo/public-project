package cn.kgc.util;

import cn.kgc.util.impl.ArrayList;

import java.util.Comparator;

public class Arrays {
    public static final int SORT_PARAM_ALL_VALID = 0xfa021b;
    public static final int SORT_PARAM_ARRAY_ERROR = 0xfb022b;
    public static final int SORT_PARAM_CMP_ERROR = 0xfc023b;
    public static final int SORT_PARAM_BEGIN_ERROR = 0xfd024b;
    public static final int SORT_PARAM_END_ERROR = 0xfe025b;
    public static final int SORT_PARAM_EQUAL_ERROR = 0xff026b;
    public static final int SORT_PARAM_GREATER_ERROR = 0xff027b;

    //值交换
    private static void swap(Object[] array, int begin, int end) {
        Object t = array[begin];
        array[begin] = array[end];
        array[end] = t;
    }

    //有效性校验
    private static int validate(Object[] array, int begin, int end, Comparator cmp) {
        if (null == array || array.length <= 1) {
            return SORT_PARAM_ARRAY_ERROR;
        }
        if (null == cmp) {
            return SORT_PARAM_CMP_ERROR;
        }
        if (begin > end) {
            return SORT_PARAM_GREATER_ERROR;
        }
        if (begin < 0) {
            return SORT_PARAM_BEGIN_ERROR;
        }
        if (end > array.length - 1) {
            return SORT_PARAM_END_ERROR;
        }
        if (begin == end) {
            return SORT_PARAM_EQUAL_ERROR;
        }
        return SORT_PARAM_ALL_VALID;
    }

    private static boolean asc(boolean... ascend) {
        return ascend.length == 0 || ascend[0];
    }

    public static void selectSort(Object[] array, int begin, int end, Comparator cmp, boolean... ascend) {
        if (validate(array, begin, end, cmp) != SORT_PARAM_ALL_VALID) {
            return;
        }
        boolean asc = asc(ascend);
        int maxValIx, _begin, rst;
        while (begin < end) {
            maxValIx = _begin = begin;
            while (++_begin <= end) {
                rst = cmp.compare(array[_begin], array[maxValIx]);
                if (asc ? rst > 0 : rst < 0) {
                    maxValIx = _begin;
                }
            }
            if (maxValIx != end) {
                swap(array, maxValIx, end);
            }
            end--;
        }
    }

    private static int mid(Object[] array, int begin, int end, Comparator cmp, boolean asc) {
        int _begin = begin;
        while (begin < end) {
            while (begin < end && (asc ? cmp.compare(array[end], array[_begin]) >= 0
                    : cmp.compare(array[end], array[_begin]) <= 0)) end--;
            while (begin < end && (asc ? cmp.compare(array[begin], array[_begin]) <= 0
                    : cmp.compare(array[begin], array[_begin]) >= 0)) begin++;
            if (begin < end) {
                swap(array, begin, end);
            }
        }
        if (begin != _begin) {
            swap(array, _begin, begin);
        }
        return begin;
    }

    private static void split(Object[] array, int begin, int end, Comparator cmp, boolean asc) {
        if (begin >= end) {
            return;
        }
        int mid = mid(array, begin, end, cmp, asc);
        split(array, begin, mid - 1, cmp, asc);
        split(array, mid + 1, end, cmp, asc);
    }

    public static void quickSort(Object[] array, int begin, int end, Comparator cmp, boolean... ascend) {
        if (validate(array, begin, end, cmp) != SORT_PARAM_ALL_VALID) {
            return;
        }
        boolean _ascend = asc(ascend);
        split(array, begin, end, cmp, _ascend);
    }

    public static <T> List<T> asList(T... t) {
        List<T> list = new ArrayList<>(t.length);
        for (T _t : t) {
            list.add(_t);
        }
        return list;
    }
}
