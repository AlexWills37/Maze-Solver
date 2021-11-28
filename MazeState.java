/**
 * MazeState.java
 * 
 * The implementation of the State interface containing the different states a maze cell can be.
 * 
 * @author Alex Wills
 */
public enum MazeState implements State {
    UNEXPLORED,
    SEARCHING,
    EXPLORED,
    WALL,
    SOLUTION;
}
