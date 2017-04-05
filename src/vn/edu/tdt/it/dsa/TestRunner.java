package vn.edu.tdt.it.dsa;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by walpurisnacht on 04/04/2017.
 */
public class TestRunner {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(WarehouseBookTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        if (result.wasSuccessful()) {
            System.out.println("TEST COMPLETED WITHOUT ERROR");
        } else {
            System.out.println("ERROR DETECTED!");
        }
    }
}
