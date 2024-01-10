package com.api.rest.pruebasunitariasspringboot.service;

import com.api.rest.pruebasunitariasspringboot.entities.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {
    Empleado saveEmpleado(Empleado empleado);

    List<Empleado> getAllEmpleados();
    Optional<Empleado> getEmpleadoById(long id);
    Empleado updateEmpleado(Empleado empleadoActualizado);
    void deleteEmpleado(long id);
}
