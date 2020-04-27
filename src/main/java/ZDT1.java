import java.io.FileWriter;
import java.io.IOException;

import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

import static java.lang.System.currentTimeMillis;

public class ZDT1 {
    public static void main(String[] args) throws IOException {
        FileWriter fileWriter = new FileWriter("results.csv");
        fileWriter.write("problem, populationSize, maxEvaluations, elapsedTime [ms]\n");

        //configure and run this experiment
        String[] problems = {"ZDT1", "ZDT2", "ZDT3"};
        int[] populationSizeArray = {10, 100, 1_000, 10_000};
        int[] maxEvaluationsArray = {1_000, 10_000, 100_000};

        for (String problem : problems) {
            for (int populationSize : populationSizeArray) {
                for (int maxEvaluations : maxEvaluationsArray) {
                    String label = String.format("Problem: %s, Population size: %s, Max evaluations: %s", problem, populationSize, maxEvaluations);

                    Executor executor = new Executor()
                            .withProblem(problem)
                            .withAlgorithm("NSGAII")
                            .withMaxEvaluations(10_000)
                            .withProperty("populationSize", populationSize)
                            .withProperty("maxEvaluations", maxEvaluations)
                            .withProgressListener(progressEvent -> System.out.format(
                                    "Label: %s, Elapsed time: %s, Remaining time: %s, Complete %s percent\n",
                                    label,
                                    progressEvent.getElapsedTime(),
                                    progressEvent.getRemainingTime(),
                                    progressEvent.getPercentComplete()
                            ));

                    long before = currentTimeMillis();
                    NondominatedPopulation result = executor.run();
                    long elapsedTime = currentTimeMillis() - before;

                    fileWriter
                            .append(problem)
                            .append(", ")
                            .append(String.valueOf(populationSize))
                            .append(", ")
                            .append(String.valueOf(maxEvaluations))
                            .append(", ")
                            .append(String.valueOf(elapsedTime))
                            .append("\n");
//                    printResults(problem, result);

                    new Plot()
                            .add(label, result)
                            .show();
                }
            }
        }

        fileWriter.flush();
        fileWriter.close();
    }

    private static void printResults(String problem, NondominatedPopulation result) {
        //display the results
        System.out.println(problem);
        System.out.format("Objective1  Objective2%n");

        for (Solution solution : result) {
            System.out.format(
                    "%.4f      %.4f%n",
                    solution.getObjective(0),
                    solution.getObjective(1)
            );
        }
    }
}
