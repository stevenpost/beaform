package beaform.dao;

import static org.junit.Assert.assertFalse;

import java.util.concurrent.CountDownLatch;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

@SuppressWarnings("static-method")
public class ShutDownHookTest {

	@Test
	public void testHook() throws InterruptedException {
		final EntityManagerFactory entityManagerFact = Persistence.createEntityManagerFactory("test");
		final EntityManager entityManager = entityManagerFact.createEntityManager();
		final ShutDownHook hook = new ShutDownHook(entityManager, entityManagerFact);
		final CountDownLatch latch = new CountDownLatch(1);

		Runnable withsync = new Runnable() {

			@Override
			public void run() {
				hook.run();
				latch.countDown();
			}
		};
		Thread thread = new Thread(withsync);
		thread.start();
		latch.await();
		assertFalse("Entity manager isn't closed", entityManager.isOpen());
		assertFalse("Entity manager factory isn't closed", entityManagerFact.isOpen());
	}

	@Test
	public void testHookAlreadyClosed() throws InterruptedException {
		final EntityManagerFactory entityManagerFact = Persistence.createEntityManagerFactory("test");
		final EntityManager entityManager = entityManagerFact.createEntityManager();
		final ShutDownHook hook = new ShutDownHook(entityManager, entityManagerFact);

		entityManager.close();
		entityManagerFact.close();

		final CountDownLatch latch = new CountDownLatch(1);

		Runnable withsync = new Runnable() {

			@Override
			public void run() {
				hook.run();
				latch.countDown();
			}
		};
		Thread thread = new Thread(withsync);
		thread.start();
		latch.await();
		assertFalse("Entity manager isn't closed", entityManager.isOpen());
		assertFalse("Entity manager factory isn't closed", entityManagerFact.isOpen());
	}
}
