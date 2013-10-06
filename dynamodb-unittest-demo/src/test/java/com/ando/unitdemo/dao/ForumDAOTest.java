package com.ando.unitdemo.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ando.nosqlunit.annotation.DataSetLocation;
import com.ando.unitdemo.model.Forum;

@DataSetLocation(value = "/com/ando/unitdemo/dao/dao-dataset.json")
public class ForumDAOTest extends AbstractDAOTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDAOTest.class);

	private ForumDAO forumDAO;

	@Test
	public void testFindAll() {
		LOGGER.info("starting testFindAllmethod...");
		forumDAO = injector.getInstance(ForumDAO.class);

		LOGGER.debug("verifying the forumDAO is not null");
		assertNotNull(forumDAO);
		LOGGER.info("forumDAO is not null.");
		List<Forum> forums = forumDAO.findAll();
		assertNotNull(forums);
		assertTrue("list must contain 2 items and not : " + forums.size(), forums.size() == 2);
	}

	@Test
	public void testSave() {
		LOGGER.info("starting testSave...");
		forumDAO = injector.getInstance(ForumDAO.class);

		LOGGER.debug("verifying the forumDAO is not null");
		assertNotNull(forumDAO);
		LOGGER.info("forumDAO is not null.");

		// 1. saving non existing entity
		Forum forum = new Forum();
		forum.setName("Forum test");
		forum.setCategory("Category test");
		forum.setMessage(3);
		try {
			forumDAO.save(forum);
		} catch (Exception e) {
			LOGGER.error("error occurred whe trying to save forum entity : " + e.getLocalizedMessage());
			fail("error occurred when trying to save forum entity : " + e.getLocalizedMessage());
		}

		List<Forum> forums = forumDAO.findAll();
		assertNotNull(forums);
		assertTrue("list must contain 3 items and not : " + forums.size(), forums.size() == 3);

		// 2. saving with existing entity
		forum = new Forum();
		forum.setName("Amazon S3");
		try {
			forumDAO.save(forum);
			fail("must throw exception before this line");
		} catch (Exception e) {
			assertTrue("error occurred when trying to save forum entity : " + e.getLocalizedMessage(), Boolean.TRUE);
		}

		// 3. saving with entity having null identifier
		forum = new Forum();
		forum.setName(null);
		try {
			forumDAO.save(forum);
			fail("must throw exception before this line");
		} catch (Exception e) {
			assertTrue("error occurred when trying to save forum entity : " + e.getLocalizedMessage(), Boolean.TRUE);
		}

	}
}
