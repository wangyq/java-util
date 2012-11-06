//package com.firestudio.threadpool;
import com.firestudio.threadpool.*;

public class TestPool {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {

			ThreadPool pool = new ThreadPool(3);
			for (int i = 0; i < 10; i++) {
				pool.addTask(new MyTask(i + 1),new MyTask(i*20 + 1),new MyTask(i*40 + 1));
			}

			Thread.currentThread().sleep(5000);
			
			pool.close();  //destroy pool!
			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}

class MyTask implements ITask {

	int num = 0;

	public MyTask(int n) {
		this.num = n;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		try{
		
			System.out.println("Hello, task = " + num);
			Thread.currentThread().sleep(10);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

}
