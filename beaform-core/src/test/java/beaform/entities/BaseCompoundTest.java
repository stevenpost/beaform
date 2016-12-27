package beaform.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

@SuppressWarnings("static-method")
public class BaseCompoundTest {

	@Test
	public void testGetName() {
		final String testname = "testBaseCompound";
		final BaseCompound bc = new BaseCompound(testname);

		assertEquals("This isn't the expected name", testname, bc.getName());
	}

	@Test
	public void testGetDescription() {
		final String testDesc= "testDescription";
		final BaseCompound bc = new BaseCompound("");

		bc.setDescription(testDesc);

		assertEquals("This isn't the expected descrtiption", testDesc, bc.getDescription());
	}

	@Test
	public void testEquals() {
		final BaseCompound tag1 = new BaseCompound("test");
		final BaseCompound tag2 = new BaseCompound("test");
		assertEquals("The base compounds are not equal", tag1, tag2);
	}

	@Test
	public void testEqualsSameObject() {
		final BaseCompound tag1 = new BaseCompound("test");
		assertTrue("The base compounds are not equal", tag1.equals(tag1));
	}

	@Test
	public void testNotEqual() {
		final BaseCompound tag1 = new BaseCompound("test1");
		final BaseCompound tag2 = new BaseCompound("test2");
		assertFalse("The base compounds are equal", tag1.equals(tag2));
	}

	@Test
	public void testNotEqualDifferentType() {
		final BaseCompound tag1 = new BaseCompound("test1");
		final Object tag2 = new Object();
		assertFalse("The base compounds are equal", tag1.equals(tag2));
	}

	@Test
	public void testEqualsHash() {
		final BaseCompound tag1 = new BaseCompound("test");
		final BaseCompound tag2 = new BaseCompound("test");
		assertEquals("The base compounds are not equal", tag1.hashCode(), tag2.hashCode());
	}

	@Test
	public void testNotEqualsHash() {
		final BaseCompound tag1 = new BaseCompound("test1");
		final BaseCompound tag2 = new BaseCompound("test2");
		assertFalse("The base compounds are equal", tag1.hashCode() == tag2.hashCode());
	}
}
