package mb.dabm.test;

import java.util.StringTokenizer;

public class Example1
{

    public static void main(String[] args)
    {
	String str = "This is the new;\"message or something;like that\";OK anders";

	System.out.println("---- Split by comma ';' ------");
	StringTokenizer st2 = new StringTokenizer(str, ";");

	while (st2.hasMoreElements()) {
	    
	    System.out.println(st2.nextElement());
	}
    }

}
