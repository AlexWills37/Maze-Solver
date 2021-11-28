import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 *  Defines a visualization tool useful for a Maze solving / pathfinding model, based on the tool from Caitrin Eaton made for
 *  SIR epidemiology modeling <br>
 *  <br>
 *  
 *  <br>
 *  @author Caitrin Eaton, 2021
 *  @author Alex Wills
 */
public class MazeVisImg implements Vis {

    // Dimensions
    private final int windowSize;         //  number of pixels along each side of the square window
    private  int cellSize = 1;            //  number of pixels along each side of each square cell within the window
    private final int width, height;      //  dimensions for the maze
    private long frameTime = 500;         //  duration of each frame (day) in milliseconds

    // Graphics components
    private JFrame window;                 //  the window in which the model's animation is displayed
    private BufferedImage img;             //  the grid of rectangles used to visualize all of the cells

    // SIR components
    private Maze grid;                 //  the grid of references to the actual cell objects themselves
    private Color[][] state;           //  a grid in which each cell's state is represented as an enumerated value

    /**
     * SIRVis constructor, creates an animated visualization for the given grid of cells. <br>
     * @param world SIRGrid, the 2D collection of cells that defines this particular SIR cellular automaton
     */
    public MazeVisImg( Maze world ) {
        // Determine the dimensions of components that will be visualized
        this.grid = world;
        this.width = world.getWidth();
        this.height = world.getHeight();
        //int gridSize = height * width;
        this.windowSize = world.getSize()*cellSize + 20;

        // Configure the graphics window
        this.window = new JFrame();
        this.img = new BufferedImage( width * cellSize, height*cellSize, BufferedImage.TYPE_INT_RGB );
        initWindow();
        //update( 0 );
    }

    /**
     * Set the window's title string for the current frame of the animation
     * @param day (int) current day of the animation
     */
    private void setTitle( int day ){
        
        /**int[] demo = grid.getDemographics();
        int popSize = grid.getSize() * grid.getSize();
        double s = (double) demo[ SIRState.SUSCEPTIBLE.ordinal() ] / popSize * 100;
        double i = (double) demo[ SIRState.INFECTIOUS.ordinal() ] / popSize * 100;
        double r = (double) demo[ SIRState.RECOVERED.ordinal() ] / popSize * 100;
        String title = String.format("Day %d, S: %.2f%%, I: %.2f%%, R: %.2f%%", day, s, i, r);
        this.window.setTitle( title );*/
        this.window.setTitle("Updates: " + day + " White = unexplored, Orange = searching, Blue = explored, Red = solution, Black = wall");
    }

    /**
     * Initialize the graphics window and its contents.
     */
    private void initWindow() {

        // Configure the window itself
        window.setSize( windowSize, windowSize );

        // Center the image in the graphics window
        ImageIcon icon = new ImageIcon( img );
        JLabel label = new JLabel( icon );
        window.add( label );

        // Make the graphics window visible
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        long initial_delay_ms = 2000;
        window.repaint( initial_delay_ms );

        // Hold the initial population steady for 2 seconds before starting the simulation
        try {
            Thread.sleep(initial_delay_ms);
        } catch (InterruptedException e) { }
    }

    /**
     * Control the speed of the animation. <br>
     * @param milliseconds long, the duration of a single frame, in milliseconds
     */
    public void setFrameTime(long milliseconds ){
        this.frameTime = milliseconds;
    }

    /**
     * Update the graphical representation of the grid to reflect the grid's current state.
     * @param day (int) current day in the simulation
     */
    public void update( int day ) {
        // Update the current state map
        this.state = this.grid.getColors();

        // Use the window's title to track population demographics
        setTitle( day );

        // Configure the color of each cell according to its state: occupied or unoccupied
        state = grid.getColors();
        Color c;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++ ){

                c = state[row][col];

                // in case a cell has more than 1 pixel along each edge
                for( int dr=0; dr<cellSize; dr++) {
                    for (int dc = 0; dc < cellSize; dc++) {
                        img.setRGB( col*cellSize+dc, row*cellSize+dr, c.getRGB() );
                    }
                }
            }
        }

        window.repaint( frameTime );
        try {
            Thread.sleep( frameTime );
        } catch (InterruptedException e) { }
    }

    /**
     * Test the SIRVis class by simulating a small population over the course of 1 week. <br>
     * @param args String[] command line arguments
     *
    public static void main(String[] args) {

        // Initialize a SIRGrid object to drive the game mechanics
        int gridSize = 600;
        int simLength = 90;
        double percentInfected = 0.0001; //20.0 /(gridSize*gridSize);
        double percentMasked = 0.50;
        Maze testGrid = new Maze( gridSize );
        testGrid.populate( percentInfected, 0.166, 0.037, percentMasked, 0, 0, 0 );

        // Initialize a SIRVis object to show the user what's going on in the game
        MazeVisImg testVis = new MazeVisImg( testGrid );
        testVis.setFrameTime( 50 );

        // Run the simulation until either no infectious cells remain or the time limit has been reached
        int day = 0;
        while( testGrid.getInfectious() > 0 && day < simLength ){
            day++;
            testGrid.update();
            // System.out.println( testGrid );
            testVis.update( day );
        }
    }*/
}
