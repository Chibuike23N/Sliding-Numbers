import java.util.*;


/**
 * @Chibuike Nnolim
 * ID: 7644941
 * 21-10-2024
 *
 * @Class PuzzleNode - This class creates a puzzle node that holds the current state
 * of a puzzle solving algorithm (possible move)
 */

public class PuzzleNode implements Comparable<PuzzleNode>
{
    private int emptyRow;
    private int emptyColumn;
    public int puzzle[][];
    public int dimensions;
    int possibleRowMoves[] = {-1, 1, 0, 0};
    int possibleColumnMoves[] = {0, 0, -1, 1};
    public boolean isGoalState;

    //A* Search
    public int gn = 0;
    public int hn = 0;
    public PuzzleNode previous;

    /**
     *
     * @param startPuzzle - the initial puzzle board
     * @param g - g cost
     * @param h - h cost
     * @param previous - parent puzzle node
     */
    public PuzzleNode(int startPuzzle[][], int g, int h, PuzzleNode previous)
    {
        puzzle = startPuzzle;
        checkGoalState();

        dimensions = startPuzzle.length;
        for (int i = 0; i < dimensions; i++)
        {
            for (int j = 0; j < dimensions; j++)
            {
                if (puzzle[i][j] == 0)
                {
                    emptyRow = i;
                    emptyColumn = j;
                    return;
                }
            }
        }

    }

    /**
     * Method checks of the desired move is valid
     * @param row
     * @param column
     * @return valid move or not - boolean
     */
    public boolean validMoveCheck(int row, int column)
    {
        if((row >= 0 && row < dimensions) && (column >= 0 && column < dimensions)) return true;
        else return false;
    }

    public void swap(int puzzle[][], int row1, int column1, int row2, int column2)
    {
        int a = puzzle[row1][column1];
        puzzle[row1][column1] = puzzle[row2][column2];
        puzzle[row2][column2] = a;
    }

    /**
     * Method generates and retrieves a list of all possible moves that can be done for the current state
     * @return a list of possible moves
     */
    public List<PuzzleNode> retrieveMoves()
    {
        ArrayList<PuzzleNode> moves = new ArrayList<>();
        int copy[][];
        for(int i = 0; i < possibleColumnMoves.length; i++)
        {
            int rowMove = emptyRow + possibleRowMoves[i];
            int columnMove = emptyColumn + possibleColumnMoves[i];

            if(validMoveCheck(rowMove,columnMove))
            {
                copy = new int[dimensions][dimensions];
                for(int x = 0; x < dimensions; x++)
                {
                    for(int y = 0; y < dimensions; y++)
                    {
                        copy[x][y] = puzzle[x][y];
                    }
                }
                swap(copy, emptyRow, emptyColumn, rowMove, columnMove);

                PuzzleNode newPuzzle = new PuzzleNode(copy, gn, hn, this);
                moves.add(newPuzzle);
            }
        }
        int a = 0;
        return moves;
    }

    /**
     * Method checks to see if the current puzzle node is at the goal state
     */
    public void checkGoalState()
    {
        int goalVal = 1;
        for(int i = 0; i < puzzle.length; i++)
        {
            for(int j = 0; j < puzzle.length; j++)
            {
                if(i == puzzle.length - 1 && j == puzzle.length - 1)
                {
                    if(puzzle[i][j] == 0)
                    {
                        isGoalState = true;
                        return;
                    }
                }
                else if(puzzle[i][j] != goalVal)return;
                goalVal += 1;
            }
        }
        isGoalState = true;
    }

    /**
     * Method returns the total cost of the current puzzle node
     * @return
     */
    public int fn()
    {
        int fn = gn + hn;
        return fn;
    }

    /**
     *
     * @param p the object to be compared.
     * @return
     */
    @Override
    public int compareTo(PuzzleNode p) {
        return Integer.compare(this.fn(),p.fn());
    }
}
