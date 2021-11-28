/**
 * Wall.java
 * 
 * A cell in the maze solving cellular automata that has limited functionality. Walls return a distance of -1
 * and should not be included in the solution. Walls also do not need to change their status
 * 
 * @author Alex Wills
 */
import java.awt.Color;
public class Wall extends BFSCell {
    
    /**
     * Constructs a wall cell with the WALL state and a distance of -1
     */
    public Wall(){
        super(MazeState.WALL);
        distance = -1;
    }

    /**
     * Overridden getColor method to return the color walls should be depicted as
     * 
     * @return the color black
     */
    public Color getColor(){
        return Color.BLACK;
    }

    /**
     * Overridden method to not execute any logic when updating nextState
     */
    public void updateNextState(){}

    /**
     * Overridden method to not execute any logic when updating currentState
     */
    public void updateCurrentState(){}

    /**
     * Overriden method to not execute any logic when adding neighbors, because neighbors are never used
     */
    public void addNeighbor(){}


}
