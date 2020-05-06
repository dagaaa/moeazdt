import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.asList;

public class Evaluation {
    public static void main(String[] args) throws IOException {
        FileWriter fileWriter = new FileWriter("results.csv");
        fileWriter.write("problem, algorithm, populationSize, maxEvaluations, elapsedTime [ms]\n");

        //configure and run this experiment
        Collection<Class<? extends Problem>> problems = asList(SymPartRotate.class, SymPartSimple.class);
        int[] populationSizeArray = {10, 100, 1_000, 10_000};
        int[] maxEvaluationsArray = {1_000, 10_000, 100_000};
        String[] algorithms = {"NSGAII", "GDE3", "eMOEA"};

        for (Class<? extends Problem> problem : problems) {
            for (String algorithm : algorithms) {
                for (int populationSize : populationSizeArray) {
                    for (int maxEvaluations : maxEvaluationsArray) {
                        String label = String.format("Problem: %s, Algorithm: %s Population size: %s, Max evaluations: %s", problem,algorithm, populationSize, maxEvaluations);

                        Executor executor = new Executor()
                                .withProblemClass(problem)
                                .withAlgorithm(algorithm)
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
                                .append(problem.getSimpleName())
                                .append(", ")
                                .append(algorithm)
                                .append(", ")
                                .append(String.valueOf(populationSize))
                                .append(", ")
                                .append(String.valueOf(maxEvaluations))
                                .append(", ")
                                .append(String.valueOf(elapsedTime))
                                .append("\n");
                        printResults(problem.getSimpleName(), result);

                        new Plot()
                                .add(label, result)
                                .show();
                    }
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
