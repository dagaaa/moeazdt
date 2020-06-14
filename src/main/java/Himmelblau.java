import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.AbstractProblem;

public class Himmelblau  extends AbstractProblem {


    public Himmelblau() {
        super(2, 1);
    }

    @Override
    public void evaluate(Solution solution) {
        double shift=2.0;
        double[] x = EncodingUtils.getReal(solution);
        double[] f = new double[numberOfObjectives];
        double y1=x[0]+shift+3;
        double y2=x[1]+shift +2;
//        double y3=x[0]+shift +3;
//        double y4=x[0]+shift +2;



        f[0] = (Math.pow(Math.pow(y1,2)+y2-11,2)+Math.pow(y1+Math.pow(y2,2)-7,2));
//        (Math.pow(Math.pow(y3,2)+y4-11,2)+Math.pow(y3+Math.pow(y4,2)-7,2));
//        f[1] = 1- Math.sqrt(Math.abs(x[0]-2))+2*Math.pow(x[1]-Math.sin(6*Math.PI*Math.abs(x[0]-2)+Math.PI),2);

        solution.setObjectives(f);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(getNumberOfVariables(), getNumberOfObjectives());
        solution.setVariable(0, new RealVariable(-10.0, 10));
        solution.setVariable(1, new RealVariable(-10.0, 10.0));

        return solution;
    }
}
