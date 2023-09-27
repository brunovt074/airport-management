package com.tsti.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tsti.entidades.Clientes;
import com.tsti.entidades.Pasaje;

/**
 * @author Bruno
 *
 */
public interface PasajeDAO extends JpaRepository<Pasaje, Long> {
    @Query("SELECT p FROM Pasaje p WHERE p.pasajero = :pasajero")
    List<Pasaje> findByPasajero(@Param("pasajero") Optional<Clientes> pasajero);
}