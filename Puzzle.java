import java.util.ArrayList;

/**
 *      Briana Collins brcollins@alaska.edu
 *      Pamela Myers pamyers@alaska.edu
 *
 * creates a Puzzle object made of PuzzleSquares that
 * represents a single 8-puzzle game state.
 * holds useful information for search method implementations.
 */
public class Puzzle implements Cloneable{

    private PuzzleSquare[] internalState;
    private Puzzle parentState = null;
    private int blankSquare;

    private ArrayList<Integer> goalStateString = new ArrayList<>();     //convenience string for calculating internal heuristics

    private int pathCostManhattan;
    private int pathCostMisplaced;
    private int depth = 0;


    /**
    * Constructs a entire 8-puzzle board instance. This instance knows:
    *   1. What position each of its {@code PuzzleSquare}s have relative to each
    *       other.
    *   2. Where the blank square is at all times.
    *   3. What the goal state is.
    *   4. If it is a child of another {@code Puzzle} game state,
    *       it knows who its parent is.
    *   5. If it is a child of another {@code Puzzle} game state,
    *       it knows what its search depth is.
    *   6. It knows what its Manhattan distance is for the current
    *       positioning of numbers on its tiles.
    *   7. It knows what its Misplaced distance is for the current
    *       positioning of numbers on its tiles.
    * @param startSquares an 9-entry array of the individual tiles that constitute the board.
    * @param blankSquarePosition the index postion [0-9] of where the blank square currently is. 
    */
    public Puzzle(PuzzleSquare[] startSquares, int blankSquarePosition)
    {
        internalState = startSquares;
        blankSquare = blankSquarePosition;

        //heuristics are calculated upon construction;  note that
        //when tiles on a Puzzle are moved, the heuristics will need to be recalculated
        pathCostManhattan = calculateManhattanDistance();
        pathCostMisplaced = calculateMisplacedTileDistance();
    }

    public void setBlankSquare(int squarePosition)
    {
        blankSquare = squarePosition;
    }

    public PuzzleSquare getBlankSquare()
    {
        return internalState[blankSquare];
    }

    public PuzzleSquare[] getInternalState() {
        return internalState;
    }

    public void setParentState(Puzzle parent)
    {
        parentState = parent;
        int parentDepth = parent.getDepth();
        depth = parentDepth + 1;
    }

    public Puzzle getParentState() {
        return parentState;
    }

    public int getDepth() {
        return depth;
    }

    /**
     * for use in calculating heuristics; creates a minimal representation of
     * a Puzzle's current gameState
     */
    public void createStateStrings()
    {
        for (int i = 0; i < internalState.length; i++)
        {

            int goal = internalState[i].getRequiredNumber();
            goalStateString.add(goal);
        }
    }

    /**
     * helper method for use in calculating heuristics.
     * since any given position index has a set row and column,
     * it can be used to calculate distance between two positions.
     * @param positionIndex each of the nine tiles on an 8-puzzle have a
     *                      specified positionIndex from [0,8]
     * @return a 2-entry int array representing [row, col]
     */
    private static int[] getGridPosition(int positionIndex)
    {
        int row;
        int col;
        if (positionIndex % 3 == 0)
        {
            col = 0;
        } else if (positionIndex % 3 == 1)
        {
            col = 1;
        } else
        {
            col = 2;
        }

        if (positionIndex < 3)
        {
            row = 0;
        } else if (positionIndex < 6)
        {
            row = 1;
        } else
        {
            row = 2;
        }

        return new int[]{row, col};
    }

    public int getPathCostManhattan() {
        return pathCostManhattan;
    }

    public int getPathCostMisplaced() {
        return pathCostMisplaced;
    }


    /**
     * calculates the Manhattan distance for use in GreedyBestFirst and
     * A* searches
     * @return int result
     */
    private int calculateManhattanDistance()
    {
        int currentDistance = 0;
        for (int i = 0; i < internalState.length; i++)
        {
            int startPosition = internalState[i].getCurrentNumber();
            int goalPosition = internalState[i].getRequiredNumber();

            if( (startPosition != goalPosition) )
            {
                // our current tile's number does not match what its goal number should be;
                // so we need to find the position index where the current tile needs to end up at.
                int goalIndex = 0;
                for(int j = 0; j < goalStateString.size(); j++)
                {
                    if(goalStateString.get(j).equals(startPosition))
                    {
                        goalIndex = j;
                    }
                }

                int[] startGridPos = getGridPosition(i);        // our current position index
                int[] goalGridPos = getGridPosition(goalIndex); // our desired final location index.

                int distance = Math.abs(startGridPos[0]-goalGridPos[0])  //add x distance
                        + Math.abs(startGridPos[1] - goalGridPos[1]);    //add y distance

                currentDistance += distance;
            }
        }
//        System.out.println("Manhattan distance is: " + currentDistance);
        return currentDistance;
    }

    /**
     * Calculates the number of misplaced tiles for use in A* search
     * @return int result
     */
    private int calculateMisplacedTileDistance() {
        int misplacedTiles = 0;
        for(int i = 0; i < internalState.length; i++){
            if(internalState[i].getCurrentNumber() != internalState[i].getRequiredNumber()
                    && internalState[i].getCurrentNumber() != 0){
                misplacedTiles++;
            }
        }
        return misplacedTiles;
    }

    /**
     * creates a deep copy for easy replication and
     * expansion of children nodes during searching.
     * @return a child copy of Puzzle
     */
    public Object clone()
    {
        PuzzleSquare[] newState = new PuzzleSquare[9];
        int blankSquareIndex = blankSquare;
        for (int i = 0; i < newState.length; i++)
        {
            newState[i] = new PuzzleSquare(internalState[i].getCurrentNumber(),
                                            internalState[i].getRequiredNumber(),
                                            internalState[i].getSquarePosition());
        }
        Puzzle puzzle = new Puzzle(newState, blankSquareIndex);
        return puzzle;
    }

    //use after cloning a gameState and shuffling squares around.
    public void recalculateHeuristics()
    {
        pathCostManhattan = calculateManhattanDistance();
        pathCostMisplaced = calculateMisplacedTileDistance();
    }

    /**
     * formats the Puzzle object in nice visual format.  useful for printing
     * ArrayLists of game states.
     * @return ascii drawing of the current state.
     */
    public String toString()
    {
        StringBuilder puzzle = new StringBuilder();
        puzzle.append("\n-------------\n");
        for(int i = 0; i < 9; i++){
            if (internalState[i].getCurrentNumber() != 0) {
                puzzle.append("| " + internalState[i].getCurrentNumber() + " ");
            }
            else
            {
                puzzle.append("|   ");
            }
            if((i + 1) % 3 == 0 && i != 8){
                puzzle.append("|\n");
                puzzle.append("----+---+----\n");
            }
        }
        puzzle.append("|\n");
        puzzle.append("-------------\n");

        return puzzle.toString();
    }

}
