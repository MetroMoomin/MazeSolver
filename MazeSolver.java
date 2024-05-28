import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MazeSolver {
    private int[][] maze;
    private int rows;
    private int cols;
    private int startRow;
    private int startCol;
    private int finishRow;
    private int finishCol;
    private int[][] path;
    private boolean[][] visited;

    private void LoadMaze(String filePath) 
    {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            rows = 0;
            String line;
            LinkedList<String> lines = new LinkedList<>();
    
            while ((line = br.readLine()) != null) 
            {
                lines.add(line);
                rows++;
            }
    
            cols = lines.get(0).replaceAll("\\s", "").length();
            maze = new int[rows][cols];
            visited = new boolean[rows][cols];
    
            for (int i = 0; i < rows; i++) 
            {
                String currentLine = lines.get(i).replaceAll("\\s", "");
                for (int j = 0; j < cols; j++) 
                {
                    char cell = currentLine.charAt(j);
                    switch (cell) {
                        case 'W':
                            maze[i][j] = -1; // Wall
                            break;
                        case 'C':
                            maze[i][j] = 0; // Corridor
                            break;
                        case 'S':
                            maze[i][j] = 1; // Start
                            startRow = i;
                            startCol = j;
                            break;
                        case 'F':
                            maze[i][j] = -2; // Finish
                            finishRow = i;
                            finishCol = j;
                            break;
                       
                    }
                }
            }
            path = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    path[i][j] = -1; 
                }
            }
        }
         catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    private void DisplayMaze() 
    {
        for (int i = 0; i < rows; i++) 
        {
            for (int j = 0; j < cols; j++) 
            {
                System.out.print(maze[i][j] + "\t");
            }
            System.out.println();
        }
    }
    
    public void printMazeAsImage(String outputPath) {
        BufferedImage image = new BufferedImage(cols * 20, rows * 20, BufferedImage.TYPE_INT_RGB);
    
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int colorValue;
                if (maze[i][j] == -1) {
                    colorValue = Color.BLACK.getRGB(); 
                } else if (maze[i][j] == -3) {
                    colorValue = Color.RED.getRGB(); 
                } else {
                    colorValue = Color.WHITE.getRGB(); 
                }
    
                for (int k = 0; k < 20; k++) {
                    for (int l = 0; l < 20; l++) {
                        image.setRGB(j * 20 + l, i * 20 + k, colorValue);
                    }
                }
            }
        }
    
        try {
            File output = new File(outputPath);
            ImageIO.write(image, "png", output);
            System.out.println("Maze image saved to: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    
    private boolean findShortestPathDFS(int currentRow, int currentCol) {
      
        if (currentRow < 0 || currentRow >= rows || currentCol < 0 || currentCol >= cols || maze[currentRow][currentCol] == -1) {
            return false;
        }
    
 
        if (currentRow == finishRow && currentCol == finishCol) {
            path[currentRow][currentCol] = -3; 
            return true;
        }
    
        if (visited[currentRow][currentCol]) {
            return false;
        }
    
       
        visited[currentRow][currentCol] = true;
    
        int[] dr = {-1, 1, 0, 0}; 
        int[] dc = {0, 0, -1, 1}; 
    
        for (int i = 0; i < 4; i++) {
            int newRow = currentRow + dr[i];
            int newCol = currentCol + dc[i];
    
            if (findShortestPathDFS(newRow, newCol)) {
                path[currentRow][currentCol] = -3; 
                return true;
            }
        }
    
        return false;
    }
    private void findShortestPath() {
        findShortestPathDFS(startRow, startCol);
    }
    
    
    private void displayShortestPath() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (path[i][j] == -3) {
                    maze[i][j] = -3; 
                }
            }
        }
    }
    
    public static void main(String[] args) 
    {
        MazeSolver mazeSolver = new MazeSolver();
        mazeSolver.LoadMaze("/Users/metromoomin/Desktop/School/java/old/maze_txt.txt");
        mazeSolver.findShortestPath();
        mazeSolver.displayShortestPath(); 
        mazeSolver.printMazeAsImage("maze_image.png");

    }
}