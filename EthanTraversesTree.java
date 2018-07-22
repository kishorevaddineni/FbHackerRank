//By Kishore Kumar Vaddineni
//Used Java 1.8

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EthanTraversesTree {
	static String filePath = "";
	static BufferedReader fReader = null;
	static BufferedWriter bWriter = null;
	static int t; //Number of trees
	static int n; //Number of Nodes
	static int k; //Number of Labels
	static int[][] treeData;
	static StringBuffer output1 = new StringBuffer();
	
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
				int currentRowIndex = 0;
				int currentTree = 0;
				
				while ((line = fReader.readLine()) != null) {
					if (lineNumber != 1) {
						if (!isNNext) {							
							String[] s = line.split(" ");
							treeData[currentRowIndex][0] = Integer.parseInt(s[0]);
							treeData[currentRowIndex][1] = Integer.parseInt(s[1]);
							if(currentRowIndex == n-1) {
								isNNext = true;
								currentRowIndex = 0;
								
								//process data and get Result
								
								output1.append("Case #" + currentTree + ": " + processDataAndGetResult() + System.lineSeparator());
							}else {
								currentRowIndex++;
							}
							

						} else {
							String s[] = line.split(" ");
							n = Integer.parseInt(s[0]);
							k = Integer.parseInt(s[1]);
							
							treeData = new int[n][2];
							isNNext = false;
							currentTree++;
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
			System.out.println(output1.toString());
			bWriter = new BufferedWriter(new FileWriter("ethantraversestree_output_kishorevaddineni.txt"));
			bWriter.write(output1.toString());
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
	
	private static String processDataAndGetResult() {		
		
		if(n == 1) {
			return "1";
		}		
		else {
			//Get Pre-Order and Post-Order Traversal values
			List<Integer> preOrder = new ArrayList<Integer>();
			List<Integer> postOrder = new ArrayList<Integer>();
			List<Integer> stack = new ArrayList<Integer>();
			List<Integer> visited = new ArrayList<Integer>();
			stack.add(1);
			preOrder.add(1);
			visited.add(1);
			int checkIndex = 0;
			while(!stack.isEmpty()) {
				int data = treeData[stack.get(stack.size()-1)-1][checkIndex];
				if(!visited.contains(data)) {
					if(data != 0) {
						visited.add(data);
						stack.add(data);
						preOrder.add(data);	
						if(checkIndex == 1) {
							checkIndex = 0;
						}
					}
					else {
						if(checkIndex == 0) {
							checkIndex = 1;
						}else {
							while(stack.size()>0 && visited.contains(stack.get(stack.size()-1))) {
								data = treeData[stack.get(stack.size()-1)-1][checkIndex];
								if(data == 0 || visited.contains(data)) {
									postOrder.add(stack.get(stack.size()-1));
									stack.remove(stack.size()-1);
								}else {
									stack.add(data);
									visited.add(data);
									preOrder.add(data);
									checkIndex = 0;
									break;
								}
								
							}							
						}
					}
									
				}
				else {
					
				}
			}
			
			//Now assign labels
			List<List<Integer>> relatedNodesToLabelList = new ArrayList<List<Integer>>();
			List<Integer> relatedNodes = new ArrayList<Integer>();
			
			relatedNodes.add(preOrder.get(0));
			int currentIndex = 0;
			int nodesProcessed = 0;
			boolean[] indexesProcessed = new boolean[preOrder.size()];
			indexesProcessed[0] = true;
			while(nodesProcessed<preOrder.size()) {
				if(relatedNodes.contains(postOrder.get(currentIndex))) {
					relatedNodesToLabelList.add(relatedNodes);
					relatedNodes = new ArrayList<Integer>();
					boolean contLoop=false;
					for(int i=0;i<indexesProcessed.length;i++) {
						if(!indexesProcessed[i]) {
							currentIndex = i;
							contLoop=true;
							break;
						}
					}
					if(!contLoop) {
						break;
					}
				}
				else {
					nodesProcessed++;
					relatedNodes.add(postOrder.get(currentIndex));
					currentIndex = preOrder.indexOf(postOrder.get(currentIndex));
					indexesProcessed[currentIndex] = true;
				}
			}
			
			if(!relatedNodes.isEmpty()) {
				System.out.println("Something went wrong");
			}
			if(k>relatedNodesToLabelList.size()) {
				return "Impossible";
			}
			else {
				int[] output = new int[n];
				int label = 1;
				for(List<Integer> nodes:relatedNodesToLabelList) {
					for(int i:nodes) {
						output[i-1] = label;
					}
					if(label==k) {
						label = 1;
					}
					else {
						label++;
					}
				}
				
				StringBuffer sb = new StringBuffer();
				for(int i=0;i<output.length;i++) {					
					sb.append(output[i]+"");
					if(i<output.length-1) {
						sb.append(" ");
					}
				}
				return sb.toString();				
			}			
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
