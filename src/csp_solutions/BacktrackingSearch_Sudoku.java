package csp_solutions;

import core_algorithms.BacktrackingSearch;
import csp_problems.*;

import java.util.List;


public class BacktrackingSearch_Sudoku extends BacktrackingSearch<String,Integer>{

    public BacktrackingSearch_Sudoku(Sudoku problem){
        super(problem);
    }

    /**
     * To revise an arc: for each value in tail's domain, there must be a value in head's domain that's different
     *                   if not, delete the value from the tail's domain
     * @param head head of the arc to be revised
     * @param tail tail of the arc to be revised
     * @return true if the tail has been revised (lost some values), false otherwise
     */
    public boolean revise(String head, String tail) {
        Boolean removed = false;
        if (getAllVariables().get(head).domain().size() == 1){
            if (getAllVariables().get(tail).domain().contains(getAllVariables().get(head).domain().get(0))) {
                getAllVariables().get(tail).domain().remove(getAllVariables().get(head).domain().get(0));
                removed = true;
            }
        }
        return removed;
    }

    /**
     * Implementing the minimum-remaining-values(MRV) ordering heuristic.
     * @return the variable with the smallest domain among all the unassigned variables;
     *         null if all variables have been assigned
     */
    public String selectUnassigned(){
        String var = null;
        for (String variable: getAllVariables().keySet()){
            if (assigned(getAllVariables().get(variable).name())){
                if (var != null){
                    if (getAllVariables().get(variable).domain().size() < getAllVariables().get(var).domain().size()) {
                        var = variable;
                    }
                } else {    var = variable;     }
            }
        }
        return getAllVariables().get(var).name();
    }

    /**
     * @param args (no command-line argument is needed to run this program)
     */
    public static void main(String[] args) {
        String filename = "./SudokuTestCases/TestCase1.txt";
        Sudoku problem = new Sudoku(filename);
        BacktrackingSearch_Sudoku agent = new BacktrackingSearch_Sudoku(problem);
        System.out.println("loading puzzle from " + filename + "...");
        problem.printPuzzle(problem.getAllVariables());
        if(agent.initAC3() && agent.search()){
            System.out.println("Solution found:");
            problem.printPuzzle(agent.getAllVariables());
        }else{
            System.out.println("Unable to find a solution.");
            problem.printPuzzle(problem.getAllVariables());
        }
    }
}
