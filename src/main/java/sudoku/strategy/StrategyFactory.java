package sudoku.strategy;

/**
 * hides strategy and their creations
 */

public class StrategyFactory {

  public Resolvable createNackedSingleInACellStrategy(){
    return new NakedSingleInACell();
  }
  public Resolvable createHiddenSingleInACellStrategy(){
    return new HiddenSingleInACell();
  }
  public Resolvable createPointingPairsInCellStrategy(){
    return new PointingPairsInCell();
  }
  public Resolvable createBacktrackStrategy(){
    return new BacktrackLucia();
  }

}
