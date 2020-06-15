import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;

import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.asList;

public class Evaluation {
    public static void main(String[] args) throws IOException {
        setProperties();

        FileWriter fileWriter = new FileWriter("results.csv");
        fileWriter.write("problem, algorithm, populationSize, maxEvaluations, elapsedTime [ms]\n");

        //configure and run this experiment
        Collection<Class<? extends Problem>> problems = asList(Equal.class);
        int[] populationSizeArray = {1000};
        int[] maxEvaluationsArray = {TwoPeak.MAX_ITERATIONS};
        String[] algorithms = {"NSGAII"};

        for (Class<? extends Problem> problem : problems) {
            for (String algorithm : algorithms) {
                for (int populationSize : populationSizeArray) {
                    for (int maxEvaluations : maxEvaluationsArray) {
                        String label = String.format("Problem: %s, Algorithm: %s Population size: %s, Max evaluations: %s", problem,algorithm, populationSize, maxEvaluations);

                        Executor executor = new Executor()
                                .withProblemClass(problem)
                                .withAlgorithm(algorithm)
                                .withMaxEvaluations(maxEvaluations)
                                .withProperty("populationSize", populationSize)
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
            System.out.println(

                    solution.getVariable(0).toString() + "     " +
//                    +solution.getVariable(1).toString()   +"  "+
                    solution.getObjective(0)
            );
        }
    }

    private static void setProperties() {
        System.setProperty("org.moeaframework.util.pisa.algorithms", "hype, spea2, nsga2");
        System.setProperty("org.moeaframework.algorithm.pisa.algorithms", "hype");
        System.setProperty("org.moeaframework.algorithm.pisa.hype.command", "C:\\Users\\dagma\\Downloads\\moeazdt\\src\\main\\resources\\hype_win\\hype.exe");
//        System.setProperty("org.moeaframework.algorithm.pisa.hype.parameters", "seed, tournament, mating, bound, nrOfSamples");
        System.setProperty("org.moeaframework.algorithm.pisa.hype.configuration", "C:\\Users\\dagma\\Downloads\\moeazdt\\src\\main\\resources\\hype_win\\hype_param.txt");
    }

}
