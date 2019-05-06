package sudoku.command;


import sudoku.model.Sudoku;
import sudoku.step.OneChangeStep;
import sudoku.step.Step;
import sudoku.strategy.BacktrackLucia;
import sudoku.strategy.Resolvable;

import java.util.*;

/*
 * uses set strategies in order to resolve a sudoku.
 * the simples strategy will be used as much as possible, if there's no progress with one method, then more
 * sophisticated method will be called
 */

// TODO in the future the steps forwards and backwards will be implemented with methods returning Command object

public class AutomatedInvoker implements Invoker {
    private List<Command> commands = new LinkedList<>();
    private List<Step> stepListFromAllUsedMethods = new ArrayList<>();
    private List<Resolvable> strategies = new ArrayList<>();
    private int currentStep = -1;

    public void setStrategies(Resolvable ... useStrategies) {
        this.strategies = new ArrayList<>();
        this.strategies.addAll(Arrays.asList(useStrategies));
    }

    private Sudoku sudoku;

//    public List<Step> getStepListFromAllUsedMethods() {
//        return stepListFromAllUsedMethods;
//    }

    public Sudoku getSudoku() {
        return sudoku;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public AutomatedInvoker() {}

    public AutomatedInvoker(Sudoku sudoku) {
        this.sudoku = sudoku;
//         by default only Backtrack Strategy will be used
        this.strategies.add(new BacktrackLucia());
    }

    @Override
    public List<Step> solvingStepsOrderLucia() {
        //CommandPicker commandPicker = new CommandPicker()
//        Resolvable currentlyUsedMethod;

            for (int i = 0; i < strategies.size(); i++) {
            //    System.out.println(strategies.get(i).getName());
                Command command = new CommandPicker(strategies.get(i), sudoku);
                sudoku = command.execute();
                stepListFromAllUsedMethods.addAll(strategies.get(i).getStepList()); // ********************** NEW FUNCTIONALITY
                commands.add(command);
                if (sudoku.isSudokuResolved()) {
                    break;
                }
                if (strategies.get(i).isUpdated() && !sudoku.isSudokuResolved()) {
                    i=-1;
                }
            }
        if (!sudoku.isSudokuResolved()) {
            System.out.println("Sudoku needs more advanced methods to be completely resolved");
        }

        //
        // printStepList();

        return stepListFromAllUsedMethods;
    }

    /**
     * prints the step list
     */
    public void printStepList() {

        System.out.println("*********************");
        System.out.println(" ***  ALL STEPS  *** ");
        System.out.println("*********************");

        int count = 1;
        for (Step step : stepListFromAllUsedMethods) {


            if (((OneChangeStep)step).getSolvingStrategyName().equals("0: NackedSingleInACell") ||
                    ((OneChangeStep)step).getSolvingStrategyName().equals("1: HiddenSingleInACell")) {
                System.out.println(count++ + ". " +
                        ((OneChangeStep) step).getSolvingStrategyName() + "\n" +
                        "[" + ((OneChangeStep) step).getCell().getI() + ", " + ((OneChangeStep) step).getCell().getJ() + "] = " +
                        ((OneChangeStep) step).getCell().getActualValue() +"\n" +

                        ((OneChangeStep) step).getSudoku());
            } else if (((OneChangeStep)step).getSolvingStrategyName().equals("2: PointingPairsInCell")) {
                System.out.println(count++ + ". " +
                        ((OneChangeStep) step).getSolvingStrategyName() + "\n" +
                        "[" + ((OneChangeStep) step).getCell().getI() + ", " + ((OneChangeStep) step).getCell().getJ() + "] = " +
                        ((OneChangeStep) step).getCell().getActualValue() +"\n" +
                        "[" + ((OneChangeStep) step).getPartnerCell().getI() + ", " + ((OneChangeStep) step).getPartnerCell().getJ() + "] = " +
                        ((OneChangeStep) step).getPartnerCell().getActualValue());
                Map<int[], Integer> deletedPossibilitiesWithLocation = ((OneChangeStep) step).getDeletedPossibilitiesWithLocation();
                for (int[] key : deletedPossibilitiesWithLocation.keySet()) {
                    System.out.println( ((OneChangeStep) step).getDeletedPossibilitiesWithLocation().get(key) + ": " + Arrays.toString(key));
                }
                System.out.println(((OneChangeStep) step).getSudoku());
            } else {
                System.out.println(count++ + ". " +
                        ((OneChangeStep) step).getSolvingStrategyName() + "\n" +
                        ((OneChangeStep) step).getSudoku());
            }


        }
    }

    @Override
    public Command getNextState() {
        return null;
    }

    @Override
    public Command getPreviousState() {
        return null;
    }

    @Override
    public Step getPreviousStep() {
        currentStep--;
        if (currentStep < 0) {
            currentStep = 0;
        }
        return stepListFromAllUsedMethods.get(currentStep);
    }

    @Override
    public Step getNextStep() {
        currentStep++;
        int lastStepIndex = stepListFromAllUsedMethods.size()-1;
        if (currentStep > lastStepIndex) {
            currentStep = lastStepIndex;
        }
        return stepListFromAllUsedMethods.get(currentStep);
    }
}
