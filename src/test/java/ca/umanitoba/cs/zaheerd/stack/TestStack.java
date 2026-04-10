package ca.umanitoba.cs.zaheerd.tests.stack;

import ca.umanitoba.cs.zaheerd.stack.LinkedStack;
import ca.umanitoba.cs.zaheerd.tests.TestResults;
import ca.umanitoba.cs.zaheerd.tests.TestSuite;
import ca.umanitoba.cs.comp2450.stack.Stack;
import ca.umanitoba.cs.comp2450.stack.impl.*;

public class TestStack implements TestSuite {

    private final TestResults results = new TestResults();

    @Override
    public String name() {
        return "Stack tests (LinkedStack + BadStack1-5)";
    }

    @Override
    public TestResults runTests() {
        System.out.println("    --- LinkedStack ---");
        runAllTests(new LinkedStack<>(), "LinkedStack");

        System.out.println("    --- BadStack1 ---");
        runAllTests(new BadStack1<>(), "BadStack1");

        System.out.println("    --- BadStack2 ---");
        runAllTests(new BadStack2<>(), "BadStack2");

        System.out.println("    --- BadStack3 ---");
        runAllTests(new BadStack3<>(), "BadStack3");

        System.out.println("    --- BadStack4 ---");
        runAllTests(new BadStack4<>(), "BadStack4");

        System.out.println("    --- BadStack5 ---");
        runAllTests(new BadStack5<>(), "BadStack5");

        return results;
    }


    private void runAllTests(Stack<String> stackname, String name) {
        testNewStackIsEmpty(stackname, name);
        testPushIncreasesSize(implementation(name), name);
        testPeekReturnsTopWithoutRemoving(implementation(name), name);
        testPopReturnsTopAndDecreasesSize(implementation(name), name);
        testLIFOOrder(implementation(name), name);
        testSizeAfterMultiplePushes(implementation(name), name);
        testIsEmptyAfterPoppingAll(implementation(name), name);
        testPopOnSingleElement(implementation(name), name);
    }

    private void testNewStackIsEmpty(Stack<String> stack, String stackname) {
        try {
            if (!stack.isEmpty()) {
                results.fail("[" + stackname + "] testNewStackIsEmpty: new stack should be empty");
            } else if (stack.size() != 0) {
                results.fail("[" + stackname + "] testNewStackIsEmpty: new stack size should be 0, got " + stack.size());
            } else {
                results.pass("[" + stackname + "] testNewStackIsEmpty: new stack is empty with size 0");
            }
        } catch (Exception e) {
            results.fail("[" + stackname + "] testNewStackIsEmpty: unexpected exception: " + e.getMessage());
        }
    }


    private void testPushIncreasesSize(Stack<String> stack, String stackname) {
        try {
            stack.push("hello");

            if (stack.size() != 1) {
                results.fail("[" + stackname + "] testPushIncreasesSize: size should be 1 after one push, got " + stack.size());
            } else if (stack.isEmpty()) {
                results.fail("[" + stackname + "] testPushIncreasesSize: stack should not be empty after push");
            } else {
                results.pass("[" + stackname + "] testPushIncreasesSize: size is 1 and isEmpty is false after push");
            }
        } catch (Exception e) {
            results.fail("[" + stackname + "] testPushIncreasesSize: unexpected exception: " + e.getMessage());
        }
    }

    private void testPeekReturnsTopWithoutRemoving(Stack<String> stack, String stackname) {
        try {
            stack.push("alpha");
            String peeked = stack.peek();
            int sizeAfterPeek = stack.size();

            if (!"alpha".equals(peeked)) {
                results.fail("[" + stackname + "] testPeekReturnsTopWithoutRemoving: peek returned '" + peeked + "', expected 'alpha'");
            } else if (sizeAfterPeek != 1) {
                results.fail("[" + stackname + "] testPeekReturnsTopWithoutRemoving: size changed after peek — should still be 1, got " + sizeAfterPeek);
            } else {
                results.pass("[" + stackname + "] testPeekReturnsTopWithoutRemoving: peek returns top and leaves size unchanged");
            }
        } catch (Exception e) {
            results.fail("[" + stackname + "] testPeekReturnsTopWithoutRemoving: unexpected exception: " + e.getMessage());
        }
    }

