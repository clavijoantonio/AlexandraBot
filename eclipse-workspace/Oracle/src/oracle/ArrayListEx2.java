package oracle;

import java.util.ArrayList;
import java.util.Iterator;

public class ArrayListEx2 {
    public static void main(String args[]) {
        
        ArrayList<Integer>numeros= new ArrayList<>();
        
        numeros.add(25);
        numeros.add(15);
        numeros.add(30);
        numeros.add(2);
        numeros.add(-16);
        numeros.add(0);
    
        Iterator<Integer> iterador= numeros.iterator();
        
       while(iterador.hasNext()) {
        	
        	System.out.println(iterador.next());
        }
        
        
       while(iterador.hasNext()) {
        	
    	   int valor=iterador.next();
        	
    	   if(valor%2==0) {
        		
    		   iterador.remove();
        	}else {
        		System.out.println("numero:"+ valor);
        	}
    	 	
        }
       
      
       	
      
       
        
        
       
        
         
    }
}
