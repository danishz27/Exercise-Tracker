package ca.umanitoba.cs.zaheerd.tests;

import ca.umanitoba.cs.zaheerd.tests.logic.TestPathFinder;
import ca.umanitoba.cs.zaheerd.tests.model.TestExercise;
import ca.umanitoba.cs.zaheerd.tests.model.TestPath;
import ca.umanitoba.cs.zaheerd.tests.model.TestUser;
import ca.umanitoba.cs.zaheerd.tests.stack.TestStack;

public class TestHarness {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  EXERCISE TRACKER — TEST HARNESS");
        System.out.println("========================================");

        TestSuite[] suites = {
                new TestUser(),
                new TestExercise(),
                new TestPath(),
                new TestPathFinder(),
                new TestStack()
        };

        int totalTests    = 0;
        int totalPasses   = 0;
        int totalFailures = 0;
        boolean anyFailures = false;

        for (TestSuite suite : suites) {
            System.out.println("\n----------------------------------------");
            System.out.println("  " + suite.name());
            System.out.println("----------------------------------------");

            TestResults results = suite.runTests();

            System.out.println("  Tests:    " + results.totalTests());
            System.out.println("  Passed:   " + results.successes());
            System.out.println("  Failed:   " + results.failures());

            totalTests    += results.totalTests();
            totalPasses   += results.successes();
            totalFailures += results.failures();

            if (results.failures() > 0) {
                anyFailures = true;
            }
        }

        System.out.println("\n========================================");
        System.out.println("  OVERALL RESULTS");
        System.out.println("========================================");
        System.out.println("  Total tests:  " + totalTests);
        System.out.println("  Total passed: " + totalPasses);
        System.out.println("  Total failed: " + totalFailures);
        System.out.println();

        if (anyFailures) {
            System.out.println("  RESULT: There were test failures.");
        } else {
            System.out.println("  RESULT: All tests passed!");
        }
        System.out.println("========================================");
    }
}