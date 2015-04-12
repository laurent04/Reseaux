/* 
 * @Author: Lava Laurent & Werenne Aurélien
 * @Date: 12/04/2015
 *
 *
 * Explanation.
 *
 *
 */



import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;






/* ------------------------------------------------------------------------------------------- */
/* 								Main class of server 										   */
/* ------------------------------------------------------------------------------------------- */
public class EchoServer 
{
	/* -- Properties -- */
	private static int portnumber; 
	private static ServerSocket serverSock;



	/* -- Main method -- */
	public static void main(String argv[]) throws Exception
	{
		EchoServer server = new EchoServer(8995);
		while(true)
		{
			Socket clientSock = server.serverSock.accept();
			System.out.println("Connected to " + clientSock.getInetAddress());
			ServerThread mythread = new ServerThread(clientSock);
			mythread.start();
		}
	}



	/* -- Constructor -- */
	public EchoServer(int portnumber) throws IOException
	{
		this.portnumber = portnumber;
		serverSock = new ServerSocket(portnumber);
	}
}




/* ------------------------------------------------------------------------------------------- */
/* 								Thread class of server 										   */
/* ------------------------------------------------------------------------------------------- */
class ServerThread extends Thread
{
	/* -- Properties -- */
	private Socket clientSock;
	private OutputStream outs;
	private InputStream ins;



	/* -- Run method -- */
	@Override
	public void run()
	{
		try
		{	
			// TODO: reading, writing and others				 
		}
		catch(Exception any)
		{
			System.err.println("[ERROR] " + any);
		}
	}




	/* -- Constructor -- */
	public ServerThread(Socket _clientSock) throws IOException
	{ 
		clientSock = _clientSock; 
		outs = clientSock.getOutputStream();
		ins = clientSock.getInputStream();
	}



	/* -- Reading request -- */
	private String reading() throws IOException
	{
		// TODO: Lava tu sais mettre ta méthode stp, la mienne marche mais est peu orthoxe
	}


	/* -- Parsing request -- */
	private void parse(String request) throws IOException
	{
		// TODO: retourne les statusLine et entityBody appropriés en fonction de la requête
	}


	/* -- Writing response -- */
	private void writing(String statusLine, String entityBody) throws IOException
	{
		PrintWriter msg = new PrintWriter(outs);

		msg.println(statusLine);
		msg.println("  Server: WebServer ");
		// TODO: ajout headerlines nécessaire
		msg.println("\r\n");
		msg.println("<html>" + entityBody + "</html>");
		msg.println("\r\n\r\n");

		msg.flush();
	}
}








	


