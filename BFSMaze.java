/**
 * Maze class that uses BFS cells to solve a maze with a breadth-first approach
 * 
 * Uses a cellular automata model of updating cells (have every cell determine its next state,
 * then have every cell take on its next state)
 * 
 * Usage: create a Maze object from an image, then repeatedly call update until pathFound() returns true or a limit has been reached
 * 
 * @author Alex Wills
 */
import java.awt.image.*;
public class BFSMaze extends Maze {
    
    /**
     * Constructs a Breadth-First Search Maze
     * 
     * @param mazePic image of the maze
     * @param startX X coordinate of start point
     * @param startY Y coordinate of start point (0 is at the top)
     * @param goalX X coordinate of goal point
     * @param goalY Y coordingat of goal point  (0 is at the top)
     */
    public BFSMaze(BufferedImage mazePic, int startX, int startY, int goalX, int goalY){
        super(mazePic, startX, startY, goalX, goalY);
    }


    /**
     * Update every cell's state by asking each cell to determine their next state,
     * then become that state. Based off of cellular automata
     */
    public void update(){

        // Determine the next states
        for(MazeCell[] row : maze){
            for(MazeCell cell : row){
                cell.updateNextState();
            }
        }

        // Switch to next states
        for(MazeCell[] row : maze){
            for(MazeCell cell : row){
                cell.updateCurrentState();
            }
        }
    }

    /**
     * Create a BFSCell when populating the maze
     * 
     * @param initState the initial state for this maze cell
     * @return a new BFSCell
     */
    protected MazeCell makeCell(MazeState initState){
        return new BFSCell(initState);
    }
}
