package mb.dabm.helper;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Helper
{

    public static boolean isNumeric(String str)
    {
	return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isAlphabeticAndLengh4(String str)
    {
	return str.matches("[A-Z]{4}");
    }
    
    /**
     * Converter Data em Long
     * @param now
     * @return
     */
    public static long convertDateToLong(Date now) {
	//Date now = new Date();
	long ut3 = now.getTime() / 1000L;
	System.out.println(ut3);
	
	return ut3;
    }

    /**
     * converte um Int/Long para data formatada
     * @param unixSeconds
     * @return
     */
    public static String convertLongToDate(long unixSeconds)
    {
	// convert seconds to milliseconds
	Date date = new java.util.Date(unixSeconds * 1000L);
	// the format of your date
	SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
	// give a timezone reference for formatting (see comment at the bottom)
	sdf.setTimeZone(java.util.TimeZone.getTimeZone("America/Sao_Paulo"));
	String formattedDate = sdf.format(date);
	System.out.println(formattedDate);
	
	return formattedDate;
    }

    /**
     * enviar alerta no sistema
     * 
     * @param type
     * @param titulo
     * @param cabecalho
     * @param conteudo
     */
    public static void enviarAlerta(AlertType type, String titulo, String cabecalho, String conteudo)
    {
	Alert dialog = new Alert(type);
	dialog.setTitle(titulo);
	dialog.setHeaderText(cabecalho);
	dialog.setContentText(conteudo);
	dialog.showAndWait();
    }

    /**
     * http://code.makery.ch/blog/javafx-dialogs-official/ enviar alerta no sistema
     * 
     * @param type
     * @param titulo
     * @param cabecalho
     * @param conteudo
     */
    public static Optional<ButtonType> enviarPergunta(AlertType type, String titulo, String cabecalho, String conteudo)
    {
	Alert dialog = new Alert(type);
	dialog.setTitle(titulo);
	dialog.setHeaderText(cabecalho);
	dialog.setContentText(conteudo);
	// dialog.showAndWait();

	return dialog.showAndWait();
    }

    /**
     * Right way to delete a non empty directory in Java
     * 
     */
    public static boolean deleteDirectory(File dir)
    {
	if (dir.isDirectory()) {
	    File[] children = dir.listFiles();
	    for (int i = 0; i < children.length; i++) {

		boolean success = deleteDirectory(children[i]);

		if (!success) {
		    return false;
		}
	    }
	}
	// either file or an empty directory
	System.out.println("removing file or directory : " + dir.getName());
	return dir.delete();
    }

    /**
     * Responsavel por abrir o arquivo especificado
     * 
     * @param file
     */
    public static void openFile(File file)
    {
	Desktop desktop = Desktop.getDesktop();

	try {
	    desktop.open(file);
	} catch (IOException ex) {
	    // Logger.getLogger(SegmentVController.class.getName()).log(Level.SEVERE, null,
	    // ex);
	}
    }
}
