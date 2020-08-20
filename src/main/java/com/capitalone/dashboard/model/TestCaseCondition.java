package com.capitalone.dashboard.model;

public class TestCaseCondition {
    private String condition;
    private TestCaseConditionResult result;


    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public TestCaseConditionResult getResult() {
        return result;
    }

    public void setResult(TestCaseStatus status, long duration) {
        TestCaseConditionResult r = new TestCaseConditionResult();
        r.setDuration(duration);
        r.setStatus(status);
        this.result = r;
    }
}
