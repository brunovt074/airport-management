package com.tsti.servicios;

import java.util.Optional;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsti.dao.CiudadDAO;
import com.tsti.entidades.Ciudad;

@Service
public class CiudadServiceImpl implements ICiudadService {
	@Autowired
	private CiudadDAO dao;
	
	@Override
	public java.util.List<Ciudad> findAll() {
		return dao.findAll();
	}

	@Override
	public Optional<Ciudad> getById(Long id) {
		return dao.findById(id);

	}
	
	public TreeSet<Ciudad> getAllDistinctCities() {
		
		TreeSet<Ciudad> ciudades = new TreeSet<>();
		
		ciudades.addAll(findAll());
				
		return ciudades;
	}

}
