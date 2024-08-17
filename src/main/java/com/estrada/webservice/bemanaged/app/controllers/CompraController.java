package com.estrada.webservice.bemanaged.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estrada.webservice.bemanaged.app.entity.Compra;
import com.estrada.webservice.bemanaged.app.service.ICompraService;

import jakarta.validation.Valid;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("api")
public class CompraController {
	
	@Autowired
	ICompraService compraService;
	
	//---FindAll Compras
	@GetMapping("/compras")
	public List<Compra> findAll(){
		return compraService.findAll();
	}
	
	//---Save Compras
	@PostMapping("/compras")
	public ResponseEntity<?> createCompra(@Valid @RequestBody Compra compra, BindingResult result) {
		Compra newCompra = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()){
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo ".concat(err.getField()).concat(": ").concat(err.getDefaultMessage()))
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			newCompra = compraService.save(compra);
		} catch (DataAccessException e) {
			response.put("message", "no se puede guardar");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("compra", newCompra);
		response.put("message", "La compra se guardo con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/compras/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id){
		Compra compra = null;
		Map<String, Object> response = new HashMap<>();
		try {
			compra = compraService.findById(id);
		} catch (DataAccessException e) {
			response.put("message", "La compra no se encuentra en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(compra == null) {
			response.put("message", "El ID de la compra: ".concat(String.valueOf(id)).concat(" no existe"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Compra>(compra, HttpStatus.OK);
	}
	
	@PutMapping("/compras/{id}")
	public ResponseEntity<?> updateById(@Valid @RequestBody Compra compra, BindingResult result, @PathVariable Long id){
		Compra currentCompra = compraService.findById(id);
		Compra updateCompra  = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo ".concat(err.getField()).concat(": ").concat(err.getDefaultMessage()))
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			currentCompra.setCantidad(compra.getCantidad());
			currentCompra.setNombreProducto(compra.getNombreProducto());
			currentCompra.setComentario(compra.getComentario());
			currentCompra.setImagen(compra.getImagen());
			currentCompra.setLugar(compra.getLugar());
			currentCompra.setPrecio(compra.getPrecio());
			currentCompra.setMotivo(compra.getMotivo());
			currentCompra.setTipo(compra.getTipo());
			currentCompra.setTipoProducto(compra.getTipoProducto());
			currentCompra.setRepetido(compra.getRepetido());
			updateCompra = compraService.save(currentCompra);
		} catch(DataAccessException e) {
			response.put("message", "Error updating compra");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("compra", updateCompra);
		response.put("message", "Update compra successful: ".concat(updateCompra.getNombreProducto()));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/compras/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id){
		Compra deleteCompra = compraService.findById(id);
		Map<String, Object> response = new HashMap<>();
		if(deleteCompra == null) {
			response.put("message", "The compra with ID: ".concat(id.toString()).concat(" not exist in database"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			compraService.deleteById(id);
		} catch(DataAccessException e) {
			response.put("message", "Error deleting compra");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "The compra was successfully removed");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
