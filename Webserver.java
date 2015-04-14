/* 
 * @Author: Lava Laurent & Werenne Aurélien
 * @Date: 12/04/2015
 *
 *
 * Explanation
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
public class WebServer 
{
	/* -- Properties -- */
	private int portnumber; 
	private ServerSocket serverSock;



	/* -- Main method -- */
	public static void main(String argv[]) throws Exception
	{
		EchoServer server = new EchoServer(8995); 	// TODO: modify groupnbr
		while(true) // TODO: argument maximum number of threads
		{
			Socket clientSock = server.serverSock.accept();
			System.out.println("Connected to " + clientSock.getInetAddress());
			ServerThread mythread = new ServerThread(clientSock);
			mythread.start();
		}
	}



	/* -- Constructor -- */
	public WebServer(int portnumber) throws IOException
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
			String temp = reading();
			Request req = new Request(temp);
			String entityBody = make_entity(req);
			String headResponse = make_headResponse(req);
			writing(headResponse, entityBody);
			// TODO: close connection if non-persistent
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
		// TODO: lava tu sais mettre ta version? La mienne n'est pas très orthodoxe
	}



	/* -- Construct entityBody in function of request -- */
	private String make_entity(Request req) throws IOException
	{
		// TODO
	}


	/* -- Construct statusline and headerlines in function of request -- */
	private String make_headResponse(Request req) throws IOException
	{
		// TODO
	}



	/* -- Writing message to client -- */
	private void writing(String headResponse, String entityBody) throws IOException
	{
		PrintWriter msg = new PrintWriter(outs);

		msg.println(headResponse);
		msg.println("\r\n");

		msg.println(entityBody);
		msg.println("\r\n\r\n");

		msg.flush();
	}


	/* -- Extracts eventual arguments from request -- */
	private void check_url(String url) throws IOException
	{
		// TODO
	}

	
}




/* ------------------------------------------------------------------------------------------- */
/* 						  Class representing a certain request 								   */
/* ------------------------------------------------------------------------------------------- */
class Request
{
	/* -- Properties -- */
	private String request;
	private String method, url, version;
	private Map<String, String> arguments;
	private Map<String, String> headerlines;
	private Bool validRequest;



	/* -- Constructor -- */
	public Request(String request) throws IOException
	{ 
		this.request = request;
		validRequest = true;
		method = "";
		url = "";
		arguments = new Map<String,String>();
		extract();	
	}



	/* -- Returns url -- */ 
	public String get_url() { return url; }



	/* -- Returns argument value, empty if argument name is not set -- */ 
	public String get_argument(String argumentName) 
	{ 
		if (arguments.containsKey(argumentName))
			return arguments.get(argumentName);
		else 
			return "";
	}



	/* -- Returns headerline value of key, empty if name of headerline is not set -- */ 
	public String get_headerline(String headerlineName) 
	{ 
		if (headerlines.containsKey(headerlineName))
			return headerlines.get(headerlineName);
		else 
			return ""; 
	}



	/* -- Return true if request is valid, false else -- */ 
	public is_valid() { return validRequest; }



	/* -- Extracts everything from request and does a checkup -- */
	private void extract_checkup() throws IOException
	{
		String word = "";
		int i; 


		/* Extract method */
		i = request.indexOf(" ");
		method = request.substring(0, i-1);

		if (method != "GET" && method != "POST")
		{
			validRequest = false;
			return;
		}


		/* Extract url and eventual arguments */
		request = request.substring(i); 	
		i = request.indexOf("/"); 			// Take begin at '/'
		request = request.substring(i); 
		i = request.indexOf(" ");  			// Take end before next space
		url = request.substring(0, i-1);


		if (url.indexOf("?") != -1) 	// Extract arguments and url
		{
			int j = url.indexOf("?");  		// Take begin of args after '?'
			String args = url.substring(j+1);
			url = url.substring(0, j-1); 	// Extract url before '?'

			while (true)
			{
				int k = args.indexOf("="); 		// Take end of argumentname before '='
				if (k == -1)
				{
					validRequest = false;
					return;
				}

				int l = args.indexOf("&"); 		// Take end of argumentvalue before '&'

				if (l == -1) // If last argument of url
				{
					if (l+1 >= args.length()) 	// Empty argument
						break;

					arguments.put(args.substring(0, k-1), args.substring(k+1)); 	// Put argname and argvalue in map
					break;
				}

				arguments.put(args.substring(0, k-1), args.substring(k+1, l-1)); 	// Put argname and argvalue in map
				args = args.substring(l+1); 	// Go to next argument
			} 
		}


		/* Extract http version */
		request = request.substring(i); 
		i = request.indexOf("H"); 		// Take begin at 'H'
		request = request.substring(i);
		i = request.indexOf("\n");  	// Take end before return carriage
		version = request.substring(0, i-1);



		/* Extract headerlines */
		while (true)
		{
			request = request.substring(i+1); 	// Take begin of headerline after return carriage
			i = request.indexOf("\n"); 			// Take end of headerline before return carriage

			String headerline = request.substring(0, i-1);
			if (request.substring(i-2, i-1) = "\r") 	// If '\r' before return carriage, end of headerlines
			{
				int j = headerline.indexOf(":");
				if (j != -1)
					headerlines.put(headerline.substring(0, j-1), headerline.substring(j+2));	
				break;
			}

			int j = headerline.indexOf(":"); 	// Take before and after 
			headerlines.put(headerline.substring(0, j-1), headerline.substring(j+2)); 
		}



		/* Extract entity body if there is one */
		if (request.substr(i+1, i+2) != "\r\n")
		{
			validRequest = false;
			return;
		}


		/* If body is empty */
		if (i+3 >= request.length())
			return;



		/* If body contains arguments */
		if (method == "GET")
		{
			validRequest = false;
			return;
		}


		request.substring(i+3);
		while (true)
		{
			int k = args.indexOf("="); 		// Take end of argumentname before '='
			if (k == -1)
			{
				validRequest = false;
				return;
			}

			int l = args.indexOf("&"); 		// Take end of argumentvalue before '&'

			if (l == -1) // If last argument of url
			{
				if (l+1 >= args.length()) 	// Empty argument
					break;

				arguments.put(args.substring(0, k-1), args.substring(k+1)); 	// Put argname and argvalue in map
				break;
			}

			arguments.put(args.substring(0, k-1), args.substring(k+1, l-1)); 	// Put argname and argvalue in map
			args = args.substring(l+1); 	// Go to next argument
		}
	}	
}






	


