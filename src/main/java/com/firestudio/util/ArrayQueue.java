package com.firestudio.util;

import java.util.Collection;

/**
 * This class provide queue implementation using array to get higher performance 
 * @author wangyq
 *
 * @param <T> general type of elements that queue holds 
 */
public class ArrayQueue<T> {
	/**
	 * default queue size
	 */
	final static int INIT_SIZE = 16;

	final static int INC_SIZE = 16;
	/**
	 * 1 - mean 0.5 ( >>1)
	 * 2 - mean 0.25 (>>2)
	 * 3 - mean 0.125(>>3)
	 */
	private static int incRatio = 2 ;

	/**
	 * 
	 */
	private int head = 0;
	/**
	 * 
	 */
	private int tail = 0;

	private int incSize = INC_SIZE;

	/**
	 * 
	 */
	private Object[]  data = null; 
	
	/**
	 * using default increament ratio to resize!
	 */
	private void reSize(){
		reSize(incSize);
	}
	/**
	 * 
	 */
	private void reSize(int incSize){

		int cursize = size();

		int size = data.length;
		if( incSize<=0 ){
			incSize = (size >>> incRatio);  //no-negtive shift!
			if( incSize<=0 ) incSize = INC_SIZE; //at last , the incSize is default value.
		}

		int newSize = size + incSize ;
		Object[]  newData = new Object[newSize];

		int i = 0;
		int index = 0;
		if( head <= tail ){
			System.arraycopy(data,head,newData,0,tail-head);
//			for( i=head; i<tail ; i++, index++){
//				newData[index] = data[i];
//			}
		} else{
			System.arraycopy(data,head,newData,0,data.length-head);
			System.arraycopy(data,0,newData,data.length-head,tail);
//			for( i=head;i<size;i++,index++){
//				newData[index] = data[i];
//			}
//			for(i=0;i<tail;i++,index++){
//				newData[index] = data[i];
//			}
		}//end of resize

		tail = cursize;  //number of copy data.
		head = 0;
		data = newData;
	}
	/**
	 * 
	 */
	public ArrayQueue(  ){
		this(INIT_SIZE,INC_SIZE);
	}
	public ArrayQueue( int initSize ){
		this(initSize,INC_SIZE);
	}
	/**
	 * 
	 * @param initSize
	 */
	public ArrayQueue( int initSize, int incSize ){
		if( initSize<=0 ){
			//throw new IllegalArgumentException("Illegal initSize: " + initSize);
			initSize = INIT_SIZE;
		}
		this.incSize = (incSize>0)?incSize:INC_SIZE;

		data = new Object[initSize];
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
		return (tail +1 == head ) || ((head==0) && (tail == (data.length-1)));
	}
	
	/**
	 * 
	 * @return
	 */
	public int size(){
		int c = 0;
		if( head <= tail ){
			c =  tail - head;
		} else {
			c = tail + (data.length-head);
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
		//tail = (tail+1)%data.length;
		tail++;
		if( tail >= data.length ){
			tail = 0;
		}
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
		//head = (head+1)%data.length;
		head++;
		if( head>=data.length){
			head = 0;
		}
		return oldValue;
	}
	/**
	 * 
	 * @param a
	 */
	public void addAll(T[] a){
		if( a == null ) return;
		if( a.length <= 0 ) return;
		
		if( size() + a.length >= data.length ){//make sure the capacity is enough!
			reSize(a.length);
		}
//		int i = 0;
//		int index = 0;
		if( head <= tail ){//be careful!

			if( a.length > data.length - tail ){ //though just when head==0, position of data.length-1 cann't be writeen.but the capacity is enough.
				System.arraycopy(a,0,data,tail,data.length-tail);  //first copy data
				System.arraycopy(a,data.length-tail,data,0,a.length-(data.length-tail)); //copy the last!

				tail = a.length - (data.length-tail);  //new tail
			} else {
				System.arraycopy(a,0,data,tail,a.length);
				tail += a.length;
			}

//			for(i=tail; (i<data.length) && (index < a.length); i++,index++){
//				data[i] = a[index];
//			}
//			if( index < a.length ){ //not copy finished!
//				for(i=0;(i<head) && (index<a.length); i++,index++){
//					data[i] = a[index];
//				}
//			}
//			tail = i;

		} else {
			System.arraycopy(a,0,data,tail,a.length);
			tail += a.length;
//			for(i=tail;i<head && index<a.length; i++,index++){
//				data[i] = a[index];
//			}

		}

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
