package com.fidenz.academy.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        Result apiCallResult = JUnitCore.runClasses(ApiCallTest.class);
        for(Failure failure: apiCallResult.getFailures()){
            System.out.println(failure.toString());
        }

        Result entityAndRepoResult = JUnitCore.runClasses(DbRepositoryTest.class);
        for(Failure failure: entityAndRepoResult.getFailures()){
            System.out.println(failure.toString());
        }
    }
}
