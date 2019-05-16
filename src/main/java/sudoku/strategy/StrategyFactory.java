package sudoku.strategy;

/**
 * hides strategy and their creations
 */

public class StrategyFactory {

  public Resolvable createNakedSingleInACellStrategy(){
    return new NakedSingleStrategy();
  }
  public Resolvable createHiddenSingleInACellStrategy(){
    return new HiddenSingleStrategy();
  }
  public Resolvable createPointingPairsInCellStrategy(){ return new PointingPairsStrategy(); }
  public Resolvable createPointingPairsRowColumn(){ return new PointingPairsRowColumnStrategy(); }
  public Resolvable createPointingPairsBox(){ return new PointingPairsBoxStrategy(); }
  public Resolvable createBacktrackStrategy(){ return new BacktrackStrategy(); }

}
