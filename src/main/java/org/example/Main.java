package org.example;

public class Main {
    public static void main(String[] args) {
        StringChecker stringchecker = new StringChecker();
        System.out.println("example 1");
        System.out.println(stringchecker.getSum("1,2,3"));
        System.out.println("example 2");
        System.out.println(stringchecker.getSum("|1|2|5"));
        System.out.println("example 3");
        System.out.println(stringchecker.getSum("|1|2|5"));
        System.out.println("example 4");
        System.out.println(stringchecker.getSum(""));
        System.out.println("example 5");
        System.out.println(stringchecker.getSum());
        System.out.println("example 6:");
        System.out.println(stringchecker.getSum(" &1&2&3"));
    }
}