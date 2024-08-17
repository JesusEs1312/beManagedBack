package com.estrada.webservice.bemanaged.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estrada.webservice.bemanaged.app.dao.ICompraDao;
import com.estrada.webservice.bemanaged.app.entity.Compra;

@Service
public class CompraServiceImpl implements ICompraService{

	@Autowired
	private ICompraDao compraDao;
	
	@Override
	public Compra save(Compra compra) {
		return this.compraDao.save(compra);
	}

	@Override
	public Compra findById(Long id) {
		return this.compraDao.findById(id).orElseGet(null);
	}

	@Override
	public List<Compra> findAll() {
		return (List<Compra>) this.compraDao.findAll();
	}

	@Override
	public void deleteById(Long id) {
		this.compraDao.deleteById(id);
	}
	
}
