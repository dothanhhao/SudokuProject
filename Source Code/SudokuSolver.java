package LabTTH;
import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.text.*;

public class SudokuSolver{
	
   private  Writer fileOut;
   private ArrayList<Block> boardS;
   private final int BlockSize = 3;
   private final int nBlock = 9;
   private final int nBoard = 81; 
   
   protected class PermutationIterator<E> implements Iterator<List<E>> {
	   
	    private int[] keys;
	    private int idx;
	    private List<E> objectMap;
	    private List<E> nextPermutation;
	   
	    public PermutationIterator(final Collection<? extends E> coll) { 
	        if (coll == null) throw new NullPointerException("The collection must not be null");

	        idx = 0;
	        keys = new int[coll.size()];

	        int value = 0;
	        objectMap = new ArrayList<E>();
	        for (E e : coll) {
	            objectMap.add(value, e);
	            keys[value] = 0;
	            value++;
	        }
	        nextPermutation = new ArrayList<E>(coll);
	    }

	    public boolean hasNext() {
	        return nextPermutation != null;
	    }

	    public void swap(int x, int y){
	         E temp = objectMap.get(x);
	         objectMap.set(x,objectMap.get(y));
	         objectMap.set(y,temp);
	    }
	   
	    public List<E> next() {
	        if (!hasNext()) throw new NoSuchElementException();

	        final List<E> nextP = new ArrayList<E>();
	    
	        if (idx == keys.length) {
	            nextPermutation = null;
	            return nextPermutation;
	        }
	        do{
	            if(keys[idx] < idx){
	                if(idx % 2 ==0)	swap(0, idx);
	                else	 swap(keys[idx], idx);
	                keys[idx] += 1;
	                idx = 0;
					break;
	            }
	            else{
	            	keys[idx] = 0;
	            	idx += 1;
	            }
	        }while(idx < keys.length);

	        for (int i = 0; i < keys.length; i++) {
	            nextP.add(objectMap.get(i));
	        }
	        
	        final List<E> result = nextPermutation;
	        nextPermutation = nextP;
	        
	        return result;
	    }
	    public void remove() {
	        throw new UnsupportedOperationException("remove() is not supported");
	    }
	}
   
   public Vector<Integer> findLostNum(Block a){
	   Vector<Integer> vec = new Vector<Integer>();
	   for(int i = 1; i<= 9 ; i++){
		   boolean check = true;
		   for(int j = 0; j < BlockSize; j++){
			   for(int k = 0 ; k < BlockSize; k++){
				   if( i == a.block[j][k]){
					   check = false;
					   break;
				   }
			   }
		   }
		   if(check == true)	vec.add(i);
	   }
	   return vec;
   }
   
   public int checkRowAndCol(int row, int col,int numTest ,int numBlock){
	   
	   boolean checkRow = true;
	   boolean checkCol = true;
	   //check number in row
	   for(int i = (numBlock/ 3) * 3 ; i <  ((numBlock/ 3) * 3) +3 ; i++){
		   for(int j = 0 ; j < BlockSize; j++){
			   if(i == numBlock && j == col){}
			   else if(numTest == boardS.get(i).block[row][j]){
				   checkRow = false;
				   break;
			   }
		   }
	   }
	   //Check number in col
	   for(int i = (numBlock % 3) ; i < nBlock ; i += 3){
		   for(int j = 0; j < BlockSize; j++){
			   if(i == numBlock && j == row){}
			   else if(numTest == boardS.get(i).block[j][col]){
				   checkCol = false;
				   break;
			   }
		   }
	   }
	   if(checkRow == true && checkCol == true)	return 1;
	   return 0;
   }
   
   
   public boolean readSudoku(String filename){
	   	
	   	ArrayList<int[]> a = new ArrayList<int[]>();
	   	
	   	try{
	   		File read = new File(filename);
	   		Scanner sc = new Scanner(read);
	   		while(sc.hasNextLine()){
	   			String line = sc.nextLine();
	   			String[] arrString = line.split(" ");
	   			int[] arrNum = new int[arrString.length];
	   			for(int i = 0; i < arrString.length ; i++){
	   				arrNum[i] = Integer.parseInt(arrString[i]);
	   			}
	   			a.add(arrNum);
	   		}
	   	}catch(IOException e){
	   		e.printStackTrace();
	   	}
	   	
	   	boardS = Block.generateBlock(a);
	   	
        return false;
   }
   
