package sudoku.processingUsingCommand;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import sudoku.model.Sudoku;
import sudoku.processingUsingStrategy.BacktrackLucia;
import sudoku.processingUsingStrategy.Resolvable;
import sudoku.stepHandlers.OneChangeStep;
import sudoku.stepHandlers.Step;

public class AutomatedInvoker implements Invoker {
    private List<Command> commands = new LinkedList<>();
    private List<Step> stepListFromAllUsedMethods = new ArrayList<>();
    private List<Resolvable> strategies = new ArrayList<>();
    private int currentStep = 0;

    public void setStrategies(Resolvable ... useStrategies) {
        this.strategies = new ArrayList<>();
        for (Resolvable strategy: useStrategies) {
            this.strategies.add(strategy);
        }
    }

    private Sudoku sudoku;

    public List<Step> getStepListFromAllUsedMethods() {
        return stepListFromAllUsedMethods;
    }

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
    public Command solvingStepsOrder() {
        //CommandPicker commandPicker = new CommandPicker()
        Resolvable currentlyUsedMethod;

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

        printStepList();

        return null;
    }

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
    public List<String> getMethodUsedInAllStep() {
        return null;
    }
    // TODO implement previous and next methods
    @Override
    public Command getPreviousState() {
//        int currentStep = commands.size() - 1;
//        if (currentStep - 1 >= 0) {
//            return commands.get(currentStep - 1);
//        } else {
//            extAppLogFile.info(getClass().getName() + " no previous element exists. First element has been returned.");
//            return commands.get(0);
//        }
        return null;
    }

    @Override
    public Command getPreviousState(int step) {
//        int currentStep = commands.size() - 1;
//        if (step >= 0 && step < currentStep) {
//            return commands.get(step);
//        } else {
//            extAppLogFile.info(getClass().getName() + " invalid previous index inserted. First element has been returned.");
//            return commands.get(0);
//        }
        return null;
    }

    @Override
    public Command getNextState() {
//        int currentStep = commands.size() - 1;
//        if (step >= 0 && step < currentStep) {
//            return commands.get(step);
//        } else {
//            extAppLogFile.info(getClass().getName() + " invalid previous index inserted. First element has been returned.");
//            return commands.get(0);
//        }
        return null;
    }

}
