/**
 * 
 */
package com.firestudio.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * @author wangyq
 *
 */
public interface Protocolable {
	/**
	 * 
	 * @param key
	 * @throws IOException
	 */
	public void handleAccept(SelectionKey key) throws IOException;
	
	/**
	 * 
	 * @param key
	 * @throws IOException
	 */
	public void handleRead(SelectionKey key) throws IOException ;
	
	/**
	 * 
	 * @param key
	 * @throws IOException
	 */
	public void handleWrite(SelectionKey key) throws IOException;
}
