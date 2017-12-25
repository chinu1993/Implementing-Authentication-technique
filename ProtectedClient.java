import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;

public class ProtectedClient
{
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException 
	{
		DataOutputStream out = new DataOutputStream(outStream);

		// IMPLEMENT THIS FUNCTION.
		long timeStamp1, timeStamp2;
		double randNumber1, randNumber2;
		
		// instantiating the Date object 
		Date date = new Date();
		
		timeStamp1 = date.getTime(); //using the milliseconds from the getTime method as my timestamp
		randNumber1 = Math.random(); //Generating a random number from the randomClass
		
		//Using the first version of the makeDigest method for the first sha Digest
		byte[] shaDigest1 = Protection.makeDigest(user, password, timeStamp1, randNumber1);
		
		
		timeStamp2 = date.getTime(); // generating the second timestamp
		randNumber2 = Math.random(); //Generating the second random number from the randomClass
		
		
		//Using the second version of makeDigest to generate the final shaDigest.
		byte[] shaDigest2 = Protection.makeDigest(shaDigest1, timeStamp2, randNumber2);
		
		
		//writing the diiferent variables to the ubderlying output stream. 
		out.writeUTF(user); 
		out.writeLong(timeStamp1);
		out.writeLong(timeStamp2);
		out.writeDouble(randNumber1);
		out.writeDouble(randNumber2);
		out.writeInt(shaDigest1.length); 
		out.write(shaDigest2);
		
		out.flush();
	}

	public static void main(String[] args) throws Exception 
	{
		//String host = "paradox.sis.pitt.edu";
		String host = "LOCALHOST";
		int port = 7999;
		String user = "George";
		String password = "abc123";
		Socket s = new Socket(host, port);

		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());

		s.close();
	}
}