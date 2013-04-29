package hudson.plugins.cigame.rules.build;

import hudson.model.AbstractBuild;
import hudson.model.Result;
import hudson.plugins.cigame.model.Rule;
import hudson.plugins.cigame.model.RuleResult;

/**
 * Rule that gives points on the result of the build.
 */
public class BuildResultRule implements Rule {

    private int failurePoints;
    private int breakPoints;
    private int successPoints;
    private int healPoints;

    public BuildResultRule() {
        this(1, 5, -1, -5);
    }

    public BuildResultRule(int successPoints, int healPoints, int failurePoints, int breakPoints) {
        this.successPoints = successPoints;
        this.failurePoints = failurePoints;
        this.breakPoints = breakPoints;
        this.healPoints = healPoints;
    }

    public String getName() {
        return Messages.BuildRuleSet_BuildResult(); //$NON-NLS-1$
    }

    public RuleResult evaluate(AbstractBuild<?, ?> build) {
        Result result = build.getResult();
        Result lastResult = null;
        if (build.getPreviousBuild() != null) {
            lastResult = build.getPreviousBuild().getResult();
        }
        return evaluate(result, lastResult);
    }

    String resultMessageFor(Result result) {
        if (result == null)
          return "";

        if (result.isBetterOrEqualTo(Result.SUCCESS)) {
            return Messages.BuildRuleSet_BuildResult();
        } else {
            return Messages.BuildRuleSet_BuildFailed();
        }
    }

    RuleResult evaluate(Result result, Result lastResult) {
        int points = 0;
        
        if (result == Result.SUCCESS) {
          if ((lastResult == null) || lastResult.isWorseThan(Result.SUCCESS))
            points = healPoints;
          else
            points = successPoints;
        } 

        if (result == Result.FAILURE) {
          if ((lastResult == null) || lastResult.isBetterThan(Result.FAILURE))
            points = breakPoints;
          else
            points = failurePoints;
        }

        return new RuleResult(points, resultMessageFor(result));
    }
}
