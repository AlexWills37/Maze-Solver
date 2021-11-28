/**
 * This class displays and saves the solution to a maze, handling file output
 * 
 * To use, create writer and give it a Maze object. Use setSolution() to give the writer the solution to save,
 * createSolution() to generate the BufferedImage with the solution, and saveSolution() to save the solution
 * to the user's files
 * 
 * @author Alex Wills
 */

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;
public class MazeWriter {

    private Maze maze;
    private byte[][] solution;
    private BufferedImage solvedMaze;

    private static final Color wallColor = Color.BLACK;
    private static final Color spaceColor = Color.WHITE;
    private static final Color solutionColor = Color.RED;

    /**
     * Construct a MazeWriter object
     * 
     * @param maze the maze object to have a solution of
     */
    public MazeWriter(Maze maze){
        this.maze = maze;
    }

    /**
     * Setter for the maze's solution once it has been found
     * @param solution byte array containing the solution info for the maze.
     *      0 - empty space
     *      1 - wall
     *      2 - solution path
     */
    public void setSolution(byte[][] solution){
        this.solution = solution;
    }
    
    /**
     * Creates the buffered image containing the solution to the maze
     * @param type the BufferedImage type (same as original BufferedImage type)
     */
    public void createSolutionImage(int type){
        // Create new buffered image

        solvedMaze = new BufferedImage(maze.getWidth(), maze.getHeight(), type);

        Color cellColor;
        int row, col;
        for(row = 0; row < maze.getHeight(); row++){
            for(col = 0; col < maze.getWidth(); col++){

                // Determine color of pixel
                if(solution[row][col] == 0){
                    cellColor = spaceColor;
                } else if (solution[row][col] == 1){
                    cellColor = wallColor;
                } else {
                    cellColor = solutionColor;
                }

                // Set color of pixl
                solvedMaze.setRGB(col, row, cellColor.getRGB());
            }
        }

        // Display image on screen
        ImageFilter.displayImage(solvedMaze, "Maze Solution");
    }

    /**
     * Save the solution image to the same directory as the original image
     * 
     * @param inFileName the name of the original file
     * @return true if the function successfully completes
     */
    public boolean saveSolution(String inFileName){

        // File overhead borrowed from Lab 4 to save image in a
        int period = inFileName.indexOf(".");
        String fileExtension = inFileName.substring(period + 1);
        String outFileName = inFileName.substring(0, period) + "_solved" + "." + fileExtension;
        
        try {
            File outFile = new File(outFileName);
            ImageIO.write(solvedMaze, fileExtension, outFile);
            System.out.println("Solution image saved at: " + outFileName);
        } catch (IOException e) {
            System.err.println( String.format("%s%n", e));
            return false;
        }   

        // Success
        return true;
    }


}
