/**
 *  ake interface su a ake maju implementacie
 *
 *  ake medzi sebou interaguju tieto triedy/interface
 *
 *
 *
 *  myslienka package
 *
 *  interace summary
 *
 *  class summary
 *
 *  exception summary
 */
/**
 * <p>This package implements Command pattern.</p>
 *
 * <p>Command represents single state in a sequence of states from initial state to solved sudoku. Invoker provides
 * a way to move to previous command or next command (possibly generating next command).</p>
 *
 * <p>CommandPicker is the only implementation of the Command interface. The invoker interface has two implementations
 * AutomatedInvoker and ManualInvoker. The automatedInvoker first generates whole sequence of states from initial state
 * to solved sudoku using all strategies available, then enables moving from state to state, while ManualInvoker
 * enables user to chose strategy to generate next state. ManualInvoker enables to return to previous state and choose
 * different strategy thus creating new state.</p>
 */
package sudoku.command;