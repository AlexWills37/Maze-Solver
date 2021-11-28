/**
 * Compilation:     javac ImageFilter.java
 * Execution:       java ImageFilter [filePath (String)] [filterName (String)] <filterParameter (int)>
 *                      NOTE: Some filters require a parameter, but not all. Leaving the parameter blank will
 *                          use a default parameter of 10.
 *                      NOTE: filePath must be the filePath of an image in the same directory as the program
 *                      NOTE: valid filterNames are "swapRedBlue", "mean", "median", and "binarize"
 * 
 * Creates a filtered version of an inputted image and saves it in the same directory.
 * 
 * Example commandline inputs
 *  java ImageFilter ed.png swapRedBlue
 *      Outputs ed_red_blue_swap.png
 *      Red and blue channels swapped
 * 
 *  java ImageFilter ed.png binarize 120
 *      Outputs ed_binarize_120.png
 *      Sets pixels to black or white if their intensity is below or above 120
 * 
 *  java ImageFilter ed.png mean 7
 *      Outputs ed_mean_7.png
 *      Uses a 7x7 window around each pixel to average the colors together using mean
 * 
 *  java ImageFilter ed.png median 10
 *      Outputs ed_median_10.png
 *      Uses a 10x10 window around each pixel to average the colors together using median
 * 
 *  java ImageFilter ed.png mean
 *      Outputs ed_mean_10.png
 *      If a filter uses a parameter but none is given, it defaults to 10
 * 
 * @author Alex Wills
 * @author Professor Eaton
 */
