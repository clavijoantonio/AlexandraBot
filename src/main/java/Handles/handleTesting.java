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
import com.opencsv.CSVReader;



public class handleTesting extends TelegramLongPollingBot {
	int usuarioSel = 0;
    //MENSAJES RECIBIDOS DESDE WA
    String[] mensajesRecibidos = new String[100];
    //LISTA DE CONTACTOS QUE MANEJAREMOS
    String[] contactos = new String[100];
    int[] etapa = new int[100];
  //DIRECCION ACTUAL DEL PROGRAMA
    String localPath = "";
    /*
    VARIABLES DE EXCEL
    */
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
    //CREAMOS UN ARREGLO QUE ALAMACENARA LOS DATOS DEL MENU
    List<String[]> registrosMenu = new ArrayList<>();
    
    
	@Override
	public void onUpdateReceived(Update update) {
		
		SendMessage message=new SendMessage();
		
		String command=update.getMessage().getText();
		
		message.setChatId(update.getMessage().getChatId().toString());
		try {
	        localPath = new File(".").getCanonicalPath();
	    } catch (IOException ex) {
	        //Logger.getLogger(ChatBotRestaurante.class.getName()).log(Level.SEVERE, null, ex);
	    }
		
		try {
			/*if(command.equals("hoy")) {
				message.setText("Buenos dias el menu  de hoy es: sopa de fideos,  carne de cerdo a la plancha, jugo de mora  ");
				execute(message);
			}else {
				message.setText("cual es tu mesaje");
				execute(message);
			}*/
	
			//if (etapa[usuarioSel] == 1) {
			if(command.equals("")) {
			
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
    } catch (Exception e) {
        //java.util.logging.Logger.getLogger(ChatBotRestaurante.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
    }

}
		
          //Etapa 1 Donde se envian los mensajes de los diferentes sorteos
            public void etapa1() {
                //ABRIMOS EL TAB DEL CONTACTO
                String listaOpciones = saludo + "\n1.-Menu del dia de hoy\n2.-Menu de otro dia\n3.-Horarios\n4.-Acerca de nosotros\n";
                //ESCRIBIMOS EL MENSAJE
                //driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys(listaOpciones);
                pausa(500);
                //CERRAMOS CHAT DE WA
               // driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys(Keys.ESCAPE);
                //PASAMOS A LA SIGUIENTE ETAPA
                etapa[usuarioSel] = 2;
            }
            public void etapa2() {
                //VARIABLE QUE RECIBE LAS OPCIONES COLOCADAS POR EL USUARIO DE WA
                int opcion;
                //QUITAMOS ESPACIOS EN BLANCO
                String mensajesRecibidosSinEspacios = mensajesRecibidos[usuarioSel].replace(" ", "");
                //String mensajesRecibidosSinEspacios = "2";
                //VALIDAMO SI REALMENTE SE ENVIO UN NUMERO
                if (isNumeric(mensajesRecibidosSinEspacios)) {
                    opcion = Integer.parseInt(mensajesRecibidosSinEspacios);
                    //SI LA OPCION ESTA ENTRE 1 Y 4
                    if (opcion >= 1 && opcion <= 4) {
                        if (opcion == 1) {
                            String textoMenuDia = "MENU DEL DIA DE HOY\n";
                            //VARIABLE QE CUENTA LOS REGISTROS
                            int cont = 0;
                            //OBTENEMOS EL DIA DE HOY
                            LocalDate today = LocalDate.now();
                            DayOfWeek dayOfWeek = today.getDayOfWeek();
                            //SACACAMOS DACA REGISTRO
                            for (String[] registro : registrosMenu) {
                                //COMENZAMOS A PARTIR DEL 3 ER REGISTRO
                                if (cont >= 2) {
                                    //OBTENEMOS EL DIA DE HOY EN NUMERO
                                    int diaSemanaNum = dayOfWeek.getValue();
                                    //OBTENEMOS LA COLUMNA DEL PRECIO
                                    int columnaPrecio = diaSemanaNum * 2;
                                    //OBTENEMOS LA COLUMNA DE LA COMIDA
                                    int columnaComida = (diaSemanaNum * 2) + 1;
                                    //OBTENEMOS LA COMIDA
                                    String comida = registro[columnaPrecio];
                                    //OBTENEMOS EL PRECIO
                                    String precio = registro[columnaComida];
                                    //CONCATENAMOS EL TEXTO
                                    textoMenuDia = textoMenuDia + comida + ": $" + precio + "\n";
                                }
                                cont++;
                            }
                            if (opcion == 2) {
                                String textoMenuOtroDia = "De que dia quiere saber el menu\n"
                                        + "1.-Lunes\n2.-Martes\n3.-Miercoles\n4.-Jueves\n5.-Viernes\n6.-Sabado\n7.-Domingo\n";
                                //ESCRIBIMOS EL MENSAJE
                               // driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys(textoMenuOtroDia);
                                pausa(500);
                               // driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys(Keys.ESCAPE);
                                etapa[usuarioSel] = 3;
                            }
                            if (opcion == 3) {
                                String textoMenuHorarios = "HORARIOS\n" + lunes + "\n" + martes + "\n" + miercoles + "\n" + jueves + "\n" + viernes + "\n" + sabado + "\n" + domingo + "\n";
                                //ESCRIBIMOS EL MENSAJE
                                //driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys(textoMenuHorarios);
                                pausa(500);
                               // driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys(Keys.ESCAPE);
                                etapa[usuarioSel] = 1;
                            }
                            if (opcion == 4) {
                                String textoMenuAcerca = "ACERCA DE NOSOTROS\n" + telefono + "\n" + direccion + "\n" + domicilio + "\n" + pagina + "\n";
                                //ESCRIBIMOS EL MENSAJE
                                //driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys(textoMenuAcerca);
                                pausa(500);
                               // driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys(Keys.ESCAPE);
                                etapa[usuarioSel] = 1;
                            }
                        } //NO SE SELECCIONO LA OPCION CORRECTA
                        else {
                            //ESCRIBIMOS EL MENSAJE
                            //driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys("Opcion no valida\n");
                            pausa(500);
                           // driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys(Keys.ESCAPE);
                            etapa[usuarioSel] = 1;
                        }
                    } else {
                        //ESCRIBIMOS EL MENSAJE
                        //driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys("Opcion no valida\n");
                        pausa(500);
                        //driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys(Keys.ESCAPE);
                        etapa[usuarioSel] = 1;
                    }
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
                            //driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys(textoMenuDia);
                            pausa(500);
                           // driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys(Keys.ESCAPE);
                            etapa[usuarioSel] = 1;
                        } else {
                            //ESCRIBIMOS EL MENSAJE
                           // driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys("Opcion no valida\n");
                            pausa(500);
                           // driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys(Keys.ESCAPE);
                        }
                    } else {
                        //ESCRIBIMOS EL MENSAJE
                        //driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys("Opcion no valida\n");
                        pausa(500);
                       // driver.findElement(By.xpath(inputTextEnviarWA)).sendKeys(Keys.ESCAPE);
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
                //FUNCION QUE VALIDA SI LA CADENA DE TEXTO ES UN NUMERO
                public boolean isNumeric(String cadena) {
                    try {
                        Float.parseFloat(cadena);
                        return true;
                    } catch (NumberFormatException nfe) {
                        return false;
                    }
                }
                
                //Quitamos las lineas finales
                private String[] removeTrailingQuotes(String[] fields) {
                    String QUOTE = "\"";
                    String result[] = new String[fields.length];
                    for (int i = 0; i < result.length; i++) {
                        result[i] = fields[i].replaceAll("^" + QUOTE, "").replaceAll(QUOTE + "$", "");
                    }
                    return result;
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
                    }
                }
                //CREAMOS UN ARREGLO QUE ALAMACENARA LOS DATOS DEL RESTAURANTE
                private void leemosDatos() {
                    //CREAMOS UN ARREGLO QUE ALAMACENARA LOS REGISTROS DEL EXCEL
                    List<String[]> registrosDatos = new ArrayList<>();
                    try (CSVReader reader = new CSVReader(new FileReader(localPath + "\\datos.csv"))) {
                        //VARIABLE QUE ALMACENARA UNA LINEA
                        String[] linea;
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
                    }
                     
                }
		/*}catch(Exception e){
			e.printStackTrace();
		}*/
            

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return "DemoAlexandraBot";
	}

	@Override
	public String getBotToken() {
		
		return "6714109081:AAGR0OeYpJ2HMvH4Xqks-8ApO9xJ0cHCS2c";
	}
}
