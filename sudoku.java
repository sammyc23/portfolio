import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class sudoku extends JFrame{
	public static void main(String[] args) {
		new sudoku();
	}

	//variable declaration
	private JTextField[][] cells;
	private JButton solveButton, clearButton, loadButton;
	
	public sudoku() {
		cells = new JTextField[9][9];
		JPanel matrixPanel = new JPanel();
		matrixPanel.setLayout(new GridLayout(9,9));
		
		//border around the grid
		matrixPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		matrixPanel.setBackground(Color.white);
		
		//create 9x9 matrix
		for (int row=0; row<9; row++) {
			for (int col=0; col<9; col++) {
				cells[row][col]=new JTextField();
				cells[row][col].setHorizontalAlignment(JTextField.CENTER);
				cells[row][col].setFont(new Font("Arial", Font.BOLD, 30));
				cells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				matrixPanel.add(cells[row][col]);
			}
		}
		
		//create buttons to solve and clear the matrix
		JPanel buttonPanel = new JPanel();
		solveButton = new JButton("Solve");
		clearButton = new JButton("Clear");
		
		//create button to import a csv and txt file
		loadButton = new JButton("Load File");
		
		//add event (call the solve function) to the solve button
		solveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				solve();
			}
		});
		
		//add event (clear the board) to the clear button
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		
		//add event (load a file) to the load file button
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadFile();
			}
		});
	
		//add buttons into GUI interface
		buttonPanel.add(solveButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(loadButton);
		add(matrixPanel);
		add(buttonPanel, BorderLayout.SOUTH);
		
		//grid display and size
		setTitle("Suduko");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,500);
		setLocationRelativeTo(null);
		setVisible(true);
		}

	
	
	//function used to solve matrix
	//called when solve button is pressed
	private void solve() {
		//create new array to store numbers from gui to solve
		int[][] matrix = new int[9][9];
		
		//get the values from the cells
		for (int i =0; i < 9; i++) {
			for (int j =0; j < 9; j++) {
				String text = cells[i][j].getText().trim();
				//if the cell is empty
				if (text.isEmpty()) {
					//store 0 for the cell
				    matrix[i][j] = 0;
				} else {
					//convert the text to an integer and store the number in array
				    matrix[i][j] = Integer.parseInt(text);
				}
			}
		}
		
		//check if the sudoku puzzle given is valid
		//caled when clear button is pressed
		if(!isValidSudoku(matrix)) {
			JOptionPane.showMessageDialog(this, "Sudoku provided is invalid!");
			return;
		}
		
		//store result for search in boolean variable
		boolean result = search(matrix);
		
		//if the search returned true (i.e. sudoku was solved)
		if (result) {
			//fill the GUI matrix with solution
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					cells[i][j].setText(String.valueOf(matrix[i][j]));
				}
			}
			//show successful message
			JOptionPane.showMessageDialog(this, "Sudoku has been solved!");
		}
		//if the sudoku could not be solved 
		else {
			//show unsuccesful message
			JOptionPane.showMessageDialog(this, "Sudoku cannot be solved!");
		}
	}
	
	//function to reset matrix
	private void clear() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setText("");
            }
        }
    }
	
	//function to load a file from users computer that they wish to solve
	private void loadFile() {
		JFileChooser fileChooser = new JFileChooser();
		
		//only txt and csv files can be uploaded
		fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text and CSV files", "txt", "csv"));
		
		//if the user selects a file
		if (fileChooser.showSaveDialog(this) == fileChooser.APPROVE_OPTION) {
			//users file assigned
			File file = fileChooser.getSelectedFile();
			try {
				//read file
				BufferedReader reader = new BufferedReader (new FileReader(file));
				String line;
				int row = 0;
				//read each line from file
				while ((line = reader.readLine()) != null && row < 9) {
					line = line.trim();	
					
					//split with commas or spaces
					if (line.isEmpty()) continue;
					String[] vals;
					
					//CSV format (8,0,2,0)
	                if (line.contains(",")) {
	                    vals = line.split(",");}
	                
	                //space-separated (8 0 2 0)
	                else if (line.contains(" ")) {
	                    vals = line.trim().split("\\s+");}
	                
	                //continuous digits (802000000)
	                else if (line.matches("\\d{9}")) {
	                    vals = line.split(""); }
	                
	                // if format is invalid dislay message
	                else {
	                    JOptionPane.showMessageDialog(this, "Invalid file format in line " + (row + 1));
	                    return;
	                }
					
					//fill each cell in the row
	                int start = vals.length == 10 ? 1 : 0; // handle leading "" from split("")
	                for (int col = 0; col < 9 && (col + start) < vals.length; col++) {
	                    String val = vals[col + start].trim();
	                    //leave blank if 0
	                    if (val.equals("0") || val.isEmpty()) {
	                        cells[row][col].setText("");
	                    } else {
	                        cells[row][col].setText(val); //put number in correct cell
	                    }
	                }
	                row++;
	            }
				//file uploads correctly
				JOptionPane.showMessageDialog(this, "File loaded successfully");
			} 
			catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error loading file");
			}
		}
	}
	
	//helper function to check given function is valid
	public static boolean isValidSudoku(int[][] matrix) {
		//iterate through each rows
		for (int i = 0; i < 9; i++) {
			//iterate through each columns
			for (int j = 0; j < 9; j++) {
				//fill number with the current cell
				int num = matrix[i][j];
				//only check empty cells
				if (num != 0) {
					//temporarily clear the cell so it doesn't count itself as a duplicate
					matrix[i][j] = 0;
					//check if the number is valid in the current row, cell and 3x3 subgrid
					if (!valid(matrix, i, j, num)) {
						//if it's invalid then restore the number to the cell and return false
						matrix[i][j] = num;
						return false;
					}
					//restore value after checking 
					matrix[i][j] = num;
				}
			}
		}
		//if everything is valid return true
		return true;
	}
	
	//helper function that carries out brute-force search with backtracking. 
	public static boolean search(int[][] matrix) {
		//search rows
		for (int i = 0; i < 9; i++) {
			//search columns
			for (int j = 0; j < 9; j++) {
				//if the cell being checked is empty
				if(matrix[i][j] == 0) {
					//check numbers from 1-10
					for (int num = 1; num < 10; num++) {
						//check if the number is valid
						if(valid(matrix, i, j, num)) {
							//if it is valid then put number in cell
							matrix[i][j] = num;
							//try solve the rest of the matrix with that number
							//if true the whole matrix is solved
							if(search(matrix)){
								return true;
							}
							//else continue trying other valid numbers
							matrix[i][j] = 0;
						}
					}
					/*if every number in this cell is not working then something is a number is wrong in a 
					 * previous cell i.e. backtrack.*/
					return false;
				}
			}
		}
		//sudoku solved!!!
		return true;
	}
	
	//helper function to test if number being tried in search is valid
	public static boolean valid(int[][] matrix, int row, int col, int num) {
		//search the row and column the cell is in
		for (int i = 0; i < 9; i++) {
			//if the same number is in the row 
			if (matrix[row][i] == num) {
				return false;
			}
			//if the same number is in the column 
			if(matrix[i][col] == num) {
				return false;
			}
		}
		
		//figure out which subgrid the current cell is in 
		int subRow = (row / 3) * 3;
		int subCol = (col / 3) * 3;
		
		//search the subgrid 
		for (int i = subRow; i < subRow + 3; i++) {
			for (int j = subCol; j < subCol + 3; j++){
				//if the cell being searched has the same number 
				if(matrix[i][j] == num) {
					return false;
				}
			}
		
		}
		return true;
	}
	
}



