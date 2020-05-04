import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

public class SymPartSimple extends AbstractProblem {

    private static final double a = 1;
    private static final double b = 10;
    private static final double c = 8;

    public SymPartSimple() {
        super(2, 2);
    }

    @Override
    public void evaluate(Solution solution) {
        double x1 = EncodingUtils.getReal(solution.getVariable(0));
        double x2 = EncodingUtils.getReal(solution.getVariable(1));

        double tDash1 = Math.signum(x1) * ((Math.abs(x1) - (a + (c / 2))) / ((2 * a) + c));
        double tDash2 = Math.signum(x2) * ((Math.abs(x2) - (b / 2)) / b);

        double t1 = Math.signum(tDash1) * Math.min(Math.abs(tDash1), 1.0);
        double t2 = Math.signum(tDash2) * Math.min(Math.abs(tDash2), 1.0);

        double p1 = x1 - t1 * (c + 2 * a);
        double p2 = x1 - t2 * b;

        double f1 = Math.pow((p1 + a), 2) + Math.pow(p2, 2);
        double f2 = Math.pow((p1 - a), 2) + Math.pow(p2, 2);

        solution.setObjective(0, f1);
        solution.setObjective(1, f2);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(getNumberOfVariables(), getNumberOfObjectives());

        solution.setVariable(0, EncodingUtils.newReal(-20.0, 20.0));
        solution.setVariable(1, EncodingUtils.newReal(-20.0, 20.0));

        return solution;
    }
}
