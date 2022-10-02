package com.yil.adress.base;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortOrderConverter implements Converter<String[], List<Sort.Order>> {
    private final String[] avaiableNames;

    public SortOrderConverter(String[] avaiableNames) {
        this.avaiableNames = avaiableNames;
    }

    @Override
    public List<Sort.Order> convert(String[] source) throws UnsupportedOperationException, IllegalArgumentException {
        List<Sort.Order> orders = new ArrayList<>();
        if (source == null)
            return orders;
        for (String order : source) {
            String[] sort = order.split(":");
            if (sort.length < 1 || sort.length > 3)
                throw new UnsupportedOperationException("Sort syntax error!");
            if (Arrays.stream(avaiableNames).filter(f -> f.equals(sort[0])).count() == 0)
                throw new UnsupportedOperationException("Sort not avaiable!");
            if (sort.length == 2 && !Arrays.asList("ASC", "DESC").contains(sort[1].toUpperCase()))
                throw new IllegalArgumentException("Sort direction not avaiable!");
            if (sort.length == 3 && !Arrays.asList("NULLS_FIRST", "NULLS_LAST").contains(sort[2].toUpperCase()))
                throw new IllegalArgumentException("Sort null direction not avaiable!");
            Sort.NullHandling nullHandling = Sort.NullHandling.NATIVE;
            Sort.Direction direction = Sort.Direction.ASC;
            if (sort.length == 2)
                direction = Sort.Direction.fromString(sort[1]);
            if (sort.length == 3)
                nullHandling = Sort.NullHandling.valueOf(sort[2]);
            orders.add(new Sort.Order(direction, sort[0], nullHandling));
        }
        return orders;
    }
}