package com.firestudio.server;

import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.nio.channels.*;
import java.util.Iterator;
import java.io.*;   // for IOException and Input/OutputStream

/**
 * Tcp Echo Server implementation using nio 
 * @author wangyq
 *
 */
public class EchoTcpServer {

	/**
	 * Server wait time out 
	 */
	private static final int TIMEOUT = 3000; // Wait timeout (milliseconds)
	
	/**
	 * 
	 */
	private static int count = 0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args)  throws IOException{
		// TODO Auto-generated method stub
		int echoServPort = 23;  //default port number
	    if (args.length >= 1) { // Test for correct # of args
	    	echoServPort = Integer.parseInt(args[0]); // Server port
	        //throw new IllegalArgumentException("Parameter(s): <Port>");
	      }

	      //echoServPort = Integer.parseInt(args[0]); // Server port
	      
	      // Create a selector to multiplex listening sockets and connections
	      Selector selector = Selector.open();
	      
	      ServerSocketChannel listnChannel = ServerSocketChannel.open();
	      listnChannel.socket().bind(new InetSocketAddress(echoServPort));
	      listnChannel.configureBlocking(false); // must be nonblocking to register
	      // Register selector with channel. The returned key is ignored
	      listnChannel.register(selector, SelectionKey.OP_ACCEPT);
	      
	      System.out.println("Server get ready at port : " + echoServPort);
	      
	      while(true){// Run forever, processing available I/O operations
	          // Wait for some channel to be ready (or timeout)
	          if (selector.select(TIMEOUT) == 0) { // returns # of ready chans
	              //System.out.print(".");
	              continue;
	            }

	            // Get iterator on set of keys with I/O to process
	            Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
	            
	            while (keyIter.hasNext()) { //process all key with I/O already
	              SelectionKey key = keyIter.next(); // Key is bit mask
	              // Server socket channel has pending connection requests?
	              if (key.isAcceptable()) {
	                //protocol.handleAccept(key);
	            	//  handleAccept(key);
	            	  count++;
	            	  new EchoProtocol(count).handleAccept(key);
	            	  
	              }
	              // Client socket channel has pending data?
	              if (key.isReadable()) {
	                //protocol.handleRead(key);
	            	  ((Protocolable)key.attachment()).handleRead(key);
	              }
	              // Client socket channel is available for writing and
	              // key is valid (i.e., channel not closed)?
	              if (key.isValid() && key.isWritable()) {
	                //protocol.handleWrite(key);
	            	  ((Protocolable)key.attachment()).handleWrite(key);
	              }
	              keyIter.remove(); // remove from set of selected keys
	            }
	    	  
	      }// end of while(true)
	      //not reachable!
	      
	}
	
}
