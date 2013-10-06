package com.ando.unitdemo.dao.impl;

import com.ando.unitdemo.dao.ForumDAO;
import com.ando.unitdemo.model.Forum;

public class ForumDAOImpl extends GenericDAOImpl<Forum, String> implements ForumDAO {

	protected ForumDAOImpl() {
		super(Forum.class);
	}

	@Override
	public String getIdentifier(Forum entity) {
		return entity.getName();
	}

}
