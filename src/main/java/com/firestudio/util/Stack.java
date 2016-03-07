package com.firestudio.util;

/**
 * Created by wang on 16-1-13.
 */
public class Stack<T> {

    final static int INIT_SIZE = 16;
    final static int INC_SIZE = 16;

    protected Object[] data = null;
    protected int incSize = INC_SIZE;
    protected int headPtr = 0;

    public Stack(){
        this(INIT_SIZE,INC_SIZE);
    }
    public Stack(int initSize){
        this(initSize,INC_SIZE);
    }
    /**
     *
     * @param initSize
     * @param incSize
     */
    public Stack(int initSize, int incSize){
        initSize = initSize>0? initSize:INIT_SIZE;
        incSize = incSize>0? incSize:INC_SIZE;

        this.data = new Object[initSize];
        this.incSize = incSize;
        headPtr = 0;
    }

    public boolean isEmpty(){
        return headPtr <=0;
    }

    public T Top(){
        if( isEmpty() ) return null;
        return (T)data[headPtr-1];
    }

    public T Pop(){
        if( isEmpty() ) return null;
        headPtr--;
        return (T)data[headPtr];
    }

    public T Push(T val){
        if( headPtr >= data.length ){
            Object[] newData = new Object[data.length+incSize];
            System.arraycopy(data,0,newData,0,data.length);  //copy array!
            this.data = newData;
        }
        this.data[headPtr++] = val;
        return val;
    }

    public static void main(String[] args) {
        Stack<Integer> st = new Stack<Integer>();

        for( int i=10;i<100;i++){
            st.Push(i);
        }

        while( !st.isEmpty() ){
            System.out.print(st.Pop() + " ");
        }
        System.out.println();
    }
}
