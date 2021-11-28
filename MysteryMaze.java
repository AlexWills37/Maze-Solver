/**
 * MysteryMaze.java
 * 
 * Depth-First Search algorithm for solving a maze, gone awry.
 * Cells are explored based on a priority queue, where exploring a cell adds its neighbors to the front of the queue.
 * This way, the algorithm explores depth before breadth (in theory).
 * 
 * This algorithm is prone to finding a suboptimal path, should there be multiple paths and the longer one is arbitrarily explored first
 * 
 * @author Alex Wills
 */
import java.awt.image.BufferedImage;
import java.util.ArrayList;
public class MysteryMaze extends Maze {

    private ArrayList<MazeCell> queue;

    /**
     * Construct a DFS maze. This is similar to the BFS maze, but now works as a priority queue system
     * instead of a complete cellular automata
     * 
     * @param mazePic BufferedImage containing maze picture
     * @param startX starting X coordinate
     * @param startY starting Y coordinate
     * @param goalX goal X coordinate
     * @param goalY goal Y coordinate
     */
    public MysteryMaze(BufferedImage mazePic, int startX, int startY, int goalX, int goalY){
        super(mazePic, startX, startY, goalX, goalY);

        // Now we have a queue field for updating the maze
        queue = new ArrayList<MazeCell>();
        queue.add(maze[goalY][goalX]);
    }

    /**
     * Create MysteryCells to populate the maze
     * 
     * @param initState the initial state for the mystery cell
     * @return MysteryCell
     */
    protected MazeCell makeCell(MazeState initState){
        return new MysteryCell(initState);
    }
    
    /**
     * Search the next cell in the queue, then add unexplored neighbors to the queue
     * 
     * I think the order cells are added to the queue is responsible for the snake-like behavior, which can
     * be traced to the MysteryCell's searchCell method.
     * 
     * Probably due to incorrectly processing SEARCHING cells vs UNEXPLORED cells
     */
    public void update(){
        // Prioritize the end of the queue so that elements do not have to constantly shift
        int lastIndex = queue.size() - 1;
        MazeCell[] addToQueue = ((MysteryCell)queue.remove(lastIndex)).searchCell();

        // Add other cells to queue now
        //System.out.println("Adding " + addToQueue.length + " Cells to queue");
        for(MazeCell cell : addToQueue){
            queue.add(cell);
        }

        //System.out.println("Queue size: " + queue.size());
    }
}