import java.awt.image.*;
import java.awt.Color;
import javax.imageio.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ImageFilter {



    /**
     * Copy the original image's pixels into the output image.
     * @param original (BufferedImage) the original image
     * @return duplicate (BufferedImage) a new copy of the original image
     */
    public static BufferedImage copy( BufferedImage original ){
        // The output image begins as a blank image that is the same size and type as the original
        BufferedImage duplicate = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        // Iterate over the original, copying each pixel's RGB color over to the new image (the copy)
        int rgb, red, green, blue;
        Color colorIn, colorOut;
        for (int row=0; row<original.getHeight(); row++){
            for (int col=0; col<original.getWidth(); col++){
                rgb = original.getRGB( col, row );
                // Casting the RGB integer to a Color is unnecessary in this case, but has been included here
                // as an example of how you can access the red (R), green (G), and blue (B) channels individually,
                // which will be essential for creating your own filters in the future.
                colorIn = new Color( rgb );
                red = colorIn.getRed();
                green = colorIn.getGreen();
                blue = colorIn.getBlue();
                colorOut = new Color( red, green, blue );
                //System.out.println( String.format("original[%d][%d] = %d, %d, %d", row, col, red, green, blue) );
                duplicate.setRGB( col, row, colorOut.getRGB() );
            }
        }

        // Return a reference to the shiny new copy of the input image
        return duplicate;
    }

    /**
     * Swaps the Red and Blue channels on every pixel
     * 
     * @param original the BufferedImage input file to be filtered
     * @return a reference to a new, filtered BufferedImage
     */
    public static BufferedImage swapRedBlue(BufferedImage original){

        // Destination for new pixels
        BufferedImage filtered = new BufferedImage( original.getWidth(), original.getHeight(), original.getType());

        int rgb, red, green, blue, row, col;
        Color colorIn, colorOut;
        // For every pixel in the image, get the colors, swap red and blue values, and save to new image
        for(row = 0; row < original.getHeight(); row++) {
            for(col = 0; col < original.getWidth(); col++) {
                // Get and change values of original pixel
                rgb = original.getRGB(col, row);
                colorIn = new Color(rgb);
                red = colorIn.getBlue();
                blue = colorIn.getRed();
                green = colorIn.getGreen();

                // Set new pixel in output image
                colorOut = new Color(red, green, blue);
                filtered.setRGB(col, row, colorOut.getRGB());                
            }
        }

        return filtered;
    }

    /**
     * Sets every pixel to either white or black depending on how bright the pixel is
     * 
     * @param original the BufferedImage to filter
     * @param threshold int from 1 to 255 - the threshold to determine if a pixel will be black or white. 
     *                  Lower threshold will make more pixels white, higher will make more pixels black
     * @return the BufferedImage with the binarize filter applied
     */
    public static BufferedImage binarize( BufferedImage original, int threshold) {
        // Destination for new pixels
        BufferedImage filtered = new BufferedImage( original.getWidth(), original.getHeight(), original.getType());

        int row, col, rgb, red, green, blue, intensity;
        Color colorIn;
        Color black = new Color(0, 0, 0);
        Color white = new Color(255, 255, 255);
        // Iterate over every pixel and get colors
        for(row = 0; row < original.getHeight(); row++){
            for(col = 0; col < original.getWidth(); col++){
                rgb = original.getRGB(col, row);
                colorIn = new Color(rgb);
                red = colorIn.getRed();
                blue = colorIn.getBlue();
                green = colorIn.getGreen();

                // Find intensity: sqrt( r^2 + g^2 + b^2) * 255 / 441   (fraction at the end is to scale intensity from 0 to 255)
                intensity = (int)( Math.sqrt( Math.pow(red, 2) + Math.pow(blue, 2) + Math.pow(green, 2) ) * 255 / 441 );

                // If above threshold, set pixel to white. Else black
                if( intensity > threshold ){
                    filtered.setRGB( col, row, white.getRGB() );
                } else {
                    filtered.setRGB( col, row, black.getRGB() );
                }
            }
        }
        // Return filtered image
        return filtered;
        
    }

    /**
     * Calculates and returns the average color in a window of colors
     * @param window a 2D array with Color and maybe also null values
     * @return Color object that is the mean of all colors in the array
     */
    public static Color calcMeanColor(Color[][] window){
       
        int totalSize, totalRed = 0, totalGreen = 0, totalBlue = 0;
        totalSize = window.length * window.length;
        // Iterate through all colors
        for(Color[] row : window){
            for(Color color : row){
                // Skip null values and lower totalSize
                if( color == null ){
                    totalSize--;
                // Add up RGB values
                } else {
                    totalRed += color.getRed();
                    totalBlue += color.getBlue();
                    totalGreen += color.getGreen();
                }
            }
        }
        // Calculate averages
        int avgRed = totalRed / totalSize;
        int avgGreen = totalGreen / totalSize;
        int avgBlue = totalBlue / totalSize;
        
        // Return average color
        Color avgColor = new Color(avgRed, avgGreen, avgBlue);
        return avgColor;
    }

    /**
     * Goes to each pixel and changes the color to the average value of the colors around it,
     * taking the colors of a square selection centered on the pixel to be changed.
     * 
     * @param original the BufferedImage to filter
     * @param windowSize the width of the square window to take the colors of. Must be smaller than the image's width and height
     * @return a BufferedImage with the mean filter applied
     */
    public static BufferedImage meanColor(BufferedImage original, int windowSize){
        
        BufferedImage filtered = new BufferedImage( original.getWidth(), original.getHeight(), original.getType() );
        
        // Error-check to ensure the windowSize is small enough
        if (windowSize < original.getWidth() && windowSize < original.getHeight()){ // Apply filter

            // Initialize sliding window
            Color[][] window = new Color[windowSize][windowSize];
            int offset = windowSize / 2; // How far to offset the window to center it on the current pixel


            // NOTE: (col, row) are output pixels
            //       (windowCol, windowRow) are window indices
            //       (imgCol, imgRow) are input pixels
            int row, col, oldestCol;
            int windowRow, windowCol, imgRow, imgCol, rgb;
            Color colorOut;

            // For every row in the image
            for(row = 0; row < original.getHeight(); row++){ 
                // Initialize sliding window for each row on leftmost pixel
                col = 0;
                oldestCol = 0;
                for(windowRow = 0; windowRow < windowSize; windowRow++){ 
                    for(windowCol = 0; windowCol < windowSize; windowCol++){
                        imgCol = col - offset + windowCol;
                        imgRow = row - offset + windowRow;

                        // Add colors to 2D array, adding null if the color is not in bounds.
                        if( imgRow < original.getHeight() && imgRow >= 0 && imgCol < original.getWidth() && imgCol >= 0){   // Pixel is in bounds
                            rgb = original.getRGB(imgCol, imgRow);
                            window[windowRow][windowCol] = new Color(rgb);
                        } else { 
                            window[windowRow][windowCol] = null;
                        }
                    }                    
                }
                // Find this mean, add to output pixel
                colorOut = calcMeanColor(window);
                filtered.setRGB(col, row, colorOut.getRGB());

                // Now slide across the row with ring buffer. Write over the oldest column of data
                for(col = 1; col < original.getWidth(); col++){
                    imgCol = col - offset + windowSize; // Rightmost pixels will be added

                    // Overwrite column in window
                    for(windowRow = 0; windowRow < windowSize; windowRow++){
                        imgRow = row - offset + windowRow;

                        // Add colors to 2D array, adding null if the color is not in bounds
                        if( imgRow < original.getHeight() && imgRow >= 0 && imgCol < original.getWidth() && imgCol >= 0){   // Pixel is in bounds
                            rgb = original.getRGB(imgCol, imgRow);
                            window[windowRow][oldestCol % windowSize] = new Color(rgb);
                        } else { 
                            window[windowRow][oldestCol % windowSize] = null;
                        }
                    }

                    oldestCol++; // Oldest column is updated

                    // Find mean RGB values
                    colorOut = calcMeanColor(window);
                    // Set output pixel to mean RGB value
                    filtered.setRGB(col, row, colorOut.getRGB());
                }
                // End of row. Reset oldest column
                oldestCol = 0;
            }

        } else { // Window size too large for the image
            System.out.println("ERROR: windowSize is " + windowSize + ", which is too large for image size " + original.getWidth()
                + " x " + original.getHeight());
        }

        // Return the filtered image
        return filtered;
    }

    /**
     * Calculates the median color value of a 2D array of Color values
     * 
     * @param window 2D array of Colors
     * @return the median Color of the Colors
     */
    public static Color calcMedianColor(Color[][] window){

        int windowWidth = window.length;
        // Process 2D array into 1D array for each color channel, counting nulls
        int[] reds = new int[windowWidth * windowWidth]; 
        int[] greens = new int[windowWidth * windowWidth];
        int[] blues = new int[windowWidth * windowWidth];
        int nulls = 0, index = 0;

        for(Color[] row : window){
            for(Color pixel : row){
                if(pixel == null){  // -1 Values will not be considered in the median
                    reds[index] = -1;
                    blues[index] = -1;
                    greens[index] = -1;
                    nulls++;
                } else {
                    reds[index] = pixel.getRed();
                    blues[index] = pixel.getBlue();
                    greens[index] = pixel.getGreen();
                }
                index++;
            }
        }

        // Sort arrays
        Arrays.sort(reds);
        Arrays.sort(blues);
        Arrays.sort(greens);

        // Find median index (-1 values to ignore will be at beginning of arrays)
        // length - nulls = length to actually consider. + nulls to offset from -1 values
        // if even number, median takes the higher middle value (instead of averaging it)
        int medianIdx = ((reds.length - nulls) / 2) + nulls;

        Color colorOut = new Color(reds[medianIdx], greens[medianIdx], blues[medianIdx]);
        
        return colorOut;
    }

    /**
     * Goes to each pixel and takes a sample of the pixels around it, changing the pixel's color to the median
     * color value of the pixels around it.
     * 
     * @param original the BufferedImage to be filtered
     * @param windowSize the width of the sliding window to take samples of. Must be smaller than the image's width and height.
     * @return a BufferedImage with the medianColor filter applied over the original image
     */
    public static BufferedImage medianColor(BufferedImage original, int windowSize){

        BufferedImage filtered = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        // Ensure window size is smaller than the photo
        if( windowSize < original.getWidth() && windowSize < original.getHeight() ){
            // Initialize sliding window
            Color[][] window = new Color[windowSize][windowSize];
            int offset = windowSize / 2; // How far to offset the window to center it on the current pixel


            // NOTE: (col, row) are output pixels
            //       (windowCol, windowRow) are window indices
            //       (imgCol, imgRow) are input pixels
            int row, col, oldestCol;
            int windowRow, windowCol, imgRow, imgCol, rgb;
            Color colorOut;

            // For every row in the image
            for(row = 0; row < original.getHeight(); row++){ 
                // Initialize sliding window for each row on leftmost pixel
                col = 0;
                oldestCol = 0;
                for(windowRow = 0; windowRow < windowSize; windowRow++){ 
                    for(windowCol = 0; windowCol < windowSize; windowCol++){
                        imgCol = col - offset + windowCol;
                        imgRow = row - offset + windowRow;

                        // Add colors to 2D array, adding null if the color is not in bounds.
                        if( imgRow < original.getHeight() && imgRow >= 0 && imgCol < original.getWidth() && imgCol >= 0){   // Pixel is in bounds
                            rgb = original.getRGB(imgCol, imgRow);
                            window[windowRow][windowCol] = new Color(rgb);
                        } else { 
                            window[windowRow][windowCol] = null;
                        }
                    }                    
                }
                // Find this mean, add to output pixel
                colorOut = calcMeanColor(window);
                filtered.setRGB(col, row, colorOut.getRGB());

                // Now slide across the row with ring buffer. Write over the oldest column of data
                for(col = 1; col < original.getWidth(); col++){
                    imgCol = col - offset + windowSize; // Rightmost pixels will be added

                    // Overwrite column in window
                    for(windowRow = 0; windowRow < windowSize; windowRow++){
                        imgRow = row - offset + windowRow;

                        // Add colors to 2D array, adding null if the color is not in bounds
                        if( imgRow < original.getHeight() && imgRow >= 0 && imgCol < original.getWidth() && imgCol >= 0){   // Pixel is in bounds
                            rgb = original.getRGB(imgCol, imgRow);
                            window[windowRow][oldestCol % windowSize] = new Color(rgb);
                        } else { 
                            window[windowRow][oldestCol % windowSize] = null;
                        }
                    }

                    oldestCol++; // Oldest column is updated

                    // Find median RGB values
                    colorOut = calcMedianColor(window);
                    // Set output pixel to mean RGB value
                    filtered.setRGB(col, row, colorOut.getRGB());
                }
                // End of row. Reset oldest column
                oldestCol = 0;
            }
        } else {
            System.out.println("ERROR: windowSize is " + windowSize + ", which is too large for image size " + original.getWidth()
                + " x " + original.getHeight());
        }     

        // Return filtered image
        return filtered;
    }


    /**
     * NOTE: Function made by Professor Caitrin Eaton
     * Make the image visible on the screen, in its own window.
     * @param img (BufferedImage) the image to display
     * @param title (String) the title and caption of the image
     * @return JFrame in which the image is displayed
     */
    public static JFrame displayImage( BufferedImage img, String title ){
        // Create the graphics window
        JFrame window = new JFrame();
        window.setTitle( title );
        window.setSize( img.getWidth()+20, img.getHeight()+40 );

        // Center the image in the graphics window
        ImageIcon icon = new ImageIcon( img );
        JLabel label = new JLabel( icon );
        window.add( label );

        // Make the graphics window visible until the user closes it (which also ends the program)
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        // Return a reference to the display window, so that we can manipulate it in the future, if we like.
        return window;
    }

    /**
     * Reads the input file image, filters it, and saves the filtered image to
     * the same directory
     * @param inFileName the path to the input file
     * @param filterName the name of the filter to apply
     * @param filterParameter the parameter of the filter
     * @return true if the function succeeds, false if an exception is thrown
     */
    public static boolean filterImage( String inFileName, String filterName, int filterParameter){
        
        // Read in the original input image
        BufferedImage original = null;
        try{
            original = ImageIO.read(new File(inFileName));
        } catch (IOException e){
            System.err.println(String.format("%s%n", e));
            return false;
        }

        // Determine which filter to use, and apply the filter
        BufferedImage filtered;
        String nameAddition;
        if (filterName.equalsIgnoreCase("swapRedBlue")){
            filtered = swapRedBlue(original);
            nameAddition = "_red_blue_swap";
        } else if (filterName.equalsIgnoreCase("binarize")){
            filtered = binarize(original, filterParameter);
            nameAddition = "_binarize_" + filterParameter;
        } else if (filterName.equalsIgnoreCase("mean") || filterName.equalsIgnoreCase("meanColor")){
            filtered = meanColor(original, filterParameter);
            nameAddition = "_mean_" + filterParameter;
        } else if (filterName.equalsIgnoreCase("median") || filterName.equalsIgnoreCase("medianColor")){
            filtered = medianColor(original, filterParameter);
            nameAddition = "_median_" + filterParameter;
        } else if (filterName.equals("")){
            System.out.println("ERROR: No filter name provided");
            return false;
        } else {
            System.out.println("ERROR: " + filterName + " is not a valid filter name");
            return false;
        }

        // Save the new image in a new file
        int period = inFileName.indexOf(".");
        String fileExtension = inFileName.substring(period + 1);
        String filteredFileName = inFileName.substring(0, period) + nameAddition + "." + fileExtension;
        try {
            File filteredFile = new File(filteredFileName);
            ImageIO.write(filtered, fileExtension, filteredFile);
        } catch (IOException e) {
            System.err.println( String.format("%s%n", e));
            return false;
        }
        
        // Success!! The filtered image has been saved.
        return true;
    }

    public static void main(String[] args) {
        
        String usageStatement = "USAGE: java ImageFilter filePath filterName filterParameter"
                + "\nFor example:"
                + "\n\tjava ImageFilter ncf.png mean 5"
                + "\nThe image's file extension must be PNG, JPEG, or JPG"
                + "\nAcceptable filterNames are [binarize], [swapRedBlue], [mean], and [median]";

        String inFileName, filterName;
        int filterParameter;
        boolean success = false;
        
        if (args.length < 1) { // No commandline arguments
            System.out.println("ERROR: No command line arguments given");
        } else if (args.length == 1) {
            System.out.println("ERROR: Only 1 commandline argument given");
        } else if (args.length == 2) {
            // No filter parameter input. Default to 10
            inFileName = args[0];
            filterName = args[1];
            filterParameter = 10;
            success = filterImage(inFileName, filterName, filterParameter);
        } else {
            inFileName = args[0];
            filterName = args[1];
            filterParameter = Integer.parseInt(args[2]);

            // Check for invalid parameter
            if (filterParameter <= 0){
                System.out.println("ERROR: filterParameter must be greater than 0");
            } else {
                success = filterImage(inFileName, filterName, filterParameter);
            }
        }

        if (success) {
            System.out.println("Program completed! Filtered image can be found in the directory of this program.");
        } else {
            System.out.println("Program ended without success.");
            System.out.println(usageStatement);
        }    
        
    }
    
}