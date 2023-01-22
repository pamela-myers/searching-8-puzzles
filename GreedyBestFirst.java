import java.util.ArrayList;


/**
 *      Briana Collins brcollins@alaska.edu
 *      Pamela Myers pamyers@alaska.edu
 *
 * *  This class is currently a wrapper around the core {@code runSearch} method
 *  in {@code Utility} to call Greedy Best First searching with the
 Manhattan hueristic.
 */
public class GreedyBestFirst {

    private Puzzle currentState;
    private ArrayList<Puzzle> solutionList = new ArrayList<>();
    private Utility statisticsGatherer;
    private boolean saveToDisk = true;


    /**
     * Takes an initial gamestate and finds solution to goal by GreedyBestFirst
     * search.
     * @param startState initial board state; Puzzle objects keep their own reference to goal state
     * @param saveToDisk convenience flag to turn on/off saving search results to disk.
     */
    public GreedyBestFirst(Puzzle startState, boolean saveToDisk)
    {
        currentState = startState;
        statisticsGatherer = new Utility(2);
        this.saveToDisk = saveToDisk;
    }

  
    /**
    Runs the given search method and returns the solution path in 
    an ArrayList, in order of first move to last move.
    */
    public ArrayList<Puzzle> Search()
    {
        solutionList = Utility.runSearch(currentState, statisticsGatherer, saveToDisk);
        return solutionList;
    }
}
