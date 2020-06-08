import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.AbstractProblem;

public class MMF1 extends AbstractProblem {


    public MMF1() {
        super(2, 2);
    }

    @Override
    public void evaluate(Solution solution) {
        double[] x = EncodingUtils.getReal(solution);
        double[] f = new double[numberOfObjectives];



        f[0] = Math.abs(x[0]-2);
        f[1] = 1- Math.sqrt(Math.abs(x[0]-2))+2*Math.pow(x[1]-Math.sin(6*Math.PI*Math.abs(x[0]-2)+Math.PI),2);

        solution.setObjectives(f);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(getNumberOfVariables(), getNumberOfObjectives());
        solution.setVariable(0, new RealVariable(1.0, 3.0));
        solution.setVariable(1, new RealVariable(-1.0, 1.0));

        return solution;
    }
}
