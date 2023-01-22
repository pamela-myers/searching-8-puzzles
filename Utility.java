import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *      Briana Collins brcollins@alaska.edu
 *      Pamela Myers pamyers@alaska.edu
 *
 *  A collection of methods used by the search algorithms, primarily helper methods
 *  for comparison, heuristics, and statistics gathering,
 */
public class Utility {

    private int totalNodesExpanded = 0;     //keep track of how many gameStates a search method needs to expand.
    private int sizeOfClosedList = 0;       //keep track of how large the closed list is.
    private int maxSizeOfFrontierList = 0;  //keep track of largest size of open list at any given time.
    private int solutionLength = 0;         //keep track of how many steps it takes to solve a puzzle.
    private int treeDepth = 0;              //keep track of how deep the search method needs to go. approx.
                                            //      same as the solution length, but can be
                                            //      updated during the search.
    private String searchName = "";
    private int searchType = 0;

    /**
     * use to initialize an class object that can keep track of
     * perfomance stats for a search method.
     * @param searchType state what search method is being used:
     *                   1. Breadth Search
     *                   2. Greedy Best First Search
     *                   3. A* with Misplaced Tile Heuristic
     *                   4. A* with Manhattan Tile Heuristic
     */
    public Utility(int searchType)
    {
        if (searchType == 1)
        {
            this.searchType = 1;
            searchName = "Breadth Search";
        }else if (searchType == 2)
        {
            this.searchType = 2;
            searchName = "Greedy Best First Search";
        }else if (searchType == 3)
        {
            this.searchType = 3;
            searchName = "A* with Misplaced Tile Heuristic";
        }else if (searchType == 4)
        {
            this.searchType = 4;
            searchName = "A* with Manhanttan Tile Heuristic";
        }else
        {
            System.out.println("Invalid search method type.");
            System.exit(0);
        }
    }

    /**
     * called from a search class to update amount of children nodes examined during search.
     */
    public void addToNodesExpanded()
    {
        totalNodesExpanded += 1;
    }

    /**
     * call from a search class to update size of the closed list
     * @param closedList the list generated during a search by a given method.
     */
    public void updateClosedListLength(ArrayList<Puzzle> closedList)
    {
            sizeOfClosedList = closedList.size();
    }

    /**
     * call from a search class to update size of the frontier list
     * @param frontierList a list maintained by a search method of unchecked game states.
     */
    public void updateFrontierListLength(ArrayList<Puzzle> frontierList)
    {
        if (frontierList.size() > maxSizeOfFrontierList)
        {
            maxSizeOfFrontierList = frontierList.size();
        }
    }

    /**
     * call from a search class to update how many moves is necessary to solve
     * a given puzzle.
     * this is only called at the very end of a search, so will be zero mid-search
     * until then.
     * @param length the minimum number of moves necessary to solve an 8-puzzle from given
     *               start state to given goal state.
     */
    public void setSolutionLength(int length)
    {
        solutionLength = length;
    }

    /**
     * call from a search class to update the current depth of the search tree
     * the search is currently at. is continually updated, so can be called
     * mid-search to get the current relative length of the solution set.
     * @param treeDepth during search, a given gameState will have a chain of
     *                  parent states that generated this state; the number
     *                  of parents is the depth.
     */
    public void setTreeDepth(int treeDepth) {
        this.treeDepth = treeDepth;
    }

