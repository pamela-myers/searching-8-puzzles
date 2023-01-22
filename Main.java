import java.util.ArrayList;
import java.util.regex.*;
import java.util.Scanner;

/**
 *      Briana Collins brcollins@alaska.edu
 *      Pamela Myers pamyers@alaska.edu
 *
 */

public class Main
{
    public static void main(String[] args)
    {
        int start[] = new int[9];
        int goal[] = new int[9];
        PuzzleSquare[] gameState = new PuzzleSquare[9];
        for (int i = 0; i < start.length; i++)
        {
            start[i] = 0;
            goal[i] = 0;
        }
        welcome(start);
        do{
            startInput(start);
            printTiles(start, 's');
            goalInput(goal);
            printTiles(goal, 'g');
            if(parityTracker(goal, start))
            {
                int tempBlankRef = 0;
                //create the PuzzleSquare objects
                for(int i = 0; i < start.length; i++)
                {
                    gameState[i] = new PuzzleSquare(start[i], goal[i], i);
                    if (start[i] == 0)
                    {
                        tempBlankRef = i;
                    }
                }
                Puzzle initState = new Puzzle(gameState, tempBlankRef);
                if (Utility.isGoalState(initState))
                {
                    System.out.println("FOUND GOAL!");
                }
                else
                {
                    chooseSearch(goal, start, initState);
                }
            }
        } while(playAgain());
    }

    /**
     * Takes user input to determine which search to be run.
     * @param goal
     * @param start
     */
    public static void chooseSearch(int goal[], int start[], Puzzle initState)
    {
        String input = " ";
        char searchChosen;
        System.out.println("SELECT A SEARCH METHOD:");
        System.out.println("       (a) BREADTH-FIRST SEARCH");
        System.out.println("       (b) GREEDY BREADTH-FIRST SEARCH USING THE MANHATTAN DISTANCE HEURISTIC");
        System.out.println("       (c) A* SEARCH USING THE MISPLACED TILES HEURISTIC");
        System.out.println("       (d) A* SEARCH USING THE MANHATTAN DISTANCE HEURISTIC\n");
        System.out.println("FOR HARDER PUZZLES, SOME SEARCHES MAY TAKE AWHILE. \n" +
                "PLEASE BE PATIENT, ESPECIALLY FOR BREADTH AND MISPLACED A*.\n");
        Scanner keyboard = new Scanner(System.in);
        boolean isValid = false;
        while(!isValid)
        {
            if(!validate(input, "searchChoice"))
            {
                System.out.println("ENTER a, b, c, OR d.\n");
                input = keyboard.nextLine();
            }
            else
                isValid = true;
        }
        searchChosen = input.toLowerCase().charAt(0);

        switch (searchChosen)
        {
            case 'a':
                System.out.println("BREADTH-FIRST SEARCH");
                BreadthFirst breadthFirst = new BreadthFirst(initState, true);
                ArrayList<Puzzle> solutionBreadth = breadthFirst.Search();
                break;
            case 'b':
                System.out.println("GREEDY BREADTH-FIRST SEARCH USING THE MANHATTAN DISTANCE HEURISTIC");
                GreedyBestFirst greedyBestFirst = new GreedyBestFirst(initState, true);
                ArrayList<Puzzle> soluitionGreed = greedyBestFirst.Search();
                break;
            case 'c':
                System.out.println("A* SEARCH USING THE MISPLACED TILES HEURISTIC");
                MisplacedAStar misplacedAStar = new MisplacedAStar(initState, true);
                ArrayList<Puzzle> solutionMisplaced = misplacedAStar.Search();
                break;
            case 'd':
                System.out.println("A* SEARCH USING THE MANHATTAN DISTANCE HEURISTIC");
                ManhattanAStar manhattanAStar = new ManhattanAStar(initState, true);
                ArrayList<Puzzle> solutionManhattan = manhattanAStar.Search();
                break;
        }
    }

    /**
     * Takes user input to determine whether the game will run again or exit.
     * @return boolean result
     */
    public static boolean playAgain()
    {
        String input = " ";
        System.out.println("DO YOU WANT TO PLAY AGAIN (Y/N)?");
        Scanner keyboard = new Scanner(System.in);
        boolean isValid = false;
        while(!isValid){
            input = keyboard.nextLine();
            if(!validate(input, "y/n"))
            {
                System.out.println("ENTER YES OR NO (Y/N).\n");
            }
            else
                isValid = true;
        }
        if(input.substring(0,1).equals("Y") || input.substring(0,1).equals("y"))
            return true;
        return false;
    }

    /**
     * Creates a black board to the screen.
     * @param board
     * @param state
     */
    public static void printTiles(int board[], char state)
    {
        if(state == 's')
            System.out.println("START STATE:");
        else if(state == 'g')
            System.out.println("GOAL STATE:");
        System.out.println("-------------");
        for(int i = 0; i < 9; i++){
            if(board[i] == 0)
                System.out.print("| " + " " + " ");
            else
                System.out.print("| " + board[i] + " ");
            if((i + 1) % 3 == 0 && i != 8){
                System.out.print("|");
                System.out.println();
                System.out.println("----+---+----");
            }
        }
        System.out.print("|");
        System.out.println();
        System.out.println("-------------\n");
    }

