//Submitted by: Kishore Kumar Vaddineni
//Language used Java 1.8
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class LetItFlow {
	static String filePath = "";
	static BufferedReader fReader = null;
	static int t; // Number of houses
	static final int NUMBER_OF_ROWS = 3;
	static int n; // Number of columns
	static String[][] g;
	static final int MODULO = 1000000007;
	static StringBuffer output = new StringBuffer();
	static BufferedWriter bWriter = null;

	public static void main(String[] args) {
		getFilePath(args);
		try {
			if (filePath == null || filePath.isEmpty()) {
				System.out.println("File path entered is empty");
			} else {
				fReader = new BufferedReader(new FileReader(filePath));
				String line = "";
				int lineNumber = 1;
				boolean isNNext = false;
				int currentRow = 0;
				int currentHouse = 0;
				while ((line = fReader.readLine()) != null) {
					if (lineNumber != 1) {
						if (!isNNext) {							
							for (int i = 0; i < n; i++) {								
								g[currentRow][i] = line.charAt(i)+"";								
							}							
							if (currentRow == NUMBER_OF_ROWS - 1) {
								currentRow = 0;
								isNNext = true;
								output.append("Case #" + currentHouse + ": " + countPipes() + System.lineSeparator());
							}else {
								currentRow++;
							}

						} else {
							n = Integer.parseInt(line.trim());
							g = new String[NUMBER_OF_ROWS][n];
							currentHouse++;
							isNNext = false;
						}
					} else {
						t = Integer.parseInt(line.trim());
						lineNumber++;
						isNNext = true;
					}
				}

				printOutput();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Input file not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error reading/writing data from/to file");
			e.printStackTrace();
		} finally {
			try {
				if (fReader != null)
					fReader.close();
			} catch (IOException e) {
				System.out.println("Error writing output to file");
			}
		}

	}

	private static void printOutput() {

		try {
			System.out.println(output.toString());
			bWriter = new BufferedWriter(new FileWriter("letitflow_output_kishorevaddineni.txt"));
			bWriter.write(output.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static long countPipes() {
		// Check the cases where piping is not possible

		// if number of columns are odd then piping is not possible for the given
		// outflow cell
		if ((n % 2) > 0) {
			return 0;
		}

		if (g[0][0].equals("#") || g[1][0].equals("#") || g[1][1].equals("#")
				|| g[NUMBER_OF_ROWS - 1][n - 1].equals("#") || g[NUMBER_OF_ROWS - 2][n - 1].equals("#")
				|| g[NUMBER_OF_ROWS - 2][n - 2].equals("#")) {
			return 0;
		} else {
			// If there is wall in any of the cell in the middle row then piping is not
			// possible
			for (int j = 0; j < n; j++) {
				if (g[1][j].equals("#")) {
					return 0;
				}
			}

			// Check first and third row for walls that might not let the piping happen
			// No need to check first and last column of the rows
			for (int j = 1; j < n - 1; j++) {
				if (g[0][j].equals("#")) {
					if (g[2][j].equals("#")) {
						return 0;
					} 					
				}			
			}

			// Count the number of loops that can be formed after drawing all the possible
			// paths
			long loopsCount = 0;
			if(n == 2) {
				return 1;
			}
			for (int j = 1; j < n - 1; j = j + 2) {
				if((g[0][j].equals("#") && g[2][j + 1].equals("#")) || (g[2][j].equals("#") && g[0][j + 1].equals("#"))) {
					return 0;
				}
				if (!(g[0][j].equals("#") || g[0][j + 1].equals("#") || g[2][j].equals("#")
						|| g[2][j + 1].equals("#"))) {
					loopsCount++;
				}
			}			

			// calculate 2^loopsCount			
			long result = (long)((Math.pow(2, loopsCount)) % MODULO);			
			return result;
		}

	}

	private static void getFilePath(String[] args) {
		if (args != null && args.length > 0) {
			// considers first arg as file path
			filePath = args[0];
		} else {
			// argument not passed, prompt to enter file path
			System.out.print("Enter file path to read input data: ");
			BufferedReader cmdReader = new BufferedReader(new InputStreamReader(System.in));
			try {
				filePath = cmdReader.readLine();
			} catch (IOException e) {
				System.out.println("Error reading file path from input");
				e.printStackTrace();
			}
		}
	}
}
