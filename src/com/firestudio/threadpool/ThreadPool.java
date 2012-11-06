package com.firestudio.threadpool;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程池的实现。仅供测试使用。
 * @author siwind
 *
 */
final public class ThreadPool {
	/**
	 * 是否结束线程池中的线程。
	 */
	boolean bExit = false;
	
	final ReentrantLock lock = new ReentrantLock();
	final Condition cond = lock.newCondition();
	
	/**
	 * 线程数组
	 */
	TheWorkerThread[] threads = null;
	
	/**
	 * 是否需要发出了任务结束的 通知消息
	 */
	boolean bNotifyFinished = false;
	
	/**
	 * 管理线程的条件通知
	 */
	final Condition cond_master = lock.newCondition();
	
	/**
	 * 任务队列
	 */
	//List<ITask> tasks = new ArrayList<ITask>(); //底层是Object[] array来实现的, 在头部删除的性能不好。
	List<ITask> tasks = new LinkedList<ITask>();  //反复插入/删除时性能更好. Task队列中有反复的添加/删除操作
	
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
			threads[i] = new TheWorkerThread(i+1);
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
		
		bNotifyFinished = true; //need notify finished!
		
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
	 * this method will finish all threads and would waiting all tasks to finished!
	 * call awaitFinished() first before call this method will make sure all tasks have been finished!
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
	
	//============================================
			/**
			 * Inner Thread to exec the task!
			 * @author Administrator
			 *
			 */
			class TheWorkerThread extends Thread{
				//ThreadPool pool = null;
				long thread_id = 0;
				public TheWorkerThread( int num){
					//this.pool = pool;
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
							lock.lock();
							while ( (tasks.size()==0) && (!bExit) ) {
								if( bNotifyFinished ){ //need signal master thread that all tasks have finished 
									bNotifyFinished = false;  //already notify the message, so set it to false!
									cond_master.signalAll();   //wake up all master'thread that waiting this condition!
								}
								//System.out.println("线程: " + thread_id + " 进入睡眠! ");
								cond.await();  //await now! and release the pool.lock
							}//end of while waiting for task to do!
							
							if( bExit ){
								break;  //here will execute the finally block!
							}
							task = tasks.remove(0);
							
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}finally{
							//System.out.println("线程: " + thread_id + " unlock! ");
							lock.unlock();
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
} //end of ThreadPool

