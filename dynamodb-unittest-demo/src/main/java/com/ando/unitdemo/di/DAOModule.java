package com.ando.unitdemo.di;

import com.ando.unitdemo.dao.ForumDAO;
import com.ando.unitdemo.dao.impl.ForumDAOImpl;
import com.google.inject.AbstractModule;

public class DAOModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ForumDAO.class).to(ForumDAOImpl.class);
	}

}
