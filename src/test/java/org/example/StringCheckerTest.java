package org.example;

import org.example.exceptions.NegativeNumbersNotAllowed;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


class StringCheckerTest {
    StringChecker stringChecker = new StringChecker();
    @BeforeEach
    void beforeEach(){
        stringChecker = new StringChecker();
    }
    @Nested
    @DisplayName("PREDICATES")
    class PredicateTests {
        @DisplayName("Number smaller than 1000 gets filtered")
        @Test
        void predicateIsSmallerThan1000Works(){
            var predicateToTest = stringChecker.getIsSmallerThan1000();
            assertThat(predicateToTest.test(1020)).isFalse();
            assertThat(predicateToTest.test(999)).isTrue();
        }
        @DisplayName("Character - gets filtered")
        @Test
        void predicateIsNotMinusSign(){
            var predicateToTest = stringChecker.getIsNotMinusSign();
            assertThat(predicateToTest.test('|')).isTrue();
            assertThat(predicateToTest.test('-')).isFalse();
        }
        @DisplayName("Blank characters get filtered")
        @Test
        void predicateIsNotBlank(){
            var predicateToTest = stringChecker.getIsNotBlank();
            assertThat(predicateToTest.test('|')).isTrue();
            assertThat(predicateToTest.test(' ')).isFalse();
        }
        @DisplayName("Only numbers pass- filter works")
        @Test
        void predicateOnlyNumbersPass(){
            var predicateToTest = stringChecker.getOnlyNumbersPass();
            assertThat(predicateToTest.test("1")).isTrue();
            assertThat(predicateToTest.test("5")).isTrue();
            assertThat(predicateToTest.test(" ")).isFalse();
            assertThat(predicateToTest.test("&")).isFalse();
            assertThat(predicateToTest.test("|")).isFalse();
        }
        @DisplayName("Character is not a digit-filter works")
        @Test
        void predicateIsNotADigit(){
            var predicateToTest = stringChecker.getIsNotADigit();
            assertThat(predicateToTest.test(' ')).isTrue();
            assertThat(predicateToTest.test('&')).isTrue();
            assertThat(predicateToTest.test('1')).isFalse();
        }
    }
    @Nested
    @DisplayName("LIMITER METHOD")
    class LimiterMethod {
        @DisplayName("Limiter is determined correct")
        @Test
        void limiterGetsCalculatedCorrect(){
            String limiterOne = "(1(2(3";
            String limiterTwo = "|1|2|3";
            String limiterThree = " )1)2)3";
            String limiterFour = " 1,2,3";
            String limiterFive = " &1&2&3";

            assertThat(stringChecker.getLimiter(limiterOne).get()).isEqualTo('(');
            assertThat(stringChecker.getLimiter(limiterTwo).get()).isEqualTo('|');
            assertThat(stringChecker.getLimiter(limiterThree).get()).isEqualTo(')');
            assertThat(stringChecker.getLimiter(limiterFour).get()).isEqualTo(',');
            assertThat(stringChecker.getLimiter(limiterFive).get()).isEqualTo('&');
        }
        @DisplayName("Minus sign - is not taken as delimiter")
        @ParameterizedTest
        @ValueSource(strings={ "-1,2,3", " -1,2,3", "-1|2|3", " -1|2|3"})
        void whenStringStartsWithMinusItIsNotTakenAsDelimiter(String startMinusString){
            assertThat(stringChecker.getLimiter(startMinusString).get()).isNotEqualTo('-');
        }
    }

    @Nested
    @DisplayName("GETSUM METHOD")
    class GetSumMethod {
        @DisplayName("No input returns 0")
        @Test
        void stringIsNullReturnsZero() {
            assertThat(stringChecker.getSum()).isEqualTo(0);
        }
        @DisplayName("Empty string returns 0")
        @Test
        void stringIsEmptyReturnsZero() {
            assertThat(stringChecker.getSum("")).isEqualTo(0);
        }
        @DisplayName("Negative numbers throw a NoNegativeNumberException")
        @ParameterizedTest
        @ValueSource(strings={ "-1,2,3", " -1,2,3", "-1|2|3", " -1|2|3", "1,-2,3", "-1|2|3", "1,2,-3"})
        void negativeNumberThrowsException(String negNrString){
            assertThatExceptionOfType(NegativeNumbersNotAllowed.class).isThrownBy(() -> stringChecker.getSum(negNrString));
        }
        @DisplayName("NoNegativeNumberException doesn't appear irregular")
        @ParameterizedTest
        @ValueSource(strings={ "1,0,3", "|1|2|3", "1|2|3", " 0|2|3"})
        void noNegativeNumberThrowsNoException(String goodNrs){
            assertDoesNotThrow(() -> stringChecker.getSum(goodNrs));
        }

        @DisplayName("Numbers greater that 1000 are not counted")
        @Test
        void numbersGreaterThan1000AreNotCounted(){
            String with1000One = " 1,1001,3";
            String with1000two = "|1145|2|3";
            String with1000three ="1,2,3000";

            assertThat(stringChecker.getSum(with1000One)).isEqualTo(4);
            assertThat(stringChecker.getSum(with1000three)).isEqualTo(3);
            assertThat(stringChecker.getSum(with1000two)).isEqualTo(5);
        }

        @DisplayName("Valid string is calculated correct")
        @ParameterizedTest
        @ValueSource(strings={ "1,2,3", "|1|2|3", " 1,2,3", " 1|2|3", "&1&2&3", "1&2&3" , " 1&2&3", " &1&2&3"
                , "(1(2(3", "1(2(3" , " 1)2)3", " )1)2)3"})
        void validStringIsCalculatedCorrect(String validString){
            assertThat(stringChecker.getSum(validString)).isEqualTo(6);
        }
        @DisplayName("Spaces in string do not make code fail")
        @ParameterizedTest
        @ValueSource(strings={ " 1,2,3", " 1, 2,3", " 1,2,3", " 1|2|3", " 1|2|  3"})
        void spacesInStringAreNotProblematic(String spacesString){
            assertThat(stringChecker.getSum(spacesString)).isEqualTo(6);
        }
    }
}