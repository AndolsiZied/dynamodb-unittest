package com.ando.unitdemo.dao;

import org.junit.Before;
import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ando.nosqlunit.rule.DynamoDBRule;
import com.ando.unitdemo.di.TestModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public abstract class AbstractDAOTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDAOTest.class);

	protected Injector injector;

	@Rule
	public DynamoDBRule rule = new DynamoDBRule();

	@Before
	public void setUp() throws Exception {
		LOGGER.info("starting setUp method...");
		injector = Guice.createInjector(new TestModule());
	}

}
