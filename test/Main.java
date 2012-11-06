import com.firestudio.threadpool.*;



public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
