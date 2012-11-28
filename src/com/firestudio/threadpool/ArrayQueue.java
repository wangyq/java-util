package com.firestudio.threadpool;

import java.util.Collection;

public class ArrayQueue<T> {
	/**
	 * default queue size
	 */
	final static int INIT_SIZE = 1;
	/**
	 * 1 - mean 0.5 ( >>1)
	 * 2 - mean 0.25 (>>2)
	 * 3 - mean 0.125(>>3)
	 */
	private int incRatio = 2 ;
	
	/**
	 * Current queue size
	 */
	private int size = INIT_SIZE;

	/**
	 * 
	 */
	private int head = 0;
	/**
	 * 
	 */
	private int tail = 0;
	
	private Object[]  data = null; 
	
	/**
	 * using default increament ratio to resize!
	 */
	private void reSize(){
		reSize(0);
	}
	/**
	 * 
	 */
	private void reSize(int incSize){
		int newIncSize = (size>>incRatio);
		if( newIncSize<incSize ) {
			newIncSize = incSize;
		}
		if( newIncSize<=0 ) { //make sure inc size is not 0!
			newIncSize = size;
		}
		
		int newSize = size + newIncSize ;
		Object[]  newData = new Object[newSize];
		int i = 0;
		int index = 0;
		if( head <= tail ){
			for( i=head; i<tail ; i++, index++){
				newData[index] = data[i];  
			}
		} else{
			for( i=head;i<size;i++,index++){
				newData[index] = data[i]; 
			}
			for(i=0;i<tail;i++,index++){
				newData[index] = data[i]; 
			}
		}//end of resize
		head = 0;
		tail = index;
		size = newSize;
		data = newData;
	}
	/**
	 * 
	 */
	public ArrayQueue(  ){
		this(INIT_SIZE);
	}
	public ArrayQueue( int initSize ){
		if( initSize<=0 ){
			throw new IllegalArgumentException("Illegal initSize: " + initSize);
			
		}
		size = initSize;
		data = new Object[size];
	}
	/**
	 * 
	 * @return true if queue is full, otherwise false
	 */
	public boolean isEmpty(){
		return head == tail;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isFull(){
		return (tail +1 == head ) || ((head==0) && (tail == (size-1)));
	}
	
	/**
	 * 
	 * @return
	 */
	public int curSize(){
		int c = 0;
		if( head <= tail ){
			c =  tail - head;
		} else {
			c = tail + (size-head);
		}
		return c;
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public T push( T value ){
		if( this.isFull() ){ //resize the queue
			reSize();
		}
		data[tail] = value;
		tail = (tail+1)%size;
		return value;
	}
	
	/**
	 * 
	 * @return head queue value,  return null if queue is empty
	 */
	public T pop(){
		if( this.isEmpty() ){
			return null;
		}
		T oldValue = (T)data[head];
		head = (head+1)%size;
		return oldValue;
	}
	/**
	 * 
	 * @param a
	 */
	public void addAll(T[] a){
		if( a == null ) return;
		if( a.length <= 0 ) return;
		
		if( curSize() + a.length > size ){
			reSize(a.length);
		}
		int i = 0;
		int index = 0;
		if( head <= tail ){//be careful!
			for(i=tail; (i<size) && (index < a.length); i++,index++){
				data[i] = a[index]; 
			}
			if( index < a.length ){ //not copy finished!
				for(i=0;(i<head) && (index<a.length); i++,index++){
					data[i] = a[index]; 
				}
			}
		} else {
			for(i=tail;i<head && index<a.length; i++,index++){
				data[i] = a[index]; 
			}
		}
		tail = i;
	}
	/**
	 * 
	 * @param c
	 * @return
	 */
	public void addAll(Collection<? extends T> c){
		Object[] a = c.toArray();
		addAll((T[])a);
	}
}
