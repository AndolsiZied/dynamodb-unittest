package com.ando.unitdemo.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<Type, IdType extends Serializable> {

	// Type find(IdType paramIdType) throws Exception;
	void save(Type paramType) throws Exception;

	// void update(Type paramType) throws Exception;
	// void delete(Type paramType) throws Exception;
	Boolean exists(IdType paramIdType) throws Exception;

	List<Type> findAll();

	IdType getIdentifier(Type entity);

	Class<Type> getTypeClass();

}