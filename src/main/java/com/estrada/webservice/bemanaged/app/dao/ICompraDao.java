package com.estrada.webservice.bemanaged.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.estrada.webservice.bemanaged.app.entity.Compra;

public interface ICompraDao extends CrudRepository<Compra, Long> {

}
