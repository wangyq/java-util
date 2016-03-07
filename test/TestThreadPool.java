/**
 * 
 */


import static org.junit.Assert.*;

import com.firestudio.util.ITask;
import com.firestudio.util.ThreadPool;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author siwind
 *
 */
public class TestThreadPool {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link ThreadPool#addTask(java.util.List)}.
	 */
	@Test
	public void testAddTaskListOfITask() {
		//fail("Not yet implemented");
		assertTrue(true);
		try {

			ThreadPool pool = new ThreadPool(2);
			for (int i = 0; i < 10; i++) {
				pool.addTask(new MyTestTask(i + 1),new MyTestTask(i + 11),new MyTestTask(i + 21));
			}

			pool.awaitFinished();
			pool.close();  //destroy pool!
			
			System.out.println("所有线程运行结束!");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
class MyTestTask implements ITask {

	int num = 0;

	public MyTestTask(int n) {
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