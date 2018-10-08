package org.clearfuny.funnytest.interner.runner;

import org.clearfuny.funnytest.exception.RunFailException;
import org.clearfuny.funnytest.interner.model.TestCase;

public interface CaseRun {

    public void run(TestCase testCase) throws RunFailException;
}
