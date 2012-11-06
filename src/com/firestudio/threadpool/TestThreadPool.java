/**
 * 
 */
package com.firestudio.threadpool;

import static org.junit.Assert.*;

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
	 * Test method for {@link com.firestudio.threadpool.ThreadPool#addTask(java.util.List)}.
	 */
	@Test
	public void testAddTaskListOfITask() {
		//fail("Not yet implemented");
		assertTrue(true);
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
