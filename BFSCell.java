/**
 * BFSCell.java
 * 
 * Maze Cell that uses a breadth-first searching algorithm.
 * To implement, fill a 2D structure with BFSCells and Walls (a child class), initialize a starting point as MazeState.SERACHING,
 * add a cell's 4 neighbors to its neighborhood (for all cells), then call updateNextState() on all the cells, 
 * then call updateCurrentState() on all the cells.
 * 
 * This uses a cellular automata approach to use a breadth-first wavefront algorithm to calculate distance from a starting point.
 * 
 * @author Alex Wills
 */
import java.awt.Color;
public class BFSCell implements MazeCell {

    // Color variables to represent state
    private static final Color unexploredColor = Color.WHITE;
    private static final Color searchingColor = new Color(255, 179, 0);   // This color is orange
    private static final Color exploredColor = new Color(0, 171, 255);      // This color is blue
    private static final Color solutionColor = Color.RED;

    protected State currentState;
    protected State nextState;

    // Cell will have up to 4 neighbors in the cardinal directions
    protected final MazeCell[] neighborhood = new MazeCell[MAX_NEIGHBORHOOD_SIZE];
    protected int totalNeighbors;

    // Distance from the goal
    protected int distance;

    /**
     * Constructs a Breadth-First Search maze cell
     * 
     * @param initState the initial MazeState of the cell (at least 1 cell in the maze must start MazeState.SEARCHING)
     */
    public BFSCell(MazeState initState){
        currentState = initState;
        nextState = initState;
        totalNeighbors = 0;
        distance = 0;
    }

    /**
     * Returns the color to make this cell in a visualization.
     * Unexplored - White
     * Searching - Yellow
     * Explored - Blue
     * Solution - Red
     * 
     * @return Color representing the cell's current status
     */
    public Color getColor(){
        
        // Return the color that corresponds to the cell's MazeState
        if(currentState == MazeState.SOLUTION){
            return solutionColor;
        } else if(currentState == MazeState.SEARCHING){
            return searchingColor;
        } else if(currentState == MazeState.EXPLORED){
            return exploredColor;
        } else {
            return unexploredColor;
        }

    }

    /**
     * Setter method to set a cell to be in the SEARCHING state for easier creation of the maze
     */
    public void searchCellNext(){
        nextState = MazeState.SEARCHING;
        currentState = MazeState.SEARCHING;
    }


    /**
     * Attempts to add a cell to its neighborhood
     * 
     * @param cell one of the 4 cells surrounding this cell
     */
    public void addNeighbor(MazeCell cell){

        if(totalNeighbors < neighborhood.length) {
            neighborhood[totalNeighbors] = (BFSCell) cell;
            totalNeighbors++;
        }
    }
    
    /**
     * Determing the next state of the cell based on neighboring cells.
     * In this breadth-first approach, an unexplored cell will be searched if any neighbors are being searched,
     * and a searching cell will be explored in the next update
     */
    public void updateNextState(){

        // Unexplored cells will be searched if any neighbors have been searched
        if(currentState == MazeState.UNEXPLORED){

            for(int i = 0; i < totalNeighbors; i++){

                // Search cell next turn if a neighbor is being searched
                if(neighborhood[i].getCurrentState() == MazeState.SEARCHING){
                    nextState = MazeState.SEARCHING;
                }

            }

        // To search a cell, caculate its distance (one plus the lowest neighbor)
        } else if (currentState == MazeState.SEARCHING){

            int shortestDistance = -1;

            // Get the shortest distance from the explored neighbors
            for(int i = 0; i < totalNeighbors; i++){
                if(neighborhood[i].getCurrentState() == MazeState.EXPLORED){
                    if(neighborhood[i].getDistance() < shortestDistance || shortestDistance == -1){
                        shortestDistance = neighborhood[i].getDistance();
                    }
                }
            }

            // Add 1 to closest explored neighbor and change state
            // If no neighbors have been explored, this cell's distance will remain 0, because it is the starting point
            distance = shortestDistance + 1;
            nextState = MazeState.EXPLORED;
        }
        // Don't do anything with explored cells
    }

    /**
     * Private setter method for adding cells to the solution, so that cells cannot be added to the solution unless they
     * are visited by nextSolutionStep()
     */
    private void setSolution(){
        this.currentState = MazeState.SOLUTION;
        this.nextState = MazeState.SOLUTION;
    }

    /**
     * Returns the neighboring cell with the smallest distance.
     * Used for finding the solution and moving from one cell to the next
     * 
     * @return the neighbor with the smallest distance
     */
    public BFSCell nextSolutionStep(){

        int shortestDistance = distance;
        MazeCell bestNeighbor = neighborhood[0];

        // Go through all neighbors
        for(int i = 0; i < totalNeighbors; i++){

            // If distance is the shortest so far, set new best neighbor
            if(neighborhood[i].getCurrentState() == MazeState.EXPLORED && neighborhood[i].getDistance() < shortestDistance){
                bestNeighbor = neighborhood[i];
                shortestDistance = bestNeighbor.getDistance();
            }
        }

        ((BFSCell)bestNeighbor).setSolution();

        return (BFSCell)bestNeighbor;
    }


    /**
     * Returns the distance from this cell to the initially searched cell
     * 
     * @return the cell's distance from the initial search point
     */
    public int getDistance(){
        return distance;
    }

    /**
     * updates the cell's current state, based on the previously calculated next state
     */
    public void updateCurrentState(){
        currentState = nextState;
    }

    /**
     * Returns the cell's current state
     * 
     * @return currentState
     */
    public State getCurrentState(){
        return currentState;
    }

    /**
     * Returns the cell's next state
     * 
     * @return nextState
     */
    public State getNextState(){
        return nextState;
    }


    /**
     * Testing method for the BFSCell class
     * 
     * @param args
     */
    public static void main(String[] args){


        BFSCell cell1 = new BFSCell(MazeState.UNEXPLORED);
        BFSCell celln = new BFSCell(MazeState.UNEXPLORED);
        BFSCell cells = new BFSCell(MazeState.UNEXPLORED);
        BFSCell celle = new BFSCell(MazeState.SEARCHING);
        BFSCell cellw = new BFSCell(MazeState.EXPLORED);

        BFSCell[] neighbors = {celln, celle, cells, cellw};

        for(BFSCell cell : neighbors){
            cell1.addNeighbor(cell);
        }

        System.out.println("Cell state: " + cell1.getCurrentState() + ", dist: " + cell1.getDistance());

        for(int i = 0; i < 3; i++){
            cell1.updateNextState();
            cell1.updateCurrentState();
            System.out.println("Cell state: " + cell1.getCurrentState() + ", dist: " + cell1.getDistance());
        }
    }
}
