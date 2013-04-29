package hudson.plugins.cigame.rules.build;

import hudson.model.Result;
import hudson.plugins.cigame.model.RuleResult;
import hudson.plugins.cigame.rules.build.BuildResultRule;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class BuildResultRuleTest {

    @Test
    public void testFirstBuildSuccess() {
        BuildResultRule rule = new BuildResultRule(10, 100, -10, -100);
        RuleResult results = rule.evaluate(Result.SUCCESS, null);
        assertThat("Successful build should give 100 results",  results.getPoints(), is((double) 100));
    }

    @Test
    public void testFirstBuildFailed() {
        BuildResultRule rule = new BuildResultRule(10, 100, -10, -100);
        RuleResult results = rule.evaluate(Result.FAILURE, null);
        assertThat("Failed build should give -100 results", results.getPoints(), is((double) -100));
    }

    @Test
    public void testFirstBuildWasUnstable() {
        BuildResultRule rule = new BuildResultRule(10, 100, -10, -100);
        RuleResult results = rule.evaluate(Result.UNSTABLE, null);
        assertThat("Unstable build should give 0 poitns", results.getPoints(), is((double) 0));
    }

    @Test
    public void testLastBuildWasUnstable() {
        BuildResultRule rule = new BuildResultRule(10, 100, -10, -100);
        RuleResult results = rule.evaluate(Result.SUCCESS, Result.UNSTABLE);
        assertThat("Fixed build should give 100 results", results.getPoints(), is((double)100));
    }

    @Test
    public void testContinuedBuildFailure() {
        BuildResultRule rule = new BuildResultRule(10, 100, -10, -100);
        RuleResult results = rule.evaluate(Result.FAILURE, Result.FAILURE);
        assertThat("Continued failure gets -10 points", results.getPoints(), is((double) -10));
    }

    @Test
    public void testContinuedUnstableBuild() {
        BuildResultRule rule = new BuildResultRule(10, 100, -10, -100);
        RuleResult results = rule.evaluate(Result.UNSTABLE, Result.UNSTABLE);
        assertThat("No change in usntable result should return 0 points", results.getPoints(), is((double) 0));
    }

    @Test
    public void testLastBuildWasAborted() {
        BuildResultRule rule = new BuildResultRule(10, 100, -10, -100);
        RuleResult results = rule.evaluate(Result.FAILURE, Result.ABORTED);
        assertThat("Previous aborted build should get -10 points", results.getPoints(), is((double) -10));
    }

    @Test
    public void testLastBuildWasFine() {
        BuildResultRule rule = new BuildResultRule(10, 100, -10, -100);
        RuleResult results = rule.evaluate(Result.FAILURE, Result.SUCCESS);
        assertThat("Breaking the build should get -100 points", results.getPoints(), is((double) -100));
    }

    @Test
    public void testContinuedBuildSuccess() {
        BuildResultRule rule = new BuildResultRule(10, 100, -10, -100);
        RuleResult results = rule.evaluate(Result.SUCCESS, Result.SUCCESS);
        assertThat("No change in result should give 10 results", results.getPoints(), is((double)10));
    }

    @Test
    public void testCurrentBuildWasUnstable() {
        BuildResultRule rule = new BuildResultRule(10, 100, -10, -100);
        RuleResult results = rule.evaluate(Result.UNSTABLE, Result.SUCCESS);
        assertThat("Unstable builds should return give 0 points", results.getPoints(), is((double) 0));
    }

    @Test
    public void testLastBuildWasBroking() {
        BuildResultRule rule = new BuildResultRule(10, 100, -10, -100);
        RuleResult results = rule.evaluate(Result.SUCCESS, Result.FAILURE);
        assertThat("Fixing the build should get 100 points", results.getPoints(), is((double) 100));
    }
}
