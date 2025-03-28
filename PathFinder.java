/*
THIS CODE WAS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
CODE WRITTEN BY OTHER STUDENTS OR COPIED FROM ONLINE RESOURCES. Philip Cardozo
*/

import java.io.*;
import java.util.*;

//Class to represent a position in the maze with row, column, and parent reference
class Position {
    public int i;     //Row index in maze
    public int j;     // Column index in maze
    public Position parent;  //Reference to parent position for path reconstruction

    Position(int x, int y, Position p) {
        i = x;
        j = y;
        parent = p;
    }
}

public class PathFinder {

    public static void main(String[] args) throws IOException {
        //Check for correct command line 
        if (args.length < 1) {
            System.err.println("***Usage: java PathFinder maze_file");
            System.exit(-1);
        }

        //Read and print the initial maze
        char[][] maze = readMaze(args[0]);
        printMaze(maze);

        //Perform stack-based search 
        Position[] path = stackSearch(maze);
        if (path == null) {
            System.out.println("Maze is NOT solvable (no valid path identified in stackSearch).");
        } else {
            System.out.println("stackSearch Solution:");
            printPath(path);
            printMaze(maze);  //Print maze with path marked
        }

        //Reload maze and perform queue-based search
        char[][] maze2 = readMaze(args[0]);
        path = queueSearch(maze2);
        if (path == null) {
            System.out.println("Maze is NOT solvable (no valid path identified in queueSearch).");
        } else {
            System.out.println("queueSearch Solution:");
            printPath(path);
            printMaze(maze2);  //Print maze with path marked
        }
    }

    // Depth-First Search using a stack
    public static Position[] stackSearch(char[][] maze) {
        int n = maze.length;
        boolean[][] visited = new boolean[n][n];  // Track visited positions
        Stack<Position> stack = new Stack<>();    //Stack for DFS
        stack.push(new Position(0, 0, null));     //Start at entrance

        while (!stack.isEmpty()) {
            Position current = stack.pop();        //Get next position to explore
            int i = current.i;
            int j = current.j;

            //Check if we've reached the exit
            if (i == n - 1 && j == n - 1) {
                return buildPath(current, maze);  //Reconstruct and return path
            }

            //Skip already visited positions
            if (visited[i][j]) continue;
            visited[i][j] = true;  //Mark current position as visited

            //Explore neighbors in order: Down, Up, Right, Left
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] dir : directions) {
                int ni = i + dir[0];  // New row
                int nj = j + dir[1];  // New column

                //Check if neighbor is valid and not a wall
                if (ni >= 0 && ni < n && nj >= 0 && nj < n && maze[ni][nj] == '0') {
                    stack.push(new Position(ni, nj, current));  //Add to stack with parent reference
                }
            }
        }
        return null;  //No path found
    }

    //Breadth-First Search using a queue
    public static Position[] queueSearch(char[][] maze) {
        int n = maze.length;
        boolean[][] visited = new boolean[n][n];  //Track visited positions
        Queue<Position> queue = new ArrayDeque<>();  //Queue for BFS
        queue.add(new Position(0, 0, null));        //Start at entrance

        while (!queue.isEmpty()) {
            Position current = queue.poll();        //Get next position to explore
            int i = current.i;
            int j = current.j;

            // Check if we've reached the exit
            if (i == n - 1 && j == n - 1) {
                return buildPath(current, maze);  //Reconstruct and return path
            }

            // Skip already visited positions
            if (visited[i][j]) continue;
            visited[i][j] = true;  // Mark current position as visited

            // Explore neighbors in order: Down, Up, Right, Left
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] dir : directions) {
                int ni = i + dir[0];  //New row
                int nj = j + dir[1];  //New column

                //Check if neighbor is valid and not a wall
                if (ni >= 0 && ni < n && nj >= 0 && nj < n && maze[ni][nj] == '0') {
                    queue.add(new Position(ni, nj, current));  //Add to queue with parent reference
                }
            }
        }
        return null;  //When no path found
    }

    // Reconstructs path from exit to start by following parent references
    private static Position[] buildPath(Position exitPos, char[][] maze) {
        ArrayList<Position> pathList = new ArrayList<>();
        Position current = exitPos;
        
        // Backtrack from exit to start using parent references
        while (current != null) {
            pathList.add(current);
            maze[current.i][current.j] = 'X';  // Mark path in maze
            current = current.parent;
        }
        
        Collections.reverse(pathList);  // Reverse to get start-to-exit order
        return pathList.toArray(new Position[0]);  // Convert to array
    }

    // Prints the path coordinates
    public static void printPath(Position[] path) {
        System.out.print("Path: ");
        for (Position p : path) {
            System.out.print("(" + p.i + "," + p.j + ") ");
        }
        System.out.println();
    }

    // Reads maze from file into 2D char array
    public static char[][] readMaze(String filename) throws IOException {
        char[][] maze;
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream(filename));
        } catch (IOException ex) {
            System.err.println("*** Invalid filename: " + filename);
            return null;
        }

        // Read maze dimensions
        int N = scanner.nextInt();
        scanner.nextLine();
        maze = new char[N][N];
        
        // Read each row of the maze
        int i = 0;
        while (i < N && scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] tokens = line.split("\\s+");
            int j = 0;
            for (; j < tokens.length; j++) {
                maze[i][j] = tokens[j].charAt(0);  // Store each cell value
            }
            // Validate column count
            if (j != N) {
                System.err.println("*** Invalid line: " + i + " has wrong # columns: " + j);
                return null;
            }
            i++;
        }
        // Validate row count
        if (i != N) {
            System.err.println("*** Invalid file: has wrong number of rows: " + i);
            return null;
        }
        return maze;
    }

    // Prints the maze grid
    public static void printMaze(char[][] maze) {
        System.out.println("Maze: ");
        if (maze == null || maze[0] == null) {
            System.err.println("*** Invalid maze array");
            return;
        }

        // Print each cell of the maze
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
