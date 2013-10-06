package com.ando.unitdemo.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.ando.unitdemo.dao.GenericDAO;
import com.google.inject.Inject;

public abstract class GenericDAOImpl<Type, IdType extends Serializable> implements GenericDAO<Type, IdType> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericDAOImpl.class);
	private Class<Type> typeClass;

	@Inject
	protected DynamoDBMapper dynamoDBMapper;

	protected GenericDAOImpl(Class<Type> aClazz) {
		this.typeClass = aClazz;
	}

	public List<Type> findAll() {
		return dynamoDBMapper.scan(typeClass, new DynamoDBScanExpression());
	}

	public Class<Type> getTypeClass() {
		return this.typeClass;
	}

	@Override
	public void save(Type paramType) throws Exception {
		LOGGER.debug("Starting create method...");

		if (paramType == null) {
			LOGGER.error("The entity " + this.getTypeClass().getSimpleName() + " to save cannot be null.");
			throw new Exception("The entity " + this.getTypeClass().getSimpleName() + " to save cannot be null.");
		}

		try {
			if (exists(getIdentifier(paramType))) {
				LOGGER.error("entity already exists");
				throw new Exception("entity already exists");
			}
			LOGGER.debug("Saving an entity " + this.getTypeClass().getSimpleName() + "...");
			dynamoDBMapper.save(paramType);

			LOGGER.info("The entity " + this.getTypeClass().getSimpleName() + " has been successfully saved.");

		} catch (Exception e) {
			LOGGER.error("An error has occured while saving the entity " + this.getTypeClass().getSimpleName(), e);
			throw new Exception("An error has occured while saving the entity " + this.getTypeClass().getSimpleName(),
					e);
		}

	}

	public Boolean exists(IdType paramIdType) throws Exception {
		LOGGER.debug("Starting exists method...");
		try {
			Type existed = dynamoDBMapper.load(getTypeClass(), paramIdType);
			if (existed != null) {
				return true;
			}
			return false;
		} catch (Exception e) {
			LOGGER.error("An error has occured while trying check if entity " + this.getTypeClass().getSimpleName()
					+ " exists", e);
			throw new Exception("An error has occured while trying check if entity "
					+ this.getTypeClass().getSimpleName() + " exists", e);
		}

	}
}
