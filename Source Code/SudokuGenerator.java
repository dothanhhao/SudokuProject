package LabTTH;
import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.text.*;

public class SudokuGenerator{
	
	public static final int n = 3;
	public Writer fileOut;
	private ArrayList<Block> boardS;
	
	private Vector<String> m;	// to save 0,1,2
	private Vector<String> result; // to save all permu of m
	
	
   public SudokuGenerator() {
   		boardS = new ArrayList<Block>();
   		m = new Vector<String>();
   		result = new Vector<String>();
   		for(int i = 0; i < 9; i++){
   			boardS.add(new Block());
   			if(i < 3)	m.add(""+i);}
   }
	
	
	public void generatingSudoku(String output1, int number_blanks){
		
		try{
			generatingBoardS();
			Random ran = new Random();
			
			//convert boardS to array
			int[] convert = new int[81];
			int count = 0;	
			int n = 0;
			while(n < 9){
				 for(int v = 0 ; v < 3; v++){
					   int tmp = n + 3;
					   for(int j = n ; j < tmp; j++){
						   for(int k = 0; k < 3; k++){
							   if(count < 81){
								 convert[count] = boardS.get(j).block[v][k];
								 count++;
							   }
							}
					   }
				 }
				 n += 3;
			 }
			
			convert = swapRow(convert,2,4);
			convert = swapRow(convert,3,7);
			convert = swapRow(convert,6,8);
			
			//digging hole
			ArrayList<Integer> indexHole  = new ArrayList<>();
			while(indexHole.size() != number_blanks){
				int a = ran.nextInt(81);
				if(!indexHole.contains(a))	indexHole.add(a);
			}
			
			for(int i = 0; i < indexHole.size() ; i++){
				convert[indexHole.get(i)] = 0;
			}
			
			//convert array to boardS
			ArrayList<int[]> toBoardS = new ArrayList<>();
			for(int i = 0; i < 9; i++){
				int[] tmp = new int[9];
				for(int j = 0 ; j < 9; j++){
					tmp[j] = convert[i * 9 + j];
				}
				toBoardS.add(tmp);
			}
			boardS = Block.generateBlock(toBoardS);
			writePuzzle(boardS,output1);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void writePuzzle(ArrayList<Block> boardS, String output_name)
	{
		int i = 0;
	    int count = 0;
	    try{
	    	fileOut = new FileWriter(output_name); 
	    	while(i < 9){
				 for(int v = 0 ; v < 3; v++){
					   int tmp = i + 3;
					   for(int j = i ; j < tmp; j++){
						   for(int k = 0; k < 3; k++){
								fileOut.write(boardS.get(j).block[v][k] + " ");
							}
					   }
					   fileOut.write("\n");
				 }
				 i += 3;
			 }
	    	fileOut.close();
        }catch(IOException e){ 
        	System.out.println(e);
        }
                  
	}
	
	public void generatingBoardS(){
		try{
			permute(m.size());
			int i = 0;
			Random a = new Random();
			
			//create block
			while(i < 9){
				boolean checkBlock = false;
				
				//random permu 
				while(checkBlock == false){
					Vector<Integer> index = new Vector<Integer>();
					int j = 0;
					while(j < 3){
						int tmp = a.nextInt(6);
						if(!index.contains(tmp)){
							index.add(tmp);
							j++;
						}
					}
					for(int x = 0; x < n; x++){
						for(int y = 0; y < n; y++){
							String[] arrPermu = result.get(index.get(x)).split("");
							boardS.get(i).block[x][y] = Integer.parseInt(arrPermu[y]);
						}
					}
					//check Sudoku Block
					checkBlock = checkBlock(boardS.get(i));
					
					//Check result to go to next Block
					if(checkBlock == true && i > 0)
						checkBlock = goNextBlock(i);
				}
				i++;
			}
			
			//convert base 3 to base 10
			ArrayList<Integer> arrToMul = new ArrayList<>();
			for(int j = 0; j < n; j++){
				for(int k = 0 ; k < n ; k++){
					arrToMul.add(boardS.get(0).block[j][k]);
				}
			}
			toDec(arrToMul);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public int[] swapRow(int[] convert, int first, int second){
		int[] tmp = new int[9];
		for(int i = 0; i < 9 ; i++){
			tmp[i] = convert[(first - 1) * 9 + i];
			convert[(first - 1) * 9 + i ]	 = convert[(second - 1) * 9 + i];
			convert[(second - 1)  * 9 + i] = tmp[i];
		}
		return convert;		
	}
	
	//convert all block : base 3 to base 10
	public void toDec(ArrayList<Integer> arrToMul){
		int tmp = 0;
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < n; j++){
				for(int k = 0; k < n ; k++){
					boardS.get(i).block[j][k] = arrToMul.get(tmp) * 3 + boardS.get(i).block[j][k] + 1;
				}
			}
			tmp++;
		}
	}
	
	//go to next block if this block different with another block
	public boolean goNextBlock(int idBlock){
		for(int i = 0 ; i < idBlock; i++){
			boolean compare = false;
			for(int j = 0; j < n; j++){
				for(int k = 0; k < n; k++){
					if(boardS.get(i).block[j][k] != boardS.get(idBlock).block[j][k]){
						compare = true;
					}
				}
			}
			if(compare == false)	return false;
		}
		return true;
	}
	
	public boolean checkBlock(Block a){
		boolean checkRow = true;
		boolean checkCol = true;
		for(int i = 0; i < n; i++){
			checkRow = a.checkRow(i);
			checkCol = a.checkCol(i);
			if(checkRow == false || checkCol == false)	return false;
		}
		return true;
	}
	
	private void swap(int i, int j) {
		String tmp = m.elementAt(i);
		m.setElementAt(m.elementAt(j), i);
		m.setElementAt(tmp,j);
	}
	
	public void permute(int n) {
		if(n == 1){
			String tmp ="";
			for(String a : m)	tmp += a;
			result.add(tmp);
		}
		else{
			for(int i = 0 ;i < n; i++){
				permute(n-1);
				if(n % 2 == 0)	swap(i,n-1);
				else	swap(0,n-1);
			}
		}
	}
	
	public static void main(String[] args){
		try{
			SudokuGenerator sg = new SudokuGenerator();
			sg.generatingSudoku("puzzle.txt", 40);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
	}
	
}