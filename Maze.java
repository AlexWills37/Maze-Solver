/**
 * Abstract Maze.java to initialize and solve a maze.
 * Different child classes have different cell types and different algorithms for updating the maze
 * 
 * 
 * To use: Construct a maze with a BufferedImage,
 *  then update() until the maze is solved (pathFound() is true)
 * 
 * @author Alex Wills
 */
import java.awt.Color;
import java.awt.image.*;

public abstract class Maze {

    protected final MazeCell[][] maze;
    private final BufferedImage mazePic;
    private final int size;
    private final int startX, startY;

    // Offsets for the 4 neighbors in the cardinal directions
    protected static final int[][] neighborOffsets = { {0, 1}, {0, -1}, {1, 0}, {-1, 0} }; // E, W, S, N

    /**
     * Constructs a Maze object from an image
     * 
     * @param mazePic a BufferedImage of a maze to solve
     * @param startX the starting point's X coordinate (0 on the left)
     * @param startY the starting point's Y coordinate (0 on the top)
     * @param goalX the goal's X coordinate (0 on the left)
     * @param goalY the goal's Y coordinate (0 on the top)
     */
    public Maze(BufferedImage mazePic, int startX, int startY, int goalX, int goalY){

        // Assign image to image field
        this.mazePic = mazePic;
        maze = new MazeCell[mazePic.getHeight()][mazePic.getWidth()];
        size = mazePic.getHeight() * mazePic.getWidth();

        this.startX = startX;
        this.startY = startY;

        // Create and initialize the maze
        buildMaze(goalX, goalY);
        makeNeighborhoods();
    }

    /**
     * Create a Cell object to serve as a navigatable space.
     * Implement this method based on what cell type non-walls are for the particular algorithm
     * 
     * @param initState initial MazeState of the cell
     * @return the non-wall cell to add to the maze
     */
    protected abstract MazeCell makeCell(MazeState initState);

    /**
     * Update the maze's cells based on the algorithm you are implementing
     */
    public abstract void update();

    /**
     * Initialize the maze field by scanning through the mazePic image, and setting dark colors to be walls, 
     * and light colors to be empty spaces. Searching starts at the goal, searching for the start
     * 
     * @param goalX the X coordinate of the goal
     * @param goalY the Y coordinate of the goal
     */
    private void buildMaze(int goalX, int goalY){

        // Go through the image and use a modified binarize filter to determine walls vs spaces.
        int col, rgb, red, blue, green, intensity;
        int threshold = 127;    // Pixels may not be perfectly black and white. Set threshold between walls/spaces halfway
        Color colorIn;
        for(int row = 0; row < mazePic.getHeight(); row++){
            for(col = 0; col < mazePic.getWidth(); col++){

                // Get pixel value
                rgb = mazePic.getRGB(col, row);
                colorIn = new Color(rgb);
                red = colorIn.getRed();
                blue = colorIn.getBlue();
                green = colorIn.getGreen();

                // Find intensity: sqrt( r^2 + g^2 + b^2) * 255 / 441   (fraction at the end is to scale intensity from 0 to 255)
                intensity = (int)( Math.sqrt( Math.pow(red, 2) + Math.pow(blue, 2) + Math.pow(green, 2) ) * 255 / 441 );

                // Dark colors below threshold will be walls. Other pixels will be spaces
                if(intensity < threshold){
                    maze[row][col] = new Wall();
                } else {
                    maze[row][col] = makeCell(MazeState.UNEXPLORED);    // Abstract mathod for modular design and creating different types of cells
                }
            }
        }
        // Set goal cells as the beginning seaching point
        maze[goalY][goalX].searchCellNext();
    }


    /**
     * Give each cell access to their neighbors in the cardinal directions
     */
    private void makeNeighborhoods(){
        
        int col, neighborRow, neighborCol;

        // Go through every cell and give it its neighbors
        for(int row = 0; row < maze.length; row++){
            for(col = 0; col < maze[0].length; col++){

                // Use the 4 offsets to locate relative neighbors
                for(int[] offset : neighborOffsets){
                    neighborRow = row + offset[0];
                    neighborCol = col + offset[1];

                    // Only add in-bounds neighbors
                    if(neighborRow < maze.length && neighborRow >= 0 && neighborCol < maze[0].length && neighborCol >= 0){

                        maze[row][col].addNeighbor( maze[neighborRow][neighborCol] );
                    }
                }
            }
        }
    }


    /**
     * Returns a 2D representation of the maze with the following key to show the solution
     * 0 - empty space
     * 1 - wall
     * 2 - solution
     * 
     * @param startX the starting X position (0 at the left)
     * @param startY the starting Y position (0 at the top)
     * @return
     */
    public byte[][] getSolution(){

        // Get starting cell
        BFSCell solutionCell = (BFSCell)maze[startY][startX];
        int distance = maze[startY][startX].getDistance();

        // Go through the best distances to flag all solution cells
        for(int i = 0; i < distance; i++){
            solutionCell = solutionCell.nextSolutionStep();
            //System.out.println("Cell: " + solutionCell);
            //System.out.println("\tDistance: " + solutionCell.getDistance());
        }
 
        // Make 2D representation of the maze out of bytes 
        byte[][] solution = new byte[maze.length][maze[0].length];

        int col, row;
        for(row = 0; row < solution.length; row++){
            for(col = 0; col < solution[0].length; col++){

                // Label walls as 1, solution steps as 2, and everything else as 0
                if(maze[row][col].getCurrentState() == MazeState.WALL){
                    solution[row][col] = 1;
                } else if (maze[row][col].getCurrentState() == MazeState.SOLUTION){
                    solution[row][col] = 2;
                } else {
                    solution[row][col] = 0;
                }
            }
        }

        return solution;
    }

    
    /** 
     * Returns an array of colors for the visualization of the maze
     * @return 2D array of Color objects representing the maze's state
     */
    public Color[][] getColors(){
        Color[][] colors = new Color[maze.length][maze[0].length];
        
        // Add every cell's color to its corresponding position
        int col;
        for(int row = 0; row < colors.length; row ++){
            for(col = 0; col < colors[0].length; col++){
                
                colors[row][col] = maze[row][col].getColor();
                
            }
        }
        
        return colors;
    }

    /**
     * Returns true if a path has been found from the start to the goal
     * @return true if the start is EXPLORED, false otherwise
     */
    public boolean pathFound(){
        return (maze[startY][startX].getCurrentState() == MazeState.EXPLORED);
    }
    
    /**
     * Pretty print maze's state for visualization
     * WARNING: Do NOT use this with large mazes
     */
    public String toString(){
        
        String result = "";
        
        for(MazeCell[] row : maze){
            for(MazeCell cell : row){
                if(cell.getCurrentState() == MazeState.WALL){
                    result += "[XX]";
                } else if (cell.getCurrentState() == MazeState.UNEXPLORED){
                    result += "    ";
                } else if (cell.getCurrentState() == MazeState.SEARCHING) {
                    result += "~??~";
                } else if (cell.getCurrentState() == MazeState.SOLUTION){
                    result += "|S |";
                } else {
                    result += String.format(" %-3d", cell.getDistance());
                }
            }
            result += "\n";
        }
        
        result += "----------------------------------------";
        return result;
    }
    
    /**
     * Accessor for the total number of cells in the maze
     * @return size
     */
    public int getSize(){
        return size;
    }

    /**
     * Accessor for the width of the maze
     * @return width of maze
     */
    public int getWidth(){
        return maze[0].length;
    }

    /**
     * Accessor for the height of the maze
     * @return height of maze
     */
    public int getHeight(){
        return maze.length;
    }

}
