/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author eiker
 */
public final class FindUtils {
    public static <T> T findByProperty(Collection<T> col, Predicate<T> filter) {
        return col.stream().filter(filter).findFirst().orElse(null);
    }
    
    public static <T> List<T> findAllByProperty(Collection<T> col, Predicate<T> filter) {
           List<T> filteredList = (List<T>) col.stream().filter(filter).collect(Collectors.toList());
           return filteredList;
    }
}
