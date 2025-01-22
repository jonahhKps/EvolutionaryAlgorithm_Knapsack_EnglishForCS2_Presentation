import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class EA1 {

    static ArrayList<int[]> presentations = new ArrayList<>();

    static int populationSize = 10;
    static ArrayList<boolean[]> currentPopulation = new ArrayList<>();

    static int maxTime = 90;
    static double mutationrate = 0.02;

    static String s = "";

    static Random random = new Random();

    public static void main(String[] args) {
        if (populationSize % 2 == 1) throw new RuntimeException("populationSize must be even");

        presentations.add(new int[]{12, 60});
        presentations.add(new int[]{30, 85});
        presentations.add(new int[]{9, 40});
        presentations.add(new int[]{25, 90});
        presentations.add(new int[]{17, 70});
        presentations.add(new int[]{22, 50});
        presentations.add(new int[]{14, 65});

        initialization(populationSize);

        s += 0 + "\t" + averageFitness() + "\n";

        for (int i = 0; i < 250; i++) {
            selection();
            crossover();
            mutation();

            s += (i+1) + "\t" + averageFitness() + "\n";

            for (boolean[] chromosome : currentPopulation) {
                System.out.print(Arrays.toString(chromosome) + " " + Arrays.toString(fitnessEvalutation(chromosome)) + "\n");
            }

            System.out.println();
        }
        System.out.println(s);

    }

    private static void initialization(int populationSize) {
        for (int i = 0; i < populationSize; i++) {
            currentPopulation.add(new boolean[presentations.size()]);
            for (int j = 0; j < presentations.size(); j++) {
                if (Math.random() < 0.5) currentPopulation.get(i)[j] = false;
                else currentPopulation.get(i)[j] = true;
            }
        }

        System.out.println("Initialization:");
        for (boolean[] chromosome : currentPopulation) {
            System.out.print(Arrays.toString(chromosome) + " " + Arrays.toString(fitnessEvalutation(chromosome)) + "\n");
        }


    }

    private static int[] fitnessEvalutation(boolean[] chromosome) {
        int time = 0;
        int fitness = 0;
        for (int i = 0; i < presentations.size(); i++) {
            if (chromosome[i]) {
                time += presentations.get(i)[0];
                fitness += presentations.get(i)[1];
            }
        }
        if (time > maxTime) fitness = 0;
        return new int[]{time, fitness};
    }

    private static void selection() {
        int[][] fitnessValues = new int[populationSize][2];
        int fitnessSum = 0;
        Double[] chromosomeProbabilities = new Double[populationSize];
        for (int i = 0; i < populationSize; i++) {
            fitnessValues[i][0] = i;
            fitnessValues[i][1] = fitnessEvalutation(currentPopulation.get(i))[1];
            fitnessSum += fitnessValues[i][1];
        }

        for (int i = 0; i < populationSize; i++) {
            chromosomeProbabilities[i] = (double) fitnessValues[i][1]/fitnessSum;
        }

        //System.out.println(Arrays.toString(chromosomeProbabilities));
        //System.out.println(fitnessSum);

        ArrayList<boolean[]> newPopulation = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            double currentLowerBoundProbability = 0;
            double rnd = Math.random();
            for (int j = 0; j < populationSize; j++) {
                if (rnd > currentLowerBoundProbability && rnd <= (currentLowerBoundProbability + chromosomeProbabilities[j])) {
                    newPopulation.add(currentPopulation.get(j).clone());
                    break;
                }
                currentLowerBoundProbability += chromosomeProbabilities[j];
            }
        }

        /*
        System.out.println("");
        for (boolean[] chromosome : newPopulation) {
            System.out.print(Arrays.toString(chromosome) + " " + Arrays.toString(fitnessEvalutation(chromosome)) + "\n");
        }
        */

        currentPopulation = newPopulation;

    }

    private static void crossover() {
        for (int i = 0; i < populationSize; i += 2) {
            if (Math.random() < 0.5) {
                //System.out.println("crossover:" + i + " & " + (i+1));
                boolean[] chromosome1 = currentPopulation.get(i).clone();
                boolean[] chromosome2 = currentPopulation.get(i+1).clone();
                System.arraycopy(currentPopulation.get(i+1), presentations.size()/2, chromosome1, presentations.size()/2, presentations.size() - presentations.size()/2);
                System.arraycopy(currentPopulation.get(i), presentations.size()/2, chromosome2, presentations.size()/2, presentations.size() - presentations.size()/2);
                //System.out.println("\n" + Arrays.toString(chromosome1) + "\n" + Arrays.toString(chromosome2));
                currentPopulation.set(i, chromosome2);
                currentPopulation.set(i+1, chromosome1);
            }
        }

        /*
        System.out.println();
        for (boolean[] chromosome : currentPopulation) {
            System.out.print(Arrays.toString(chromosome) + " " + Arrays.toString(fitnessEvalutation(chromosome)) + "\n");
        }
         */
    }

    private static void mutation() {
        for (int i = 0; i < populationSize; i++) {
            if (Math.random() < mutationrate) {
                int rnd = random.nextInt(presentations.size());
                //System.out.println("mutation: " + i);
                currentPopulation.get(i)[rnd] = !currentPopulation.get(i)[rnd];
            }
        }

        /*
        System.out.println();
        for (boolean[] chromosome : currentPopulation) {
            System.out.print(Arrays.toString(chromosome) + " " + Arrays.toString(fitnessEvalutation(chromosome)) + "\n");
        }
        */
    }

    private static double averageFitness() {
        double fitnessSum = 0;
        for (int i = 0; i < populationSize; i++) {
            fitnessSum += fitnessEvalutation(currentPopulation.get(i))[1];
        }
        return fitnessSum/populationSize;
    }

}
