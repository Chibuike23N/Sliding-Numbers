import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @Chibuike Nnolim
 * @ID: 7644941
 * @21-10-2024
 *
 * @Class Menu - This class displays a menu for the user to solve the sliding numbers
 * puzzle through the desired search algorithm
 */

public class Menu
{
    public static int[][] initialPuzzleBoard;
    public int dimensions;
    public int maxDepth = 20;
    public int aStarMoveCount = 0;
    public PuzzleNode initialPuzzleNode;
    static public int puzzleCount = 0;
    public Menu()
    {

        File file = new File("puzzle3.txt");//desired puzzle file
        try
        {
            Scanner scan = new Scanner(file);

            dimensions = scan.nextInt();
            initialPuzzleBoard = new int[dimensions][dimensions];

            for(int i = 0; i < dimensions; i++)
            {
                for(int j = 0; j < dimensions; j++)
                {
                    initialPuzzleBoard[i][j] = scan.nextInt();
                }
            }

            for(;;)//loops until the user decides to quit the program
            {
                Scanner menu = new Scanner(System.in);
                System.out.println();
                System.out.println("Enter the desired command.");
                System.out.println("0. Exit Program");
                System.out.println("1. Iterative Deepening Depth-First Search");
                System.out.println("2. A* Search");
                try
                {
                    int input = menu.nextInt();
                    switch (input)
                    {
                        case 0:
                        {
                            System.exit(0);
                        }
                        case 1:
                        {
                            final long startTime = System.nanoTime();
                            initialPuzzleNode = new PuzzleNode(initialPuzzleBoard, 0, 0, null);

                            List<PuzzleNode> goalPuzzles = iterativeDeepeningDepthFirstSearch();
                            if(goalPuzzles == null)
                            {
                                System.out.println("There is no solution for this puzzle (within depth limit).");
                            }
                            else//execute iddfs
                            {
                                PuzzleNode goal = goalPuzzles.get(goalPuzzles.size() - 1);


                                System.out.println("Start State:");
                                for(int i = 0; i < initialPuzzleNode.puzzle.length; i++)
                                {
                                    for(int j = 0; j < initialPuzzleNode.puzzle[i].length; j++)
                                    {
                                        System.out.print(initialPuzzleNode.puzzle[i][j] + " ");
                                    }
                                    System.out.println();
                                }

                                System.out.println();
                                System.out.println("Goal State:");
                                for(int i = 0; i < goal.puzzle.length; i++)
                                {
                                    for(int j = 0; j < goal.puzzle[i].length; j++)
                                    {
                                        System.out.print(goal.puzzle[i][j] + " ");
                                    }
                                    System.out.println();
                                }

                                System.out.println();
                                System.out.println("Moves to make:");
                                int count = 0;
                                for(PuzzleNode p: goalPuzzles)
                                {
                                    System.out.println("Move " + count + ":");
                                    for(int i = 0; i < p.puzzle.length; i++)
                                    {
                                        for(int j = 0; j < p.puzzle[i].length; j++)
                                        {
                                            System.out.print(p.puzzle[i][j] + " ");
                                        }
                                        System.out.println();
                                    }
                                    System.out.println("");
                                    count++;
                                }
                                System.out.println("# of Moves: " + (goalPuzzles.size() - 1));
                                System.out.println();
                                System.out.println("# of Puzzles generated: " + puzzleCount);
                                puzzleCount = 0;
                                final long duration = System.nanoTime() - startTime;
                                System.out.println("");
                                System.out.println("Time elapsed (Nanoseconds): " + duration + "ns");
                            }
                            break;
                        }
                        case 2:
                        {
                            final long startTime = System.nanoTime();
                            initialPuzzleNode = new PuzzleNode(initialPuzzleBoard, 0, 0, null);
                            PuzzleNode goal = aStar();

                            if(goal != null)
                            {

                                System.out.println();
                                System.out.println("Start State: ");
                                for(int i = 0; i < dimensions; i++)
                                {
                                    for(int j = 0; j < dimensions; j++)
                                    {
                                        System.out.print(initialPuzzleBoard[i][j] + " ");
                                    }
                                    System.out.println();
                                }
                                System.out.println();

                                System.out.println("Goal State: ");
                                for(int i = 0; i < dimensions; i++)
                                {
                                    for(int j = 0; j < dimensions; j++)
                                    {
                                        System.out.print(goal.puzzle[i][j] + " ");
                                    }
                                    System.out.println();
                                }
                                System.out.println();

                                System.out.println("Moves: ");
                                displaySolution(goal);
                                System.out.println();
                                System.out.println("# of Moves: " + (aStarMoveCount - 1));
                                System.out.println();
                                System.out.println("# of Puzzles generated: " + puzzleCount);
                                aStarMoveCount = 0;
                                puzzleCount = 0;
                                final long duration = System.nanoTime() - startTime;
                                System.out.println("");
                                System.out.println("Time elapsed (Nanoseconds): " + duration + "ns");
                            }
                            else
                            {
                                System.out.println("There is no solution for the given puzzle.");
                                System.out.println();
                            }
                            break;
                        }
                        default:
                        {
                            System.out.println("Invalid input. Try again");
                            System.out.println();
                            break;
                        }
                    }
                }
                catch (InputMismatchException inputMismatchException)
                {
                    System.out.println("Invalid Input. Try again.");
                    System.out.println();
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found. Exiting program.");
        }

    }

    //IDDFS Search


    /**
     * Method checks if passed puzzle is already inside the passed list
     * -Ended up not using this method because I could not code it properly for it to function properly,
     * used hash sets instead as an alternative
     *
     * @param list - list to be iterated through
     * @param desired - puzzle to be compared
     * @return if duplicate or not - boolean
     */
    public boolean isDuplicate(List<int[][]> list, int[][] desired)
    {
        if(list.size() == 0)return false;
        boolean check = true;
        for(int[][] compare: list)
        {
            check = true;
            for(int i = 0; i < desired.length; i++)
            {
                for(int j = 0; j < desired[i].length; j++)
                {
                    if(compare[i][j] != desired[i][j]) check = false;
                }
            }
            if(check)return true;
        }
        return false;
    }

    /**
     * Method starts the IDDFS recursive algorithm within the maximum depth.
     * @return a list of the path to the solution if there exists one
     */
    public List<PuzzleNode> iterativeDeepeningDepthFirstSearch()
    {
        for(int i = 0; i < maxDepth; i++)
        {
            List<int[][]> seenPuzzles = new ArrayList<>();
            List<PuzzleNode> solution = recurse(i, initialPuzzleNode, seenPuzzles);

            if(solution != null)
            {
                return solution;
            }
        }
        return null;
    }

    /**
     * Method goes through the puzzle tree recursively.
     * @param depth - current depth
     * @param puzzleNode - current state
     * @param seen - list of visited states
     * @return a list of the solution path
     */
    public List<PuzzleNode> recurse(int depth, PuzzleNode puzzleNode, List<int[][]> seen)
    {
        if(puzzleNode.isGoalState)
        {
            List<PuzzleNode> solution = new ArrayList<>();
            solution.add(puzzleNode);
            return solution;
        }
        else if(depth == 0)
        {
            return null;
        }


        puzzleCount += 1;
        List<PuzzleNode> possibleMoves = puzzleNode.retrieveMoves();

        for (PuzzleNode possibleMove : possibleMoves)
        {
            List<PuzzleNode> search = recurse(depth - 1, possibleMove, seen);

            if (search != null)
            {
                List<PuzzleNode> searchPath = new ArrayList<>();
                searchPath.add(puzzleNode);
                for (PuzzleNode p : search) {
                    searchPath.add(p);
                }
                return searchPath;
            }
        }
        return null;
    }

    //A* Search

    /**
     * Method starts the A* search using a priority queue and the heuristic function (Manhattan distance)
     * @return - simply to return back to constructor
     */
    public static PuzzleNode aStar()
    {
        PriorityQueue<PuzzleNode> queue = new PriorityQueue<>();
        Set<int[][]> seenPuzzles = new HashSet<>();
        PuzzleNode initialPuzzleNode = new PuzzleNode(initialPuzzleBoard, 0, manhattanHeuristic(initialPuzzleBoard), null);
        queue.add(initialPuzzleNode);

        while (queue.isEmpty() == false)
        {

            PuzzleNode state = queue.poll();
            if(state.isGoalState)
            {
                return state;
            }

            seenPuzzles.add(state.puzzle);
            puzzleCount += 1;
            List<PuzzleNode> possibleMoves = state.retrieveMoves();

            for(PuzzleNode possibleMove: possibleMoves)
            {
                if(seenPuzzles.contains(possibleMove.puzzle) == false)
                {
                   possibleMove.gn = state.gn + 1;
                   possibleMove.hn = manhattanHeuristic(possibleMove.puzzle);
                   possibleMove.previous = state;
                   queue.add(possibleMove);
                }
            }
        }
        return null;
    }


    /**
     * Method prints out the solution path for the A* algorithm by going the all the parent nodes connected to the goal
     * state
     *
     * @param puzzleNode the puzzle node in the solution path
     */
    public void displaySolution(PuzzleNode puzzleNode)
    {
        if(puzzleNode != null)
        {
            displaySolution(puzzleNode.previous);
            System.out.println("Move " + aStarMoveCount + ":");
            aStarMoveCount += 1;
            for(int i = 0; i < dimensions; i++)
            {
                for(int j = 0; j < dimensions; j++)
                {
                    System.out.print(puzzleNode.puzzle[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();

        }
        return;
    }

    /**
     * This method computes the heuristic function for the current state and returns the distance.
     * @param puzzle - current state
     * @return the distance from the goal state
     */
    public static int manhattanHeuristic(int puzzle[][])
    {
        int manDist = 0;
        for(int x = 0; x < puzzle.length; x++)
        {
            for(int y = 0; y < puzzle[x].length; y++)
            {
                int num = puzzle[x][y];
                if(num != 0)
                {
                    int x2 = (num-1)/puzzle.length;
                    int y2 = (num-1)%puzzle.length;
                    manDist += Math.abs(x-x2) + Math.abs(y-y2);
                }
            }
        }
        return manDist;
    }

    public static void main(String[] args){Menu m = new Menu();}
}