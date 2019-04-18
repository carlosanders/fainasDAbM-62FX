package mb.dabm.test;

import java.util.StringTokenizer;

public class LeituraArquivo
{

    public static void main(String[] args)
    {
	//exemplo varios delimitadores
	//String message = "This is the new, message or something like that, OK	anders";
	//exemplo c/Tab
	//String messageTab = "This is the new	message or something like that	OK anders";
	//exemplo c/|
	//String messageTab = "This is the new|message or something like that|OK anders";
	//exemplo c/;
	//String messageTab = "This is the new;message or something like that;OK anders";
	
	String messageTab = "This is the new;\"message or somethinglike that\";OK anders";
	
	String delim = ";"; // insert here all delimitators
	//String delim = " \n\r\t,.;"; // insert here all delimitators
	//String delim = " \n\r,.;"; // insert here all delimitators
	StringTokenizer st = new StringTokenizer(messageTab, delim);
	
	System.out.println("total: " + st.countTokens());
	while (st.hasMoreTokens()) {
	    System.out.println(st.nextToken());	    
	}
	
    }

}
