package oracle;

import java.util.ArrayList;

public class ArrayListEx1 {

    public static void main(String[] args) {
        
        ArrayList<String> Estudiantes= new ArrayList<>();
        
        Estudiantes.add("Amy");
        Estudiantes.add("Bod");
        Estudiantes.add("Cindy");
        Estudiantes.add("David");
        Estudiantes.add(0,"Nick");
        Estudiantes.add(1,"Mike");
        Estudiantes.remove(3);
        for(String i:Estudiantes) {
        
        	System.out.println(i);
    	
     }
        System.out.println(Estudiantes.size());
    }
    
}
