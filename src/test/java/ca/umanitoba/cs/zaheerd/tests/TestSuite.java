package ca.umanitoba.cs.zaheerd.tests;

public interface TestSuite {
    String name();
    TestResults runTests();
}
