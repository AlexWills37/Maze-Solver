/**
 * Pre-successful attempt at a Depth-First search algorithm that has an interesting snake-like behavior that
 * finds a horribly sub-optimal path from the start to the goal
 * 
 * @author Alex Wills
 */
public class MysteryCell extends BFSCell{

    /**
     * Construct a MysteryCell
     * 
     * @param initState the initial MazeState for this cell
     */
    public MysteryCell(MazeState initState){
        super(initState);
    }

    /**
     * Searches this cell and returns references to unexplored neighbors to add to the queue
     * 
     * @return An array of neighbors to add to the search queue
     */
    public MazeCell[] searchCell(){
        
        // Determine distance of cell by comparing distances of neighbors
        int shortestDistance = -1;
        int numUnexploredNeighbors = 0;

        int i;
        // Get the shortest distance from the explored neighbors
        for(i = 0; i < totalNeighbors; i++){
            if(neighborhood[i].getCurrentState() == MazeState.EXPLORED){
                if(neighborhood[i].getDistance() < shortestDistance || shortestDistance == -1){
                    shortestDistance = neighborhood[i].getDistance();
                }

            // Count unexplored neighbors
            } else if(neighborhood[i].getCurrentState() == MazeState.UNEXPLORED){
                numUnexploredNeighbors++;
            }
        }

        // Add 1 to closest explored neighbor and change state
        // If no neighbors have been explored, this cell's distance will remain 0, because it is the starting point
        distance = shortestDistance + 1;
        currentState = MazeState.EXPLORED;

        // Create array of unexplored neighbors and add to an array
        MazeCell[] searchNext = new MazeCell[numUnexploredNeighbors];
        for(i = 0; i < totalNeighbors; i++){

            // If neighbor is unexplored, set it to SEARCHING and add it to array
            if(neighborhood[i].getCurrentState() == MazeState.UNEXPLORED){
                neighborhood[i].searchCellNext();
                searchNext[numUnexploredNeighbors - 1] = neighborhood[i];
                numUnexploredNeighbors--;
            }
        }

        return searchNext;
    }

    
}