    private void testPopReturnsTopAndDecreasesSize(Stack<String> stack, String stackname) {
        try {
            stack.push("first");
            stack.push("second");
            String popped = stack.pop();

            if (!"second".equals(popped)) {
                results.fail("[" + stackname + "] testPopReturnsTopAndDecreasesSize: pop returned '" + popped + "', expected 'second'");
            } else if (stack.size() != 1) {
                results.fail("[" + stackname + "] testPopReturnsTopAndDecreasesSize: size should be 1 after pop, got " + stack.size());
            } else {
                results.pass("[" + stackname + "] testPopReturnsTopAndDecreasesSize: pop returns top and decrements size");
            }
        } catch (Exception e) {
            results.fail("[" + stackname + "] testPopReturnsTopAndDecreasesSize: unexpected exception: " + e.getMessage());
        }
    }

    private void testLIFOOrder(Stack<String> stack, String stackname) {
        try {
            stack.push("A");
            stack.push("B");
            stack.push("C");

            String third = stack.pop();
            String second = stack.pop();
            String first  = stack.pop();

            if (!"C".equals(third) || !"B".equals(second) || !"A".equals(first)) {
                results.fail("[" + stackname + "] testLIFOOrder: expected C, B, A but got "
                        + third + ", " + second + ", " + first);
            } else {
                results.pass("[" + stackname + "] testLIFOOrder: elements returned in correct LIFO order");
            }
        } catch (Exception e) {
            results.fail("[" + stackname + "] testLIFOOrder: unexpected exception: " + e.getMessage());
        }
    }

    private void testSizeAfterMultiplePushes(Stack<String> stack, String stackname) {
        try {
            stack.push("one");
            stack.push("two");
            stack.push("three");
            stack.push("four");
            stack.push("five");

            if (stack.size() != 5) {
                results.fail("[" + stackname + "] testSizeAfterMultiplePushes: expected size 5, got " + stack.size());
            } else {
                results.pass("[" + stackname + "] testSizeAfterMultiplePushes: size correctly tracks 5 pushes");
            }
        } catch (Exception e) {
            results.fail("[" + stackname + "] testSizeAfterMultiplePushes: unexpected exception: " + e.getMessage());
        }
    }

    private void testIsEmptyAfterPoppingAll(Stack<String> stack, String stackname) {
        try {
            stack.push("x");
            stack.push("y");
            stack.pop();
            stack.pop();

            if (!stack.isEmpty()) {
                results.fail("[" + stackname + "] testIsEmptyAfterPoppingAll: stack should be empty after popping all elements");
            } else if (stack.size() != 0) {
                results.fail("[" + stackname + "] testIsEmptyAfterPoppingAll: size should be 0, got " + stack.size());
            } else {
                results.pass("[" + stackname + "] testIsEmptyAfterPoppingAll: stack correctly empty after all pops");
            }
        } catch (Exception e) {
            results.fail("[" + stackname + "] testIsEmptyAfterPoppingAll: unexpected exception: " + e.getMessage());
        }
    }

    private void testPopOnSingleElement(Stack<String> stack, String stackname) {
        try {
            stack.push("only");
            String popped = stack.pop();

            if (!"only".equals(popped)) {
                results.fail("[" + stackname + "] testPopOnSingleElement: expected 'only', got '" + popped + "'");
            } else if (!stack.isEmpty()) {
                results.fail("[" + stackname + "] testPopOnSingleElement: stack should be empty after sole pop");
            } else if (stack.size() != 0) {
                results.fail("[" + stackname + "] testPopOnSingleElement: size should be 0, got " + stack.size());
            } else {
                results.pass("[" + stackname + "] testPopOnSingleElement: stack correctly empty after sole element popped");
            }
        } catch (Exception e) {
            results.fail("[" + stackname + "] testPopOnSingleElement: unexpected exception: " + e.getMessage());
        }
    }


    private Stack<String> implementation(String implName) {
        switch (implName) {
            case "BadStack1": return new BadStack1<>();
            case "BadStack2": return new BadStack2<>();
            case "BadStack3": return new BadStack3<>();
            case "BadStack4": return new BadStack4<>();
            case "BadStack5": return new BadStack5<>();
            default:          return new LinkedStack<>();
        }
    }
}