    /**
     * helper method; checks if a given Puzzle state matches the goalState.
     *
     * @param puzzle the given 8-puzzle to check.
     * @return True if Puzzle being checked is the goal. False if it is not.
     */
    public static boolean isGoalState(Puzzle puzzle)
    {
        PuzzleSquare[] puzzleSquares = puzzle.getInternalState();
        for (int i = 0; i < puzzleSquares.length; i++)
        {
            if (puzzleSquares[i].getCurrentNumber() !=(puzzleSquares[i].getRequiredNumber()))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * helper method; Checks whether two different gameStates are equal. States are
     * equal if all their current tile numbers match.
     * @param thisPuzzle the current puzzle of interest
     * @param otherPuzzle a second puzzle to check against (i.e., an entry from a List)
     * @return True if both puzzles match; false if not.
     */
    public static boolean isEqual(Puzzle thisPuzzle, Puzzle otherPuzzle)
    {

        for (int i = 0; i < thisPuzzle.getInternalState().length; i++)
        {
            int thisStateCurrent = thisPuzzle.getInternalState()[i].getCurrentNumber();
            int otherStateCurrent = otherPuzzle.getInternalState()[i].getCurrentNumber();
            if (thisStateCurrent != (otherStateCurrent))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper method; Checks whether a given puzzle has already been
     * searched and placed on the closed list.
     * @param puzzle current puzzle of interest that needs to be checked
     * @param closedList list of all puzzles already evaluated by search classes.
     * @return True if puzzle is on the list, false if not.
     */
    public static boolean isOnClosedList(Puzzle puzzle, ArrayList<Puzzle> closedList)
    {
        for (Puzzle checkedState : closedList)
        {
                if (isEqual(puzzle, checkedState))
                {
                    return true;
                }
        }
        return false;
    }

    /**
     * helper method to gather performance statistics needed for report
     * and format them nicely.
     * NOTE: this function calls {@code Collections.reverse} on the 
     * solution list, so there is need to be careful with the order of
     * calling this function and possible manipulation the {@code solutionList}
     * returned by the search methods.
     */
    public String createStatistics(ArrayList<Puzzle> solutionList)
    {
        Collections.reverse(solutionList);

        StringBuilder text = new StringBuilder();
        int searchNameLength = searchName.length();
        StringBuilder nameBox = new StringBuilder();
        for (int i = 0; i < searchNameLength; i++)
        {
            nameBox.append("=");
        }
        text.append("+" + nameBox + "+\n");
        text.append("|" + searchName + "|\n");
        text.append("+" + nameBox + "+\n\n");
        text.append("  Nodes Expanded  |  Size of Closed List   |  Max Frontier List Size  |  Solution Length  |  Tree Depth  \n");
        text.append("------------------+------------------------+--------------------------+-------------------+---------------\n");
        String data = String.format("%17d | %22d | %24d | %17d | %14d \n", totalNodesExpanded, sizeOfClosedList,
                maxSizeOfFrontierList, solutionLength, treeDepth );
        text.append(data);
        text.append("----------------------------------------------------------------------------------------------------------\n\n");

        ArrayList<String[]> movesSplitByLine = new ArrayList<>();
        for (int i = 0; i < solutionLength; i++) {
            String[] parts = solutionList.get(i).toString().split("\n");
            movesSplitByLine.add(parts);
        }

        int numLinesInAsciiDrawing = movesSplitByLine.get(0).length;

        text.append("                        Solution Move List: (read START to GOAL, left to right)");

        //print six game states horizontally in a row before returning to a new line.
        int rowLength = 6;
        int numRows;
        int finalColLength = 0;
        int endRow = rowLength;
        boolean evenDivisor = false;
        if (solutionLength % rowLength == 0) {
            numRows = solutionLength / rowLength;
            evenDivisor = true;
        }
        else {
            numRows = solutionLength / rowLength + 1;
            finalColLength = solutionLength % rowLength;
        }

        for (int rowNum = 0; rowNum < numRows; rowNum++) {

            if (rowNum == numRows - 1 && !evenDivisor)
            {
                endRow = finalColLength;
            }
            for (int drawLine = 0; drawLine < numLinesInAsciiDrawing; drawLine++) {
                for (int colNum = 0; colNum < endRow; colNum++) {
                    int moveInSolutionList = colNum + (rowNum * rowLength);
                    String[] entry = movesSplitByLine.get( moveInSolutionList);
                    text.append("    " + entry[drawLine]);

                }
                    text.append("\n");
            }
        }

        text.append("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");

        return text.toString();
    }

    /**
     * helper method; finds from a list of possible puzzles the state most likely
     * to lead to a solution via the Manhattan heuristic for Greedy Best First.
     * @param openList the list created by a search class of currently unexpanded gameStates.
     * @return the puzzle that has the lowest Manhattan score (defaults to the first entry
     *          if multiple puzzles have the same score.)
     */
    public static Puzzle getLowestGreedyManhattan(ArrayList<Puzzle> openList)
    {
        Puzzle min = openList.get(0);
        int currentMin = min.getPathCostManhattan();

        for (Puzzle state : openList)
        {
            if (state.getPathCostManhattan() < currentMin)
            {
                currentMin = state.getPathCostManhattan();
                min = state;
            }
        }
        return min;
    }

    /**
     * helper method; finds from a list of possible puzzles the state most likely
     * to lead to a solution via the Manhattan heuristic for A*.
     * @param openList the list created by a search class of currently unexpanded gameStates.
     * @return the puzzle that has the lowest Manhattan score (defaults to the first entry
     *          if multiple puzzles have the same score.) plus the lowest search depth.
     */
    public static Puzzle getLowestAStarManhattan(ArrayList<Puzzle> openList)
    {
        Puzzle min = openList.get(0);
        int currentManhattan = min.getPathCostManhattan();
        int currentDepth = min.getDepth();
        int currentMin = currentDepth + currentManhattan;

        for (Puzzle state : openList)
        {
            if ((state.getPathCostManhattan() + state.getDepth()) < currentMin)
            {
                currentManhattan = state.getPathCostManhattan();
                currentDepth = state.getDepth();
                currentMin = currentDepth + currentManhattan;
                min = state;
            }
        }
        return min;
    }

    /**
     * helper method; finds from a list of possible puzzles the state most likely
     * to lead to a solution via the Misplaced heuristic for A*.
     * @param openList the list created by a search class of currently unexpanded gameStates.
     * @return the puzzle that has the lowest Misplaced score (defaults to the first entry
     *          if multiple puzzles have the same score.) plus the lowest search depth.
     */
    public static Puzzle getLowestMisplaced(ArrayList<Puzzle> openList)
    {
        Puzzle min = openList.get(0);
        int currentMisplaced = min.getPathCostMisplaced();
        int currentDepth = min.getDepth();
        int currentMin = currentDepth + currentMisplaced;

        //System.out.println("Current Depth: " + currentDepth + "  |  Cost: " + currentMisplaced);

        for (Puzzle state : openList)
        {
            if ((state.getPathCostMisplaced() + state.getDepth()) < currentMin)
            {
                currentMisplaced = state.getPathCostMisplaced();
                currentDepth = state.getDepth();
                currentMin = currentDepth + currentMisplaced;
                min = state;
            }
        }
        return min;
    }

    public static Puzzle getNextNode(ArrayList<Puzzle> openList)
    {
        Puzzle min = openList.get(0);
        //int currentDepth = min.getDepth();


        return min;
    }

    /**
     * debugger method to see in-progress performance stats.
     */
    public void currentStats()
    {
        System.out.println("number nodes expanded: " + totalNodesExpanded);
        System.out.println("maximum number of children on closed list at any one time: " + sizeOfClosedList);
        System.out.println("maximum number of children on frontier list at any one time: " + maxSizeOfFrontierList);
        System.out.println("current depth of search tree: " + treeDepth);
        System.out.println("Solution length " + solutionLength);
    }

    /**
     * helper method to save performance statistics needed for report
     * to disk. Appends every new search to file. Use createStatistics()
     * to get search data in String form.
     * @param solutionText the data to save to disk in nice string format.
     */
    public void saveToFile(String solutionText)
    {
        final String fileName = "Search Performance Statistics.txt";

        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;

        try
        {
            File file = new File(fileName);

            if (!file.exists())
            {
                file.createNewFile();
            }

            fileWriter = new FileWriter(file.getAbsolutePath(), true);
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(solutionText);

            bufferedWriter.close();
            fileWriter.close();

        } catch( IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
    * The core method to run the search methods.  The structure of the 
    * search is the same for all the methods, which the only difference
    * being which search cost function f(n) = g(n) + h(n) is used.
    * @param initState the starting game state position.
    * @param statistics a helper object constructed in the calling class, which among other things contains a {@code int} flag that lets {@code runSearch} know which search method called it.
    * @param saveToDisk convenience flag that toggles saving to disk on/off.
    */
    public static ArrayList<Puzzle> runSearch(Puzzle initState, Utility statistics, boolean saveToDisk)
    {
        ArrayList<Puzzle> frontierList = new ArrayList<>();
        ArrayList<Puzzle> closedList = new ArrayList<>();
        ArrayList<Puzzle> solutionList = new ArrayList<>();
        Puzzle currentState = initState;

        frontierList.add(currentState);
        Puzzle gameState = null;

        while (frontierList != null)
        {
            if (statistics.searchType == 1)
            {
                //breadth
                gameState = frontierList.get(0);
            } else if (statistics.searchType == 2)
            {
                //greedy
                gameState = getLowestGreedyManhattan(frontierList);
            } else if (statistics.searchType == 3)
            {
                //A* misplaced
                gameState = getLowestMisplaced(frontierList);
            } else if (statistics.searchType == 4)
            {
                //A* manhattan
                gameState = getLowestAStarManhattan(frontierList);
            }else
            {
                return null; //error occurred
            }

            frontierList.remove(gameState);

            if (isGoalState(gameState))
            {
                //create solution list and return
                Puzzle tempState = gameState;
                while(tempState.getParentState() !=null)
                {
                    solutionList.add(tempState);
                    tempState = tempState.getParentState();
                }
                //add the initial gameState.
                if (tempState.getParentState() == null)
                {
                    solutionList.add(tempState);
                }

                statistics.setSolutionLength(solutionList.size());
                statistics.setTreeDepth(solutionList.get(0).getDepth()); //make sure to set correct solution depth for final stats. (due to how breadth search is implemented.)
                String statisticsText = statistics.createStatistics(solutionList);
                System.out.println(statisticsText);
                if (saveToDisk) {
                    statistics.saveToFile(statisticsText);
                }

                return solutionList;
            }
            if (isOnClosedList(gameState, closedList))
            {
                //System.out.println("Found repeat state.");

                //discard current state and start from top of loop again.
                continue;
            } else
            {
                //find blank square and its available moves;
                PuzzleSquare blankSquare = gameState.getBlankSquare();
                int[] moves = blankSquare.getMoves();
                int blankSquarePosition = blankSquare.getSquarePosition();

                //iterate over blank square's available moves
                for (int move: moves)
                {
                    Puzzle tempChild = (Puzzle) gameState.clone();

                    //determine adjacent tile
                    int moveToSquare = blankSquarePosition + move;
                    int moveToNumber = gameState.getInternalState()[moveToSquare].getCurrentNumber();

                    //switch Blank tile with adjacent tile.
                    tempChild.getInternalState()[moveToSquare].setCurrentNumber(0);
                    tempChild.setBlankSquare(moveToSquare);
                    tempChild.getInternalState()[blankSquarePosition].setCurrentNumber(moveToNumber);

                    //recalculations required by cloning and then shifting squares.
                    tempChild.recalculateHeuristics();
                    tempChild.createStateStrings();

                    //make sure we can reconstruct solution path from goal state.
                    tempChild.setParentState(gameState);

                    frontierList.add(tempChild);

                    statistics.addToNodesExpanded();
                    statistics.updateFrontierListLength(frontierList);

                    statistics.setTreeDepth(tempChild.getDepth());
                
                }
                closedList.add(gameState);
                statistics.updateClosedListLength(closedList);
            }
        }

        return null; //no solution found (i.e., error occurred if parity's correct.)
    }

}

