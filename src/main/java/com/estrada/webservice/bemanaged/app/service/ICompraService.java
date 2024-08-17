package com.estrada.webservice.bemanaged.app.service;

import java.util.List;

import com.estrada.webservice.bemanaged.app.entity.Compra;

public interface ICompraService {
	
	Compra save(Compra compra);
	Compra findById(Long id);
	List<Compra> findAll();
	void deleteById(Long id);
}
