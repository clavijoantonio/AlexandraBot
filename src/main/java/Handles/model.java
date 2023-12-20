package Handles;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.opencsv.CSVReader;

public class model extends TelegramLongPollingBot {

	    String saludo;
	    String telefono;
	    String direccion;
	    String domicilio;
	    String pagina;
	    String lunes;
	    String martes;
	    String miercoles;
	    String jueves;
	    String viernes;
	    String sabado;
	    String domingo;
	    String localPath = "";
	    int opcion;
	    int contMesa;
	    int[] etapa = new int[100];
	    String message_text;
	   
	    List<String[]> registrosMenu = new ArrayList<>();
	    
	  //LISTA DE CONTACTOS QUE MANEJAREMOS
	    String[] contactos = new String[100];
	  //NUMERO DE CONTACTOS QUE ESTAREMOS MANEJANDO
	    int cantTabs = 0;
	    //USUARIO SELECCIONADO O TAB SELECCIONADA
	    int usuarioSel = 0;
	    SendMessage message = new SendMessage(); 
	  //MENSAJES RECIBIDOS DESDE WA
	    String[] mensajesRecibidos = new String[100];
	    String nombreContacto;
	  
	
	@Override
	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		 try {
	            localPath = new File(".").getCanonicalPath();
	            System.out.println( localPath);
	        } catch (IOException ex) {
	           System.out.println("no ecuentro la ruta");
	        }
		if (update.hasMessage() && update.getMessage().hasText()) {
	        // Set variables
	        nombreContacto = update.getMessage().getChat().getFirstName();
	        String user_last_name = update.getMessage().getChat().getLastName();
	        String user_username = update.getMessage().getChat().getUserName();
	        long user_id = update.getMessage().getChat().getId();
	        message_text = update.getMessage().getText();
	        long chat_id = update.getMessage().getChatId();
	        
	        System.out.println(message_text+nombreContacto);
	       // Create a message object object
	        
	        message.setChatId(update.getMessage().getChatId().toString());
	      //VARIABLE QUE ALMACENARA LOS ULTIMOS MENSAJE ENVIADOS
	        mensajesRecibidos[usuarioSel] = "";
	       
	        leemosDatos();
	        leemosMenu();
	        revisionTel();
	
		}
	}

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return "DemoAlexandraBot";
		
	}
	
	@Override
	public String getBotToken() {
		
		return "6714109081:AAGR0OeYpJ2HMvH4Xqks-8ApO9xJ0cHCS2c";
	}
	
	public void revisionTel() {
		if(message_text!=null) {
        	
        	//etapa[usuarioSel]=1;
        	System.out.println("hay mensaje");
        	
        }
        
       boolean contactoEncontrado = false;
     //VALIDAMOS QUE EL NOMBRE DE CONTACTO NO ESTE EN LA LISTA DE CONTACTOS DE LO CONTRARIO NOS POSICIONAMOS EN EL ID DEL CONTACTO
       for (int tabSel = 1; tabSel <= cantTabs; tabSel++) {
           //SI EL CONTACTO YA EXISTE EN LA LISTA
           if (nombreContacto.equals(contactos[tabSel])) {
               //ACTIVAMOS LA BANDERA Y DECMOS QUE SI ENCONTRAMOS EL CONTACTO
               contactoEncontrado = true;
               usuarioSel = tabSel;
               System.out.println( usuarioSel);
               System.out.println(" aca voy");
               //ROMPEMOS EL CICLO
               break;
           }
       }
      //SI NO SE ENCONTRO EL CONTACTO LO CREAMOS
        if (contactoEncontrado == false) {
            //AUMENTAMOS EL NUMERO DE CONTACTOS
            cantTabs++;
            //ASIGNAMOS EL NOMBRE DEL CONTACTO A LA LISTA DE CONTACTOS
            contactos[cantTabs] = nombreContacto;
            //OBTENEMOS EL ID DEL USUARIO SELECCIONADO
            usuarioSel = cantTabs;
            //ASIGANOS LA ETAPA DEL USUARIO QUE ES LA 1 (ENVIAR TEXTOS D ELOS SORTEOS)
            etapa[usuarioSel] = 1;
        }
        String ultimoMensaje =message_text;
        mensajesRecibidos[usuarioSel] = mensajesRecibidos[usuarioSel] + " " + ultimoMensaje;
       
        try {
        	
        if (etapa[usuarioSel] == 1) {
            //ETAPA 1 DONDE SE ENVIAN LOS MENSAJES DE LOS DIFERENTES SORTEOS
            etapa1();
           return;
        } else if (etapa[usuarioSel] == 2) {
            //VALIDAMOS QUE SI SEA UN NUMERO DEL SORTEO Y PREGUNTAMOS SI QUIERE UN NUMERO AL AZAR Y QUIERE SELECCIONAR SU NUMERO
            //SI NO LLAMAMOS LA ETAPA 1 PARA QUE ENVIE POR WA LOS SORTEOS
            etapa2();
            return;
        }
        if (etapa[usuarioSel] == 3) {
            //VALIDAMOS QUE SI SEA UN NUMERO DEL SORTEO Y PREGUNTAMOS SI QUIERE UN NUMERO AL AZAR Y QUIERE SELECCIONAR SU NUMERO
            //SI NO LLAMAMOS LA ETAPA 2 PARA QUE ENVIE POR WA LOS SORTEOS
            etapa3();
            return;
        }
                            
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}
           
	
	public void etapa1() throws TelegramApiException {
		
    	String listaOpciones = saludo + "\n1.-Menu del dia de hoy\n2.-Menu de otro dia\n3.-Horarios\n4.-Acerca de nosotros\n";
    	
    	message.setText(listaOpciones);
    	 
    	execute(message);
    	
    	etapa[usuarioSel] = 2;
  
    	System.out.println(etapa[usuarioSel]);
    }
	public void etapa2() {
        //VARIABLE QUE RECIBE LAS OPCIONES COLOCADAS POR EL USUARIO DE WA
        int opcion;
        //QUITAMOS ESPACIOS EN BLANCO
        String mensajesRecibidosSinEspacios = mensajesRecibidos[usuarioSel].replace(" ", "");
        //mensajesRecibidosSinEspacios = "2";
        //VALIDAMO SI REALMENTE SE ENVIO UN NUMERO
        if (isNumeric(mensajesRecibidosSinEspacios)) {
            opcion = Integer.parseInt(mensajesRecibidosSinEspacios);
            //SI LA OPCION ESTA ENTRE 1 Y 4
            if (opcion >= 1 && opcion <= 4) {
  
            	System.out.println(opcion);
                if (opcion == 1) {
                    String textoMenuDia = "MENU DEL DIA DE HOY\n";
                    //VARIABLE QE CUENTA LOS REGISTROS
                    int cont = 0;
                    //OBTENEMOS EL DIA DE HOY
                    LocalDate today = LocalDate.now();
                    DayOfWeek dayOfWeek = today.getDayOfWeek();
                    System.out.println(dayOfWeek);
                    //SACACAMOS DACA REGISTRO
                    for (String[] registro : registrosMenu) {
                        //COMENZAMOS A PARTIR DEL 3 ER REGISTRO
                        if (cont >= 2) {
                            //OBTENEMOS EL DIA DE HOY EN NUMERO
                            int diaSemanaNum = dayOfWeek.getValue();
                            System.out.println(diaSemanaNum*2);
                            //OBTENEMOS LA COLUMNA DEL PRECIO
                            int columnaPrecio = diaSemanaNum;
                            //OBTENEMOS LA COLUMNA DE LA COMIDA
                            int columnaComida = (diaSemanaNum*2) + 1;
                           
                            //OBTENEMOS LA COMIDA
                            String comida = registro[columnaPrecio];
                            System.out.println(comida );
                            //OBTENEMOS EL PRECIO
                            String precio = registro[columnaComida];
                            //CONCATENAMOS EL TEXTO
                            textoMenuDia = textoMenuDia + comida + ": $" + precio + "\n";
                            System.out.println( textoMenuDia);
                        }
                        cont++;
                    }
                    //ESCRIBIMOS EL MENSAJE
                    
                    message.setText(textoMenuDia);
                    try {
						execute(message);
					} catch (TelegramApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                   
                    etapa[usuarioSel] = 1;
                }
                if (opcion == 2) {
                    String textoMenuOtroDia = "De que dia quiere saber el menu\n"
                            + "1.-Lunes\n2.-Martes\n3.-Miercoles\n4.-Jueves\n5.-Viernes\n6.-Sabado\n7.-Domingo\n";
                    //ESCRIBIMOS EL MENSAJE
                    message.setText(textoMenuOtroDia);
                    try {
						execute(message);
					} catch (TelegramApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                   
                    etapa[usuarioSel] = 3;
                }
                if (opcion == 3) {
                    String textoMenuHorarios = "HORARIOS\n" + lunes + "\n" + martes + "\n" + miercoles + "\n" + jueves + "\n" + viernes + "\n" + sabado + "\n" + domingo + "\n";
                    //ESCRIBIMOS EL MENSAJE
                    message.setText(textoMenuHorarios);
                    try {
						execute(message);
					} catch (TelegramApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                  
                    etapa[usuarioSel] = 1;
                }
                if (opcion == 4) {
                    String textoMenuAcerca = "ACERCA DE NOSOTROS\n" + telefono + "\n" + direccion + "\n" + domicilio + "\n" + pagina + "\n";
                    //ESCRIBIMOS EL MENSAJE
                    message.setText(textoMenuAcerca);
                    try {
						execute(message);
					} catch (TelegramApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                   
                    etapa[usuarioSel] = 1;
                }
            } //NO SE SELECCIONO LA OPCION CORRECTA
            else {
                //ESCRIBIMOS EL MENSAJE
            	message.setText("Opcion no valida\n");
                try {
					execute(message);
				} catch (TelegramApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               
                etapa[usuarioSel] = 1;
            }
        } else {
            //ESCRIBIMOS EL MENSAJE
        	message.setText("Opcion no valida\n");
            try {
				execute(message);
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            etapa[usuarioSel] = 1;
        }
    }
    public void etapa3() {
        //VARIABLE QUE RECIBE LAS OPCIONES COLOCADAS POR EL USUARIO DE WA
        int opcion;
        //QUITAMOS ESPACIOS EN BLANCO
        String mensajesRecibidosSinEspacios = mensajesRecibidos[usuarioSel].replace(" ", "");
        //VALIDAMO SI REALMENTE SE ENVIO UN NUMERO
        if (isNumeric(mensajesRecibidosSinEspacios)) {
            opcion = Integer.parseInt(mensajesRecibidosSinEspacios);
            //SI LA OPCION ESTA ENTRE 1 Y 7
            if (opcion >= 1 && opcion <= 7) {
                String textoMenuDia = "MENU DEL DIA " + deNumeroAdiaSemana(opcion) + "\n";
                //SI ESCRIBIO LA OPCION 7 LA RRESCRIBIMOS COMO 0 OSEA QUE EL DOMINGO ES EL DIA 0
                if (opcion == 7) {
                    opcion = 0;
                }
                //VARIABLE QE CUENTA LOS REGISTROS
                int cont = 0;
                //SACACAMOS DACA REGISTRO
                for (String[] registro : registrosMenu) {
                    //COMENZAMOS A PARTIR DEL 3 ER REGISTRO
                    if (cont >= 2) {
                        //OBTENEMOS LA COLUMNA DEL PRECIO
                        int columnaPrecio = opcion * 2;
                        //OBTENEMOS LA COLUMNA DE LA COMIDA
                        int columnaComida = (opcion * 2) + 1;
                        //OBTENEMOS LA COMIDA
                        String comida = registro[columnaPrecio];
                        //OBTENEMOS EL PRECIO
                        String precio = registro[columnaComida];
                        //SI HAY TEXTO EN COMIDA ESCRIBIMOS LA LINEA
                        if (comida.length() > 0) {
                            //CONCATENAMOS EL TEXTO
                            textoMenuDia = textoMenuDia + comida + ": $" + precio + "\n";
                        }
                    }
                    cont++;
                }
                //ESCRIBIMOS EL MENSAJE
                message.setText( textoMenuDia);
                try {
    				execute(message);
    			} catch (TelegramApiException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                etapa[usuarioSel] = 1;
            } else {
                //ESCRIBIMOS EL MENSAJE
            	
            	message.setText("Opcion no valida\n");
                try {
    				execute(message);
    			} catch (TelegramApiException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                
                etapa[usuarioSel] = 1;
              
            }
        } else {
            //ESCRIBIMOS EL MENSAJE
        	message.setText("Opcion no valida\n");
            try {
				execute(message);
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            etapa[usuarioSel] = 1;
          
        }
    }
    public String deNumeroAdiaSemana(int dia) {
        if (dia == 1) {
            return "LUNES";
        }
        if (dia == 2) {
            return "MARTES";
        }
        if (dia == 3) {
            return "MIERCOLES";
        }
        if (dia == 4) {
            return "JUEVES";
        }
        if (dia == 5) {
            return "VIERNES";
        }
        if (dia == 6) {
            return "SABADO";
        }
        if (dia == 7) {
            return "DOMINGO";
        }
        return "";
    }
    //FUNCION QUE ESPERA UN TIEMPO
    public void pausa(long sleeptime) {
        try {
            Thread.sleep(sleeptime);
        } catch (InterruptedException ex) {
        }
    }
	 private void leemosDatos() {
         //CREAMOS UN ARREGLO QUE ALAMACENARA LOS REGISTROS DEL EXCEL
         List<String[]> registrosDatos = new ArrayList<>();
         try (CSVReader reader = new CSVReader(new FileReader(localPath + "\\datos.csv"))) {
             //VARIABLE QUE ALMACENARA UNA LINEA
             String[] linea;
             
             System.out.println(registrosDatos);
             //NOS MOVEMOS LINEA POR LINEA PARA ALMACENAR TODOS OS REGISTROS EN EL ARREGLO
             while ((linea = reader.readNext()) != null) {
                 if (linea[0].equals("Saludo")) {
                     saludo = linea[1];
                 }
                 if (linea[0].equals("Teléfono")) {
                     telefono = linea[0] + ": " + linea[1];
                 }
                 if (linea[0].equals("Dirección")) {
                     direccion = linea[0] + ": " + linea[1];
                 }
                 if (linea[0].equals("Entregas a domicilio")) {
                     domicilio = linea[0] + ": " + linea[1];
                 }
                 if (linea[0].equals("Pagina")) {
                     pagina = linea[0] + ": " + linea[1];
                 }
                 if (linea[0].equals("Lunes")) {
                     lunes = linea[0] + " " + linea[1];
                 }
                 if (linea[0].equals("Martes")) {
                     martes = linea[0] + " " + linea[1];
                 }
                 if (linea[0].equals("Miércoles")) {
                     miercoles = linea[0] + " " + linea[1];
                 }
                 if (linea[0].equals("Jueves")) {
                     jueves = linea[0] + " " + linea[1];
                 }
                 if (linea[0].equals("Viernes")) {
                     viernes = linea[0] + " " + linea[1];
                 }
                 if (linea[0].equals("Sábado")) {
                     sabado = linea[0] + " " + linea[1];
                 }
                 if (linea[0].equals("Domingo")) {
                     domingo = linea[0] + " " + linea[1];
                 }
             }
         } catch (IOException ex) {
             //Logger.getLogger(ChatBotRestaurante.class.getName()).log(Level.SEVERE, null, ex);
        	 System.out.println("no leo este metodo");
         }
	 }
	  private void leemosMenu() {
	        try (CSVReader reader = new CSVReader(new FileReader(localPath + "\\menu.csv"))) {
	            //VARIABLE QUE ALMACENARA UNA LINEA
	            String[] linea;
	            //NOS MOVEMOS LINEA POR LINEA PARA ALMACENAR TODOS OS REGISTROS EN EL ARREGLO
	            while ((linea = reader.readNext()) != null) {
	                registrosMenu.add(linea);
	            }
	            //etapa2();
	        } catch (IOException ex) {
	           // Logger.getLogger(ChatBotRestaurante.class.getName()).log(Level.SEVERE, null, ex);
	        	 System.out.println("este menos  metodo");
	        }
	    }
	  //FUNCION QUE VALIDA SI LA CADENA DE TEXTO ES UN NUMERO
	    public boolean isNumeric(String cadena) {
	        try {
	            Float.parseFloat(cadena);
	            return true;
	        } catch (NumberFormatException nfe) {
	            return false;
	        }
	    }
}
