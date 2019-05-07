package sudoku.strategy;

public class StrategyFactory {

  Resolvable createPointing(){
    return new PointingPairsInCell();
  }

}
