import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.AbstractProblem;

import java.util.Arrays;
import java.util.stream.IntStream;

import static java.lang.Math.*;

public class TwoPeak extends AbstractProblem {

    public static final int MAX_ITERATIONS = 10_000;
    private static final double UPPER_BOUND = 30.0;
    private static final double LOWER_BOUND = -10.0;
    private static final double R = UPPER_BOUND - LOWER_BOUND;
    private int currentIteration = 0;

    public TwoPeak() {
        super(1, 1);
    }

    @Override
    public void evaluate(Solution solution) {
        currentIteration++;
        double[] xIT = EncodingUtils.getReal(solution);
        double[] f = new double[numberOfObjectives];
        double[] shiftIT = calcShiftIT(xIT);
        double[] xI = calcXI(xIT, shiftIT);
        double[] yI = calcYI(xI);
        f[0] = calcF(yI);
        System.out.println(f[0]);

        solution.setObjectives(f);
    }

    private double[] calcShiftIT(double[] xIT) {
        return Arrays.stream(xIT)
                .map(this::calcShiftIT)
                .toArray();
    }

    private double calcShiftIT(double xIT) {
        double commonPart = (-((double) currentIteration / (double) MAX_ITERATIONS)) * R;

        if (xIT <= UPPER_BOUND) {
            return commonPart;
        } else {
            return commonPart - R;
        }
    }

    private double[] calcXI(double[] xIT, double[] shiftIT) {
        return IntStream.range(0, xIT.length)
                .mapToDouble(i -> xIT[i] + shiftIT[i])
                .map(this::calcBoundedXI)
                .toArray();
    }

    private double calcBoundedXI(double xI) {
        if (xI > UPPER_BOUND) {
            double difference = Math.abs(UPPER_BOUND - xI);
            return calcBoundedXI(LOWER_BOUND + difference);
        } else if (xI < LOWER_BOUND) {
            double difference = Math.abs(LOWER_BOUND - xI);
            return calcBoundedXI(UPPER_BOUND - difference);
        } else {
            return xI;
        }
    }

    private double[] calcYI(double[] xI) {
        return Arrays.stream(xI)
                .map(this::calcYI)
                .toArray();
    }

    private double calcYI(double xI) {
        if (xI < 0.0) {
            return -160+pow(xI, 2.0);
        } else if (xI <= 15.0) {
            return (160.0/15)*(xI-15);
        } else if (xI <= 20.0){
            return 200.0/5*(15-xI);
        }
        else
            return -200+Math.pow(xI-20,2);
    }

    private double calcF(double[] yI) {
        double yISum = Arrays.stream(yI).sum();
        return 200*numberOfVariables + yISum;
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(getNumberOfVariables(), getNumberOfObjectives());
        IntStream.range(0, numberOfVariables)
                .forEach(i -> solution.setVariable(i, new RealVariable(LOWER_BOUND, UPPER_BOUND)));

        return solution;
    }
}