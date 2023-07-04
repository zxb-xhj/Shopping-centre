package com.xhj.product;

import lombok.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * @Author: xhj
 * @Date: 2023/05/08/20:36
 * @Description:
 */
public class TestSet {

    public static void main(String[] args) {
        HashSet hashSet = new HashSet();
        hashSet.add(new Employee("jack",1000L,new Employee.MyDate("2023","5","8")));
        hashSet.add(new Employee("jack1",1000L,new Employee.MyDate("2023","5","8")));
        hashSet.add(new Employee("jack2",1000L,new Employee.MyDate("2023","5","8")));
        hashSet.add(null);
        System.out.println(hashSet);

        LinkedList linkedList = new LinkedList();
        linkedList.add(null);
        linkedList.add(null);
        System.out.println(linkedList);

        LinkedHashSet linkedHashSet = new LinkedHashSet();
        linkedHashSet.add(null);
        System.out.println(linkedHashSet);

    }

}

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
class Employee{
    private String name;
    private Long sal;
    private MyDate birthday;




    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class MyDate{
        private String year;
        private String month;
        private String day;
    }
}
