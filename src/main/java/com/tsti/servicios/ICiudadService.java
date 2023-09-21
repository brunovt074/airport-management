package com.tsti.servicios;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tsti.entidades.Ciudad;

@Service
public interface ICiudadService {
	public java.util.List<Ciudad> findAll();

	public Optional<Ciudad> getById(Long id);
}