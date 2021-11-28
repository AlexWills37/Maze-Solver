/**
 * MazeSolver.java
 * 
 * Compile:     javac MazeSolver.java
 * Execution:   java MazeSolver <filename> <startX> <startY> <goalX> <goalY> (algorithm) (skipVisualization)
 *      filename - String - filepath to get to the image of the maze you'd like to solve
 *      startX - int - X coordinate of the starting point in the maze
 *      startY - int - Y coordinate of the starting point in the maze
 *      goalX - int - X coordinate of the goal of the maze
 *      goalY - int - Y coordinate of the goal of the maze
 *      (algorithm) - String - optional - the name of the algorithm you are using
 *          Options: BFS, Mystery
 *              BFS - Breadth-First Search - default value
 *              Mystery - quetsionable attempt at Depth-First Search that yields interesting results
 *                  (I do not know for sure if it is a depth-first search, but it does not seem like it)
 *      (skipVisualization) - boolean - optional - true if you would like to skip the step-by-step animation of the algorithm
 *          Options: true, false (default)
 *          This option is useful for large images that take a really long time to complete
 *      
 *      NOTE: Coordinates (0, 0) are at the top left corner of the image, and every unit is a pixel
 * 
 *      Ex: java MazeSolver Mazes/westworld.jpg 511 424 399 390
 *          java MazeSolver Mazes/prim_maze.png 26 16 470 488 BFS
 *          java MazeSolver Mazes/westworld.jpg 511 424 399 390 Mystery true
 * 
 * Output:
 *      The program will visualize a pathfinding algorithm based on your selection, then display the solution and save the solution
 *      at the same directory as "filename_solved."
 * 
 * @author Alex Wills 
 */
import java.io.IOException;
import java.awt.image.*;
import javax.imageio.*;
import java.io.File;

public class MazeSolver {
    
    // non-static fields
    private final Maze maze;
    private final MazeVisImg vis;
    private BufferedImage mazePic;
    private final int startX, startY, goalX, goalY;
    private int numUpdates;
    private boolean skipVis;
    
    private final MazeWriter writer;
    private final String filename;

    private final int maxUpdates;

    /**
     * Constructs a MazeSolver object to solve a maze
     * 
     * @param startX starting X coordinate
     * @param startY starting Y coordinate
     * @param goalX goal X coordinate
     * @param goalY goal Y coordinate
     * @param filename name of input file (should be an image of a maze)
     * @param algorithm name of algorithm to execute (DFS, Mystery)
     * @param skipVis boolean where, if true, the path-finding will not be animated (useful for large images)
     */
    public MazeSolver(int startX, int startY, int goalX, int goalY, String filename, String algorithm, boolean skipVis){
        
        // Assign fields
        this.startX = startX;
        this.startY = startY;
        this.goalX = goalX;
        this.goalY = goalY;
        this.numUpdates = 0;
        this.filename = filename;
        this.skipVis = skipVis;

        // Create maze
        maze = createMaze(filename, algorithm);

        // Set number of pixels in image to limit for solving
        maxUpdates = maze.getSize();

        // Create Vis and Writer
        this.vis = new MazeVisImg( maze );
        this.vis.setFrameTime(10);

        this.writer = new MazeWriter(maze);

    }

    /**
     * Create the maze object based on the algorithm
     *  
     * @param filename name of the input image file of a maze
     * @param algorithm name of the algorithm to use (DFS, Mystery)
     * @return Maze object of a specific type
     */
    private Maze createMaze(String filename, String algorithm){

        Maze maze = null;

        // Go to filepath and read the image 
        mazePic = null;
        try{
            mazePic = ImageIO.read(new File(filename));
        } catch (IOException e){
            System.err.println(String.format("%s%n", e));
        }

        // Validate algorithm choice
        if(algorithm.equalsIgnoreCase("BFS")){
            maze = new BFSMaze(mazePic, startX, startY, goalX, goalY);

        } else if(algorithm.equalsIgnoreCase("Mystery")) {
            maze = new MysteryMaze(mazePic, startX, startY, goalX, goalY);

        } else {
            System.out.println("ERROR: Invalid algorithm given, could not create maze");
        }

        return maze;
    }

    /**
     * Run the maze's pathfinding algorithm until a path from the goal to the start has been found
     */
    public void solveMaze(){
        // If skipVis is true, do not visualize in the update loop
        if(skipVis){
            // Until the solution is found or a max limit is reached, update the maze
            while( !(maze.pathFound()) && numUpdates < maxUpdates ) {
                maze.update();
                numUpdates++;
            }

        // If skipVis is false, visualize the maze each update
        } else {
            while( !(maze.pathFound()) && numUpdates < maxUpdates ) {
                maze.update();
                vis.update(numUpdates);
                numUpdates++;
            }
        }
    }

    /**
     * Flag the solution cells, display solution, and save image to files
     */
    public void makeSolution(){
        byte[][] solution = maze.getSolution();

        // Give solution to writer
        writer.setSolution(solution);

        vis.update(numUpdates);

        // Have writer display final solution and save image
        writer.createSolutionImage(mazePic.getType());
        writer.saveSolution(filename); 
    }




    /**
     * Main method to run the Maze Solver program. See file header for commandline argument information
     * 
     * @param args commandline arguments
     */
    public static void main(String[] args){

        // Default commandline parameters for testing
        int startX = 0;
        int startY = 0;
        int goalX = 42;
        int goalY = 71;
        String filename = "Mazes/bigtest.png";
        String algorithm = "BFS";
        boolean skipVisualization = false;

        String usageStatement = "USAGE: java MazeSolver filepath start_x start_y goal_x goal_y (algorithm) (skipVisualization)"
            + "\nFor example:"
            + "\n\tjava MazeSolver Mazes/westworld.jpg 511 424 399 390 BFS"
            + "\nThe Image's file extension should be PNG, JPG, or JPEG";

        // Get and parse commandline arguments
        if(args.length < 5){
            System.out.println("ERROR: 5 commandline arguments required but only " + args.length + " given.");
            System.out.println(usageStatement);
        } else if (args.length > 4){
            filename = args[0];
            startX = Integer.parseInt(args[1]);
            startY = Integer.parseInt(args[2]);
            goalX = Integer.parseInt(args[3]);
            goalY = Integer.parseInt(args[4]);

            if(args.length > 5){
                algorithm = args[5];
            }

            if(args.length > 6){
                skipVisualization = Boolean.parseBoolean(args[6]);
            }

        }
        
        // Create maze and solve
        MazeSolver solver = new MazeSolver(startX, startY, goalX, goalY, filename, algorithm, skipVisualization);
        solver.solveMaze();
        solver.makeSolution();
    }
}
