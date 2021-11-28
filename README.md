# Maze-Solver
Java maze solving algorithms using Object Oriented Programming

### Input
An image file containing a maze and parameters specifying where the start and end of the maze are

### Output
A new image file with the maze and a solution for the maze, saved in the same directory as the input file

## Algorithms

### Wavefront: Breadth-first search
This algorithm searches a starting cell's neighbors and adds them to a list to be searched. The wavefront is a queue, so all neighbors will be checked before checking the neighbor's neighbors. In other words, the algorithm checks all cells 1 cell from the start, then 2 cells from the start, then 3, and so forth.

### Mystery: An attempted depth-first search
This algorithm searches a starting cell's neighbors, checking the deepest neighbor first. Before checking a cell's last 3 neighbors, it must first check the first neighbor and all of its neighbors.
Presumably due to the order this algorithm checks neighbors, it finds awfully suboptimal solutions, searching in a snake-like pattern and checking far more cells than it needs to check.

## How to run
```
$ javac *.java
$ java MazeSolver <filename> <startX> <startY> <goalX> <goalY> (algorithm) (skipVisualization)
```
### Parameters
- filename (String): the location of the image file containing the maze
- startX (int): X coordinate of the starting pixel in the maze
- startY (int): Y coordinate of the starting pixel in the maze
- goalX (int): X coordinate of the ending pixel in the maze
- goalY (int): Y coordinate of the ending pixel in the maze
- algorithm (String) (optional): name of the algorithm to perform
  - "BFS" - Wavefront / Breadth-First Search (default)
  - "Mystery" - Mystery / Attempted Depth-First Search
- skipVisualization (boolean) (optional): boolean to skip the visualization of the algorithm. Useful for large mazes and suboptimal algorithms
  - false (default) - algorithm will be visualized with each step of the search
  - true - algorithm will not be visualized. Output image will still be displayed and saved

## Examples
Input file

![prim_maze](https://user-images.githubusercontent.com/77563588/143785247-9aff9a14-3f13-46ed-a74f-5e5b12db6b68.png)

Algorithm Visualization (Wavefront)

![Maze Solving in progress](https://user-images.githubusercontent.com/77563588/143785254-b88008c9-2a38-43e5-913f-0b77cc47682c.png)

Solved Maze (Wavefront)

![Finished Maze Solving](https://user-images.githubusercontent.com/77563588/143785255-a4c7b164-b0a7-4fb3-8905-937a8d91f620.png)

Solved Maze (Mystery)

![prim_maze_solved(mystery)](https://user-images.githubusercontent.com/77563588/143785332-2ee96823-a0e9-41ac-a502-11a2d439199d.png)


## Future Work
- Implement more pathfinding algorithms
  - True depth-first search
  - Heuristic algorithms
