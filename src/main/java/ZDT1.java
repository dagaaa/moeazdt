import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

public class ZDT1 {
    public static void main(String[] args) {
        //configure and run this experiment
        String[] problems = {"ZDT1", "ZDT2", "ZDT3"};
        for (String problem : problems) {
            NondominatedPopulation result = new Executor()
                    .withProblem(problem)
                    .withAlgorithm("NSGAII")
                    .withMaxEvaluations(10000)
                    .run();

            //display the results
            System.out.println(problem);
            System.out.format("Objective1  Objective2%n");

            for (Solution solution : result) {
                System.out.format("%.4f      %.4f%n",
                        solution.getObjective(0),
                        solution.getObjective(1));
            }
            new Plot()
                    .add(problem, result)
                    .show();
        }
    }
}
