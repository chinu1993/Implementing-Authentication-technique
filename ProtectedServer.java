import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer
{
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException 
	{

		DataInputStream in = new DataInputStream(inStream);

		// IMPLEMENT THIS FUNCTION.
		
		boolean isItTheSame = true;
		int shaDigestLength;
		long timeStamp1, timeStamp2;
		double randNumber1, randNumber2;
		
		//reading the user and password from the inputStream
		String user = in.readUTF();
		String password = lookupPassword(user);
		
		timeStamp1 = in.readLong(); //reading the first timestamp
		timeStamp2 = in.readLong();//reading the second timestamp
		randNumber1 = in.readDouble(); // reading the first random number
		randNumber2 = in.readDouble(); //reading the second random number 
		
		// computing the first sha digest with the info received from the client 
		byte[] shaDigestServer1 = Protection.makeDigest(user, password, timeStamp1, randNumber1);
		
		// computing the second sha digest with the info received from the client
		byte[] shaDigestServer2 = Protection.makeDigest(shaDigestServer1, timeStamp2, randNumber2);
		
		// reading the length for memory allocation
		shaDigestLength = in.readInt();
		//read the value from the client and store it in the array 
		byte[] shaClient = new byte [shaDigestLength];
		in.readFully(shaClient);
		
		//compare the value received from the client with the value we recomputed in the server. 
		isItTheSame = MessageDigest.isEqual(shaClient, shaDigestServer2); 
	
		return isItTheSame;
	}

	protected String lookupPassword(String user) { return "abc123"; }

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		
		ServerSocket s = new ServerSocket(port);
		System.out.println("Server listening...");
		Socket client = s.accept();

		ProtectedServer server = new ProtectedServer();

		if (server.authenticate(client.getInputStream()))
		  System.out.println("Client logged in.");
		else
		  System.out.println("Client failed to log in.");

		s.close();
		System.out.println("connection closed.... ");
	}
}