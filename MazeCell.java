/**
 * Cell.java
 * 
 * An interface for a cell to be used in a cellular automata, with Maze solving and pathfinding initially considered for application
 * 
 * Todo: fix type casting in BFSMaze to remove BFS specific methods from this interface?
 * 
 * @author Alex Wills
 */
import java.awt.Color;
public interface MazeCell{

    // A cell in a maze has 4 neighbors, one in each cardinal direction
    final short MAX_NEIGHBORHOOD_SIZE = 4;

    /**
     * To work as a cellular automaton, a cell needs to be able to determine its next state
     * before taking that state on
     */
    void updateNextState();
    
    /**
     * Cells should also be able to take on their next state as their current state in each time step
     */
    void updateCurrentState();

    /**
     * The cellular automaton approach gives cells access to only their 4 neighbors.
     * This method adds a cell to the neighborhood
     * 
     * @param cell the cell to add to the neighborhood
     */
    void addNeighbor(MazeCell cell);

    /**
     * For visualization, every cell is able to return a Color to represent how it should be displayed
     * 
     * @return Color to display the cell as
     */
    Color getColor();

    /**
     * A cell's distance should be publicly readable so that an algorithm can find the shortest path from one point to another
     * 
     * @return the cell's distance from the initially searched cell(s)
     */
    int getDistance();

    /**
     * The cell's current state should be publicly readable so that an algorithm can detect when the goal has been searched
     * 
     * @return the cell's current State
     */
    State getCurrentState();

    /**
     * Returns the cell's next state
     * 
     * @return the cell's next state
     */
    State getNextState();

    /**
     * Set the cell's next state to SEARCHING (to set the initial starting point)
     */
    void searchCellNext();

    /**
     * Returns the cell with the shortest distance and sets its state to be part of the solution
     * @return
     */
    MazeCell nextSolutionStep();
}