package beaform;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.TransactionManager;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform;
import org.hibernate.jpa.HibernateEntityManagerFactory;

import beaform.entities.Formula;

public class TestMain {

	public static void main2(String[] args) throws Exception {
		//build the EntityManagerFactory as you would build in in Hibernate ORM
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(
						"ogm-jpa-tutorial");

		//accessing JBoss's Transaction can be done differently but this one works nicely
		TransactionManager tm = extractJBossTransactionManager(emf);

		//Persist entities the way you are used to in plain JPA
		tm.begin();
		EntityManager em = emf.createEntityManager();
		Formula collie = new Formula();
		collie.setName("Collie");
		em.persist(collie);
		String id = collie.getId();
		em.flush();
		em.close();
		tm.commit();


		//Retrieve your entities the way you are used to in plain JPA
		tm.begin();
		em = emf.createEntityManager();
		collie = em.find(Formula.class, id);
		em.flush();
		em.close();
		tm.commit();
		System.out.println(collie.getName());

		tm.begin();
		em = emf.createEntityManager();
		String query = "match (n:Formula) return n";
		List<Formula> bases = em.createNativeQuery(query, Formula.class).getResultList();

		System.out.println(bases.size());

		for (Formula base : bases) {
			System.out.println(base.getName());
		}
		em.flush();
		em.close();
		tm.commit();


		emf.close();
	}

	private static TransactionManager extractJBossTransactionManager(EntityManagerFactory factory) {
		SessionFactoryImplementor sessionFactory =
						(SessionFactoryImplementor) ( (HibernateEntityManagerFactory) factory ).getSessionFactory();
		return sessionFactory.getServiceRegistry().getService( JtaPlatform.class ).retrieveTransactionManager();
	}
}
