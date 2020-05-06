import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.AbstractProblem;

public class SymPartSimple extends AbstractProblem {

    private static final double a = 1;
    private static final double b = 10;
    private static final double c = 8;

    public SymPartSimple() {
        super(11, 2);
    }

    @Override
    public void evaluate(Solution solution) {
        double[] x = EncodingUtils.getReal(solution);
        double[] f = new double[numberOfObjectives];

        double tDash1 = Math.signum(x[0]) * ((Math.abs(x[0]) - (a + (c / 2))) / ((2 * a) + c));
        double tDash2 = Math.signum(x[1]) * ((Math.abs(x[1]) - (b / 2)) / b);

        double t1 = Math.signum(tDash1) * Math.min(Math.abs(tDash1), 1.0);
        double t2 = Math.signum(tDash2) * Math.min(Math.abs(tDash2), 1.0);

        double p1 = x[0] - t1 * (c + 2 * a);
        double p2 = x[1] - t2 * b;

         f[0] = Math.pow((p1 + a), 2) + Math.pow(p2, 2);
         f[1] = Math.pow((p1 - a), 2) + Math.pow(p2, 2);

        solution.setObjectives(f);
//        solution.setObjective(1, f2);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(getNumberOfVariables(), getNumberOfObjectives());
        for (int i = 0; i < getNumberOfVariables(); i++) {
            solution.setVariable(i, new RealVariable(-20.0, 20.0));
        }


        return solution;
    }
}
