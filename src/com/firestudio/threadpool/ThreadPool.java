package com.firestudio.threadpool;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

final public class ThreadPool {
	boolean bExit = false;
	
	final ReentrantLock lock = new ReentrantLock();
	final Condition cond = lock.newCondition();
	
	/**
	 * 任务队列
	 */
	List<Task> tasks = new ArrayList<Task>();
	
	/**
	 * 
	 * @param size -- 线程池大小 
	 */
	public ThreadPool(int size){
		if( size<=0 ){
			throw new IllegalArgumentException();
		}
		for(int i=0;i<size;i++){
			new Thread(new TheThread(this)).start();
		}
	}
	
	/**
	 * 
	 * @param t
	 */
	public void addTask(Task... ts){
		lock.lock();
		for(Task t:ts){
			tasks.add(t);
		}
		if( ts.length>1 ){//唤醒多少线程?
			cond.signalAll();
		}
		else{
			cond.signal();
		}
		lock.unlock();
	}
	
	/**
	 * 
	 * @param l
	 */
	public void addTask(List<Task> l){
		lock.lock();
		tasks.addAll(l);
		if( l.size() > 1 ){//唤醒多少线程?
			cond.signalAll();
		}
		else{
			cond.signal();
		}
		lock.unlock();
	}
	
	/**
	 * Close and destroy the threadpool
	 */
	public void close(){
		lock.lock();
		bExit = true;
		cond.signalAll();
		lock.unlock();
	}
}

/**
 * Inner Thread to exec the task!
 * @author Administrator
 *
 */
class TheThread implements Runnable{
	ThreadPool pool = null;
	long tid = 0;
	public TheThread(ThreadPool pool){
		this.pool = pool;
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.tid = Thread.currentThread().getId();
		System.out.println("线程: " + tid + " 已经启动! ");
		
		while(true){
			Task task = null;
			//////////////////
			try{
				pool.lock.lock();
				while ( (pool.tasks.size()==0) && (!pool.bExit) ) {
					System.out.println("线程: " + tid + " 进入睡眠! ");
					pool.cond.await();  //await now!
				}
				
				if( pool.bExit ){
					break;  //here will execute the finally block!
				}
				task = pool.tasks.remove(0);
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}finally{
				pool.lock.unlock();
			}
			///////////////////
			//do work here. and pool's lock is unlocked!
			try {
				if( null != task ){//avoid error!
					System.out.println("线程: " + tid + " 在执行! ");
					task.start();  //run task now!
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}//end of while!
		System.out.println("线程: " + tid + " 已经退出! ");
	}

}