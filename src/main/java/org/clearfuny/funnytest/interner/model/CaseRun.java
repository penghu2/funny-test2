package org.clearfuny.funnytest.interner.model;

import org.clearfuny.funnytest.exception.RunFailException;

public interface CaseRun {

    public void run(TestCase testCase) throws RunFailException;
}
