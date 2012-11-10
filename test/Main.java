import java.util.ArrayList;

import com.firestudio.threadpool.*;

public class Main {

	public static void testQueue(){
		ArrayQueue<Integer> a = new ArrayQueue<Integer>();
		a.push(10);
		a.push(-5);
		a.push(250);
		Integer b[] = {1,3,5,7,9,11,13,15};
		Integer c[] = {2,4,6,8,10};
		a.addAll(b);
		a.addAll(c);
		
		while(!a.isEmpty() ){
			System.out.print(a.pop() + "  ");
		}
		System.out.println();
	}
	public static void testThreadPool(){
		try {

			ThreadPool pool = new ThreadPool(2);
			for (int i = 0; i < 10; i++) {
				pool.addTask(new MyTask(i + 1),new MyTask(i + 11),new MyTask(i + 21));
			}

			pool.awaitFinished();
			pool.close();  //destroy pool!
			
			System.out.println("所有线程运行结束!");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		testQueue();
		
		testThreadPool();

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
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

}
