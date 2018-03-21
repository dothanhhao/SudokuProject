package LabTTH;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;

public class Block{
	
    int [][] block;
    public static final int n = 3;
    
    public  Block(){
         block = new int[n][n];
         for(int i = 0; i< n; i++)
                  for(int j = 0; j< n; j++)
                       block[i][j] = 0;
    }
    
    public static ArrayList<Block> generateBlock(ArrayList<int[]> a){
           ArrayList<Block> p = new ArrayList<Block>();
           
           int[] arraySave = new int[81];
           int index = 0;
           for(int i = 0; i < a.size(); i++){
        	   for(int j = 0 ; j < a.get(i).length; j++){
        		   arraySave[index] = a.get(i)[j];
        		   index++;
        	   }
           }
           
          for(int i = 0 ; i< 9 ; i++){
        	  p.add(new Block());
          }
           
          for(int i = 0; i < arraySave.length ; i++){
   	   		int id = ((i/9) / 3) * 3 + (i % 9)/3;
   	   		int row = (i / 9)% 3;
   	   		int col =  i % 3;
   	   		p.get(id).block[row][col] = arraySave[i];
   	   	  }
           
       return p;
    }
    
    public boolean checkCol(int id){  
    	for(int i = 0 ; i < n; i++){
    		for(int j = i + 1; j < n; j++){
    			if(block[i][id] == block[j][id]) return false;
    		}
    	}
         return true;
    }
    
    public boolean checkRow(int id){        
    	for(int i = 0 ; i < n; i++){
    		for(int j = i + 1; j < n; j++){
    			if(block[id][i] == block[id][j]) return false;
    		}
    	}
         return true;
    }
    
}