    /**
     * Prints the blank board.
     * @param board
     */
    public static void welcome(int board[])
    {
        System.out.println("WELCOME TO THE SEARCHING 8-PUZZLE!");
        printTiles(board, 'b');
    }

    /**
     * Prints the directions of the game.
     * @param board
     */
    public static void userDirections(int board[])
    {
        System.out.println("ENTER THE NUMBERS 1-8 IN ANY ORDER. YOU MUST CHOOSE EXACTLY 3 NUMBERS PER ROW WITH NO SPACING.");
        System.out.println("SELECT THE BLANK SPACE BY ENTERING '0'");
        System.out.println("AFTER INPUTTING THE 3 DIGITS BE SURE TO PRESS ENTER.");

        boolean dup[] = new boolean[9];
        resetDup(dup);
        for(int i = 0; i < 3; i++) {
            System.out.println("ROW " + (i + 1) + ":");
            userInput(board, i * 3, dup);
        }
    }

    /**
     * Takes in the starting board state.
     * @param start
     */
    public static void startInput(int start[])
    {
        System.out.println("PLEASE ENTER THE START STATE.\n");
        userDirections(start);
    }

    /**
     * Takes in the goal board state.
     * @param goal
     */
    public static void goalInput(int goal[])
    {
        System.out.println("PLEASE ENTER THE GOAL STATE.\n");
        userDirections(goal);
    }

    /**
     * Creates flag to decide with regex to use.
     * "validArray" checks to see if the array has any repeated numbers, uses letters,
     * and whether it has more numbers than it is supposed to.
     * "y/n" checks to see if the user wants to continue playing the game,
     * makes sure the first letter the user enters is either "y", "n", "yes", or "no".
     * "searchChoice" determines which search to run,
     * checks see if the user enters anything other than "a", "b", "c", or "d".
     * @param input
     * @param flag
     * @return boolean result
     */
    public static boolean validate(String input, String flag)
    {
        Pattern p;
        if(flag.equals("validArray"))
            p = Pattern.compile("^(?:([0-8])(?!.*\\1))*$");
        else if(flag.equals("y/n"))
            p = Pattern.compile("(?i)([y]|[n]|[yes]|[no])");
        else if(flag.equals("searchChoice"))
            p = Pattern.compile("(?i)([a]|[b]|[c]|[d])");
        else
            p = Pattern.compile("");
        Matcher m = p.matcher(input);
        if (m.matches())
        {
            return true;
        }
        return false;
    }

    /**
     * Takes the user input for row.
     * Takes the sting the user enters and parses it into integers.
     * @param board
     * @param row
     * @param dup
     */
    public static void userInput(int board[], int row, boolean dup[])
    {
        Boolean isValid = false;
        String input = " ";
        String flag = "validArray";
        Scanner keyboard = new Scanner(System.in);
        while(!isValid)
        {
            input = keyboard.nextLine();
            if(input.length() != 3 || !validate(input, flag))
            {
                System.out.println("ENTER EXACTLY 3 NON REPEATING CHARACTERS WITH NO SPACES.");
                System.out.println("YOU MAY ENTER DIGITS 1-8 OR 0 FOR BLANK.");
            }
            else if(dup[Integer.parseInt(input.substring(0, 1))] == true ||
                    dup[Integer.parseInt(input.substring(1, 2))] == true ||
                    dup[Integer.parseInt(input.substring(2, 3))] == true)
            {
                System.out.print("ONE OF THESE VALUES HAS ALREADY BEEN USED. PLEASE ENTER THE ROW AGAIN.\n");
            }
            else
                isValid = true;
        }
        board[row] = Integer.parseInt(input.substring(0, 1));
        dup[Integer.parseInt(input.substring(0, 1))] = true;
        board[row + 1] = Integer.parseInt(input.substring(1, 2));
        dup[Integer.parseInt(input.substring(1, 2))] = true;
        board[row + 2] = Integer.parseInt(input.substring(2, 3));
        dup[Integer.parseInt(input.substring(2, 3))] = true;
    }

    /**
     * Resets the boolean array dup[] to false for all indexes.
     * @param dup
     */
    public static void resetDup(boolean dup[])
    {
        for(int i = 0; i < dup.length; i++)
        {
            dup[i] = false;
        }
    }

    /**
     *  Determines the parity of the 8-puzzles that were chosen.
     *  Determines whether the parities match.
     * @param goal
     * @param start
     * @return parity result
     */
    public static boolean parityTracker(int goal[], int start[])
    {
        int parityGoalCount = 0;
        int parityStartCount = 0;
        for(int i = 0; i < goal.length-1; i++)
        {
            for(int j = i+1; j < goal.length; j++)
            {
                if(goal[i] != 0 && goal[j] != 0)
                {
                    if(goal[i] > goal[j])
                    {
                        parityGoalCount++;
                    }
                }
                if(start[i] != 0 && start[j] != 0)
                {
                    if(start[i] > start[j]){
                        parityStartCount++;
                    }
                }

            }
        }
        if(parityGoalCount%2 == parityStartCount%2)
        {
            System.out.println("THE PARITIES MATCH.");
            return true;
        }
        System.out.println("THE PARITIES DO NOT MATCH. THE GAME HAS BEEN TERMINATED.");
        return false;
    }
}