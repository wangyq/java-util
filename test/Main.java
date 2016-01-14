import com.firestudio.threadpool.*;
import com.firestudio.util.ArrayQueue;

public class Main {

	public static void testQueue(){
		ArrayQueue<Integer> queue = new ArrayQueue<Integer>();

		for( int i=10;i<100;i++){
			queue.push(i);
			queue.push(i+3);
			queue.pop();
		}
		queue.push(10);
		queue.push(-5);
		queue.push(250);
		Integer b[] = {1,3,5,7,9,11,13,15};
		Integer c[] = {2,4,6,8,10};
		queue.addAll(b);
		queue.addAll(c);

		while(!queue.isEmpty() ){
			System.out.print(queue.pop() + "  ");
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
