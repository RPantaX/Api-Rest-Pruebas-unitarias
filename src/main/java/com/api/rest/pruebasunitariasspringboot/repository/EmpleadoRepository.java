package com.api.rest.pruebasunitariasspringboot.repository;

import com.api.rest.pruebasunitariasspringboot.entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado,Long> {
    Optional<Empleado>findByEmail(String email);
}
