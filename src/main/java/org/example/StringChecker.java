package org.example;

import org.example.exceptions.NegativeNumbersNotAllowed;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public class StringChecker {
    private Predicate<Integer> isSmallerThan1000 = i -> i <=1000;
    private Predicate<Character> isNotMinusSign = c -> !c.toString().equals("-");
    private Predicate<Character> isNotBlank = c -> !c.toString().equals(" ");
    private Predicate<String> onlyNumbersPass = s -> s.matches("-?\\d+([.]\\d+)?");
    private Predicate<Character> isNotADigit = c -> !(Character.isDigit(c));
    public int getSum(String input){
        int result = 0;
        if(!input.isEmpty() && !(input == null)){
            String[] numbers = input.split("\\" + getLimiter(input).get());
            result = Arrays.stream(numbers)
                    .map(string -> string.trim())
                    .filter(onlyNumbersPass)
                    .map(string -> Integer.parseInt(string))
                    .filter(isSmallerThan1000)
                    .filter(i -> checkNegative(i))
                    .mapToInt(i -> (int) i)
                    .sum();
        }
        return result;
    }
    public int getSum(){
        return 0;
    }
    public Optional<Character> getLimiter(String input){
        return input.chars()
                .mapToObj(c -> (char) c)
                .filter(isNotBlank)
                .filter(isNotADigit)
                .filter(isNotMinusSign)
                .findFirst();
    }
    private boolean checkNegative(int i){
        if(i < 0 ) throw new NegativeNumbersNotAllowed("negatieve cijfers zijn niet toegestaan");
        return true;
    }
    public Predicate<Integer> getIsSmallerThan1000() {
        return isSmallerThan1000;
    }
    public Predicate<Character> getIsNotMinusSign() {
        return isNotMinusSign;
    }
    public Predicate<Character> getIsNotBlank() {
        return isNotBlank;
    }
    public Predicate<String> getOnlyNumbersPass() {
        return onlyNumbersPass;
    }
    public Predicate<Character> getIsNotADigit() {
        return isNotADigit;
    }
}
