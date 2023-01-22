import java.util.ArrayList;

/**
 *      Briana Collins brcollins@alaska.edu
 *      Pamela Myers pamyers@alaska.edu
 *
 *  Represents a single tile on an 8-puzzle board; keeps references
 *  to its current state, its goal state, and its position relative to
 *  the entire board.
 */
public class PuzzleSquare {

    private int currentNumber;      // what number is currently upon tile
    private int requiredNumber;     // what number needs to be upon tile
    private int squarePosition;     // where tile is positioned on puzzle board


    //For a given position on a 3x3 board, labeled as:
    //  0 | 1 | 2
    //  --+---+--
    //  3 | 4 | 5
    //  --+---+--
    //  6 | 7 | 8
    // then side-to-side movement indices differ by one, and
    // up-and-down movement indices differ by 3.
    private final int UP = -3;
    private final int DOWN = 3;
    private final int LEFT = -1;
    private final int RIGHT = 1;

    private int[] allowableMoves;

    /**
    * Constructs a single tile of a 8-puzzle board.  
    * @param initialNumber an integer from 0-8 (0 = blank tile), representing what number is currently displayed on the tile.
    * @param goalNumber an integer from 0-8 (0 = blank tile), representing what number needs to be displayed on the tile.
    * @param position an integer from 0-9 that indicates where this
    * tile exists on the 8-puzzle board.  By allowing the PuzzleSquare
    * to know this, and by changing only the {@code currentNumber} displayed on the tile, we can let PuzzleSquare have the knowledge
    * of what moves are available to it relative to its position.  i.e., {@code position} = 3 means the tile can move UP, DOWN, and RIGHT.
    */
    public PuzzleSquare(int initialNumber, int goalNumber, int position)
    {

        currentNumber = initialNumber;
        requiredNumber = goalNumber;
        squarePosition = position;

        // Reference for position numbers:
        //  0 | 1 | 2
        //  --+---+--
        //  3 | 4 | 5
        //  --+---+--
        //  6 | 7 | 8

        //each puzzle square is aware of its position on the board and knows how
        //a blank tile is allowed to move.
        if (position == 0)
        {
            allowableMoves = new int[2];
            allowableMoves[0] = RIGHT;
            allowableMoves[1] = DOWN;

        } else if (position == 1)
        {
            allowableMoves = new int[3];
            allowableMoves[0] = RIGHT;
            allowableMoves[1] = DOWN;
            allowableMoves[2] = LEFT;

        } else if (position == 2)
        {
            allowableMoves = new int[2];
            allowableMoves[0] = LEFT;
            allowableMoves[1] = DOWN;

        } else if (position == 3)
        {
            allowableMoves = new int[3];
            allowableMoves[0] = UP;
            allowableMoves[1] = DOWN;
            allowableMoves[2] = RIGHT;

        } else if (position == 4)
        {
            allowableMoves = new int[4];
            allowableMoves[0] = UP;
            allowableMoves[1] = DOWN;
            allowableMoves[2] = RIGHT;
            allowableMoves[3] = LEFT;

        } else if (position == 5)
        {
            allowableMoves = new int[3];
            allowableMoves[0] = UP;
            allowableMoves[1] = DOWN;
            allowableMoves[2] = LEFT;

        } else if (position == 6)
        {
            allowableMoves = new int[2];
            allowableMoves[0] = RIGHT;
            allowableMoves[1] = UP;

        } else if (position == 7)
        {
            allowableMoves = new int[3];
            allowableMoves[0] = RIGHT;
            allowableMoves[1] = UP;
            allowableMoves[2] = LEFT;

        } else if (position == 8)
        {
            allowableMoves = new int[2];
            allowableMoves[0] = LEFT;
            allowableMoves[1] = UP;
        }
        else
        {
            //there was an error.
        }
    }

    public void setCurrentNumber(int newNumber)
    {
        currentNumber = newNumber;
    }

    public int getCurrentNumber()
    {
        return currentNumber;
    }

    public int getRequiredNumber() {return requiredNumber;}

    public int getSquarePosition() {
        return squarePosition;
    }

    public int[] getMoves()
    {
        return allowableMoves;
    }

    public String toString()
    {
        StringBuilder string = new StringBuilder();
        string.append("This is square position " + squarePosition + "\n");
        string.append("From this position, a block can move, relative to position index: " + "\n");
        for (int i = 0; i < allowableMoves.length; i++)
        {
            string.append("   " + allowableMoves[i] + "\n");
        }
        string.append("Its current number is '" + currentNumber + "'\n");
        string.append("Its goal number is '" + requiredNumber + "'\n");
        return string.toString();
    }

}
