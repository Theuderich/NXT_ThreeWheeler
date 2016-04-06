package threeWheeler;

import java.io.*;
import lejos.nxt.*;

public class MessageLogging {

    private FileOutputStream out = null;
    private DataOutputStream dataOut = null;
    private File data = null;
	
	MessageLogging(String fileName){
		data = new File(fileName);
	    try {
	        out = new FileOutputStream(data);
	      } catch(IOException e) {
	      	System.err.println("Failed to create output stream");
	      	Button.waitForAnyEvent(0);
	      	System.exit(1);
	      }
		
	    dataOut = new DataOutputStream(out);
	}
	
	void addMsg(String msg){
		
		if( dataOut == null)
			return;
		
		long time = System.currentTimeMillis();
		String msgLine = time + "," + msg + "\n";
		try {
			dataOut.writeChars(msgLine);
		} catch (IOException e) {
	      	System.err.println("Failed to write message to file.");
	      	System.err.println(msg);
	      	Button.waitForAnyEvent(0);
	      	System.exit(1);
		}
	}

	void addFunctionMsg(String func, String msg){
		
		if( dataOut == null)
			return;
		
		long time = System.currentTimeMillis();
		String msgLine = time + "," + func + "," + msg + "\n";
		try {
			dataOut.writeChars(msgLine);
		} catch (IOException e) {
	      	System.err.println("Failed to write message to file.");
	      	System.err.println(msg);
	      	Button.waitForAnyEvent(0);
	      	System.exit(1);
		}
	}
	
	
	void closeLog(){
		try {
			out.close();
		} catch (IOException e) {
	      	System.err.println("Failed to close file.");
	      	Button.waitForAnyEvent(0);
	      	System.exit(1);
		}
	}
}
