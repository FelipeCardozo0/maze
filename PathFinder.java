import java.io.*;
import java.util.*;

class Position {
    public int i;
    public int j;
    public Position parent;

    Position(int x, int y, Position p) {
        i = x;
        j = y;
        parent = p;
    }
}

public class PathFinder {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("***Usage: java PathFinder maze_file");
            System.exit(-1);
        }

        char[][] maze;
        maze = readMaze(args[0]);
        printMaze(maze);
        Position[] path = stackSearch(maze);
        if (path == null) {
            System.out.println("Maze is NOT solvable (no valid path identified in stackSearch).");
        } else {
            System.out.println("stackSearch Solution:");
            printPath(path);
            printMaze(maze);
        }

        char[][] maze2 = readMaze(args[0]);
        path = queueSearch(maze2);
        if (path == null) {
            System.out.println("Maze is NOT solvable (no valid path identified in queueSearch).");
        } else {
            System.out.println("queueSearch Solution:");
            printPath(path);
            printMaze(maze2);
        }
    }

    public static Position[] stackSearch(char[][] maze) {
        int n = maze.length;
        boolean[][] visited = new boolean[n][n];
        Stack<Position> stack = new Stack<>();
        stack.push(new Position(0, 0, null));

        while (!stack.isEmpty()) {
            Position current = stack.pop();
            int i = current.i;
            int j = current.j;

            if (i == n - 1 && j == n - 1) {
                return buildPath(current, maze);
            }

            if (visited[i][j]) continue;
            visited[i][j] = true;

            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] dir : directions) {
                int ni = i + dir[0];
                int nj = j + dir[1];
                if (ni >= 0 && ni < n && nj >= 0 && nj < n && maze[ni][nj] == '0') {
                    stack.push(new Position(ni, nj, current));
                }
            }
        }
        return null;
    }

    public static Position[] queueSearch(char[][] maze) {
        int n = maze.length;
        boolean[][] visited = new boolean[n][n];
        Queue<Position> queue = new ArrayDeque<>();
        queue.add(new Position(0, 0, null));

        while (!queue.isEmpty()) {
            Position current = queue.poll();
            int i = current.i;
            int j = current.j;

            if (i == n - 1 && j == n - 1) {
                return buildPath(current, maze);
            }

            if (visited[i][j]) continue;
            visited[i][j] = true;

            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] dir : directions) {
                int ni = i + dir[0];
                int nj = j + dir[1];
                if (ni >= 0 && ni < n && nj >= 0 && nj < n && maze[ni][nj] == '0') {
                    queue.add(new Position(ni, nj, current));
                }
            }
        }
        return null;
    }

    private static Position[] buildPath(Position exitPos, char[][] maze) {
        ArrayList<Position> pathList = new ArrayList<>();
        Position current = exitPos;
        while (current != null) {
            pathList.add(current);
            maze[current.i][current.j] = 'X';
            current = current.parent;
        }
        Collections.reverse(pathList);
        return pathList.toArray(new Position[0]);
    }

    public static void printPath(Position[] path) {
        System.out.print("Path: ");
        for (Position p : path) {
            System.out.print("(" + p.i + "," + p.j + ") ");
        }
        System.out.println();
    }

    public static char[][] readMaze(String filename) throws IOException {
        char[][] maze;
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream(filename));
        } catch (IOException ex) {
            System.err.println("*** Invalid filename: " + filename);
            return null;
        }

        int N = scanner.nextInt();
        scanner.nextLine();
        maze = new char[N][N];
        int i = 0;
        while (i < N && scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] tokens = line.split("\\s+");
            int j = 0;
            for (; j < tokens.length; j++) {
                maze[i][j] = tokens[j].charAt(0);
            }
            if (j != N) {
                System.err.println("*** Invalid line: " + i + " has wrong # columns: " + j);
                return null;
            }
            i++;
        }
        if (i != N) {
            System.err.println("*** Invalid file: has wrong number of rows: " + i);
            return null;
        }
        return maze;
    }

    public static void printMaze(char[][] maze) {
        System.out.println("Maze: ");
        if (maze == null || maze[0] == null) {
            System.err.println("*** Invalid maze array");
            return;
        }

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
