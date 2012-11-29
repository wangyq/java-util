package com.firestudio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class EchoProtocol implements Protocolable {

	/**
	 * 
	 */
	private int c_id = 0;
	
	/**
	 * 
	 */
	private int buf_size = 1024;
	/**
	 * 
	 */
	private ByteBuffer readBuffer = null;
	/**
	 * 
	 */
	private ByteBuffer writeBuffer = null;

	/**
	 * 
	 */
	StringBuffer stringBuffer = new StringBuffer("");
	/**
	 * 
	 */
	CharsetEncoder encoder = null;

	/**
	 * 
	 */
	CharsetDecoder decoder = null;

	/**
	 * Construction
	 */
	public EchoProtocol(int id) {
		c_id = id;
		readBuffer = ByteBuffer.allocate(buf_size);
		writeBuffer = ByteBuffer.allocate(buf_size);

		Charset charset = Charset.forName("UTF-8");
		encoder = charset.newEncoder();
		decoder = charset.newDecoder();

	}

	@Override
	public void handleAccept(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		SocketChannel clntChan = ((ServerSocketChannel) key.channel()).accept();

		System.out.println("[" + c_id + "]: Recieve a client connection from "
				+ clntChan.socket().getRemoteSocketAddress().toString());

		clntChan.configureBlocking(false); // Must be nonblocking to register
		// Register the selector with new channel for read and attach byte
		// buffer
		// clntChan.register(key.selector(), SelectionKey.OP_READ,
		// ByteBuffer.allocate(bufSize));
		// CharBuffer buffer =
		// CharBuffer.wrap("Welcome to Echo Server.  Type \"Quit\" to quit the server. \r\n");
		String str = "[" + c_id + "]: " +  "Welcome to Echo Server.  Please type \"Quit\" to quit the server. \r\n";
		writeBuffer.put(str.getBytes());

		clntChan.register(key.selector(), SelectionKey.OP_WRITE, this);
	}

	@Override
	public void handleRead(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub

		SocketChannel clntChan = (SocketChannel) key.channel();

		// decoder.reset();// clearing any internal state
		boolean bExit = false;
		int Ops = SelectionKey.OP_READ;

		//while (true) { // repeat read until buffer is empty!
		readBuffer.clear();
		int bytesRead = clntChan.read(readBuffer);
		if (bytesRead == -1) { // Did the other end close?
			bExit = true;
			// clntChan.close();
			// System.out.println("Client close connection!");
			//break;
		} else if (bytesRead == 0) {
			System.out.println("Read Buffer is full!");
			//break;
		} else {
			// else if (bytesRead > 0) {
			// Indicate via key that reading/writing are both of interest
			// now.
			// CharBuffer cbuf = decoder.decode(readBuffer);
			String str = new String(readBuffer.array(), 0, bytesRead);
			stringBuffer.append(str);

			// key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);

			// }//end of while read
			if (stringBuffer.lastIndexOf("\n") != -1) {
				System.out.print("[" + c_id + "]: " + stringBuffer);
				// CharBuffer buffer = CharBuffer.wrap(stringBuffer.toString());
				// writeBuffer.put(encoder.encode(buffer));
				writeBuffer.put(stringBuffer.toString().getBytes());
				Ops |= SelectionKey.OP_WRITE;
								
				if( stringBuffer.toString().toLowerCase().indexOf("quit") != -1 ){
					bExit = true;
				}
				
				stringBuffer.setLength(0);
			}
		}
		//}
		 //System.out.println(stringBuffer);
		if (!bExit) {
			// key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			key.interestOps(Ops);
		} else {
			clntChan.close();
			System.out.println("[" + c_id + "]: " +"Client close connection!");
		}
	}

	@Override
	public void handleWrite(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		SocketChannel clntChan = (SocketChannel) key.channel();

		writeBuffer.flip(); // Prepare buffer for writing
		clntChan.write(writeBuffer);

		if (!writeBuffer.hasRemaining()) { // Buffer completely written?
			// Nothing left, so no longer interested in writes
			key.interestOps(SelectionKey.OP_READ);
		}
		writeBuffer.compact(); // Make room for more data to be read in
	}

}
