package ca.umanitoba.cs.zaheerd.tests;

public class TestResults {

    private int totalTests = 0;
    private int successes = 0;
    private int failures = 0;

    public int totalTests() { return totalTests; }
    public int successes()  { return successes; }
    public int failures()   { return failures; }

    public void pass(String msg) {
        System.out.println("  PASS: " + msg);
        successes++;
        totalTests++;
    }

    public void fail(String msg) {
        System.out.println("  FAIL: " + msg);
        failures++;
        totalTests++;
    }
}