   public void SudokuSolver(String filename){
	   SudokuGenerator gen = new SudokuGenerator();
	   gen.writePuzzle(boardS, filename);
	   //SudokuGenerator.writePuzzle(boardS, filename);
//	   File write = new File(filename);
//	   try {
//		BufferedWriter wf= new BufferedWriter(new FileWriter(write));
//		int n = 0;
//		while(n < 9){
//			 for(int i = 0 ; i < BlockSize; i++){
//				   int tmp = n + 3;
//				   for(int j = n ; j < tmp; j++){
//					   for(int k = 0; k < BlockSize; k++){
//							 System.out.print(boardS.get(j).block[i][k] + " ");
//							 wf.write(boardS.get(j).block[i][k] + " ");
//						}
//				   }
//				   wf.write("\n");
//				   System.out.println();
//			 }
//			   n += 3;
//		 }
//		wf.close();
//	   } catch (IOException e) {
//			e.printStackTrace();
//	   }
   }
   
   public boolean solve(int level){
        if(level == 9)	return true;
       
        //find all lost number in block
        boolean checkSolve = false;
        Vector<Integer> vec = new Vector<Integer>();
        vec = findLostNum(boardS.get(level));
      
        //Save the number in current block
        Vector<Integer> saveSta = new Vector<Integer>();
        for(int i = 0; i < vec.size() ; i++){
        	saveSta.add(0);
        }
        
        //permu
        PermutationIterator<Integer> permu = new PermutationIterator<Integer>(vec);
        while(permu.hasNext() && checkSolve == false ){
        	
        	ArrayList<Integer> list = (ArrayList<Integer>) permu.next();
        	
        	if(list != null){
	        	int indexList = 0;
	        	int stop = -1;
	        	for(int i = 0 ; i < BlockSize; i++){		
	        		for(int j = 0; j < BlockSize; j++){
	        		
	        			if(boardS.get(level).block[i][j] == saveSta.get(indexList)){
	        				// if checkRowAndCol false, we will stop and check next permu
	        				stop = checkRowAndCol(i,j,list.get(indexList),level);
	        				if(stop == 1){
	        					boardS.get(level).block[i][j] = list.get(indexList);
	        					saveSta.set(indexList, list.get(indexList));
	        					indexList++;
	        					if(indexList == list.size())	break;
	        				}
	        				else if (stop == 0){
	        					saveSta = returnPermu(saveSta,level);
	        					stop = 0;
	        					break;
	        				}
	        			}
	        		}
	        		if(stop == 0 || indexList == list.size())	break;
	        	}
	        	
	        	//recursive to find number in next block
	        	if( indexList == list.size()){
	        		checkSolve = solve(level + 1);
	        		if(checkSolve == false){
	        			saveSta = returnPermu(saveSta,level);
	        		}
	        	}
        	}
        }
   	    return checkSolve;
   }
   
   //return the begin statement of block with number 0
   public Vector<Integer> returnPermu(Vector<Integer> saveSta,int level){
	   int index = 0 ;
	   boolean enough = false;
	   for(int i = 0; i < BlockSize; i++){
			for(int j = 0; j < BlockSize; j++){
				if(boardS.get(level).block[i][j] == saveSta.get(index)
						&& saveSta.get(index) != 0){
					boardS.get(level).block[i][j] = 0;
					saveSta.set(index, 0);
					index++;
					if(index == saveSta.size()){
						enough = true;
						break;
					}
				}
			}
			if(enough == true)	break;
		}
	   return saveSta;
   }

   public static void main(String args[]){
	   try{
	       SudokuSolver sol = new SudokuSolver();
	       sol.readSudoku("puzzle.txt");
	       sol.solve(0);
	       sol.SudokuSolver("outputSudoku.txt");
	   }catch(Exception ex){
		   ex.printStackTrace();
	   }
	   
   }
}