package com.firestudio.threadpool;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.sun.jmx.snmp.tasks.ThreadService;

/**
 * 线程池的实现。仅供测试使用。
 * @author 
 *
 */
final public class ThreadPool {
	boolean bExit = false;
	
	final ReentrantLock lock = new ReentrantLock();
	final Condition cond = lock.newCondition();
	
	/**
	 * 线程数组
	 */
	TheWorkerThread[] threads = null;
	
	/**
	 * 是否发出了通知结束消息。
	 */
	boolean bNotifyFinished = true;
	
	/**
	 * 管理线程的条件通知
	 */
	final Condition cond_master = lock.newCondition();
	
	/**
	 * 任务队列
	 */
	List<ITask> tasks = new ArrayList<ITask>();
	
	/**
	 * 
	 * @param size -- 线程池大小 
	 */
	public ThreadPool(int size){
		if( size<=0 ){
			throw new IllegalArgumentException();
		}
		threads = new TheWorkerThread[size];
		
		for(int i=0;i<size;i++){
			threads[i] = new TheWorkerThread(this, i+1);
			threads[i].start();
		}
	}
	
	/**
	 * waiting when all tasks finished, then call return
	 */
	public void awaitFinished(){
	
		try {
			lock.lock(); //first get lock!
			cond_master.await(); //release the lock and await signal to finished!
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally{
			lock.unlock();
		}
		
	}
	/**
	 * 
	 * @param ts
	 */
	public void addTask(ITask... ts){
		List<ITask> list = Arrays.asList(ts);
		addTask(list);
	}
	
	/**
	 * 
	 * @param l
	 */
	public void addTask(List<ITask> l){
		
		lock.lock();  //first lock
		
		bNotifyFinished = false; //need notify finished!
		
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
		//
		for(TheWorkerThread th: threads){
			try {
				th.join(); //waiting all thread to exit!
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

/**
 * Inner Thread to exec the task!
 * @author Administrator
 *
 */
class TheWorkerThread extends Thread{
	ThreadPool pool = null;
	long thread_id = 0;
	public TheWorkerThread(ThreadPool pool, int num){
		this.pool = pool;
		thread_id = num;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//this.tid = Thread.currentThread().getId();
		//System.out.println("线程: " + tid + " 已经启动! ");
		
		while(true){
			ITask task = null;
			//////////////////
			try{
				pool.lock.lock();
				while ( (pool.tasks.size()==0) && (!pool.bExit) ) {
					if( !pool.bNotifyFinished ){
						pool.bNotifyFinished = true;
						pool.cond_master.signalAll();   //wake up all master'thread that waiting this condition!
					}
					//System.out.println("线程: " + thread_id + " 进入睡眠! ");
					pool.cond.await();  //await now! and release the pool.lock
				}//end of while waiting for task to do!
				
				if( pool.bExit ){
					break;  //here will execute the finally block!
				}
				task = pool.tasks.remove(0);
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}finally{
				//System.out.println("线程: " + thread_id + " unlock! ");
				pool.lock.unlock();
			}
			///////////////////
			//do work here. and pool's lock is unlocked!
			try {
				if( null != task ){//avoid error!
					//System.out.println("线程: " + thread_id + " 在执行! ");
					task.start();  //run task now!
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			finally{
				//here!
			}
		}//end of while!
		//System.out.println("线程: " + tid + " 已经退出! ");
	}

}