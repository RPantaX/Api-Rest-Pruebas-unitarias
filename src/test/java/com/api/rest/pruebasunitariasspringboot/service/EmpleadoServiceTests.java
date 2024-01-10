package com.api.rest.pruebasunitariasspringboot.service;
//para seguir trabajando con BDD importamos
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import com.api.rest.pruebasunitariasspringboot.entities.Empleado;
import com.api.rest.pruebasunitariasspringboot.exception.ResourceNotFoundException;
import com.api.rest.pruebasunitariasspringboot.repository.EmpleadoRepository;
import com.api.rest.pruebasunitariasspringboot.service.impl.EmpleadoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class) //Esta notacion sirve para indicarle que trabajaremos con mockito y cargar extenciones de mockito en la que usa extenciones de junit
public class EmpleadoServiceTests {

    @Mock //Mock crea un simulacro de este repositorio
    private EmpleadoRepository empleadoRepository;

    @InjectMocks //inyectamos empleadosRepository dentro de empleadoService
    private EmpleadoServiceImpl empleadoService;

    Empleado empleado;
    @BeforeEach
    void guardarEmpleado(){
        empleado= Empleado.builder()
                .id(1L)
                .nombre("Christian")
                .apellido("Ramirez")
                .email("pantax@gmail.com")
                .build();
    }
    @DisplayName("Test para guardar un empleado")
    @Test
    void testGuardarEmpleado(){
        //given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.empty());
        given(empleadoRepository.save(empleado)).willReturn(empleado);
        //when
        Empleado empleadoGuardado = empleadoService.saveEmpleado(empleado);
        //then
        assertThat(empleadoGuardado).isNotNull();
    }

    @DisplayName("Test para guardar un empleado con Throw Exception")
    @Test
    void testGuardarEmpleadoConThrowException(){
        //given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.of(empleado));
        //when
        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            empleadoService.saveEmpleado(empleado);
        });

        //then
        verify(empleadoRepository,never()).save(any(Empleado.class)); //nunc se usó el repositorio para guardar empleado algún empleado

    }

    @DisplayName("Test para listar empleados")
    @Test
    void testListarEmpleados(){
        //given
        Empleado empleado1= Empleado.builder()
                .id(2L).nombre("Jefferson").apellido("Panta")
                .email("panta@gmail.com").build();
        given(empleadoRepository.findAll()).willReturn(List.of(empleado,empleado1));
        //when
        List<Empleado> empleados= empleadoService.getAllEmpleados();
        //then
        assertThat(empleados).isNotNull();
        assertThat(empleados.size()).isEqualTo(2);
    }

    @DisplayName("Test para listar empleados vacio")
    @Test
    void testListarEmpleadosVacia(){
        //given
        given(empleadoRepository.findAll()).willReturn(Collections.emptyList());
        //when
        List<Empleado> empleados= empleadoService.getAllEmpleados();
        //then
        assertThat(empleados).isEmpty();
        assertThat(empleados.size()).isEqualTo(0);
    }

    @DisplayName("Test para obtener empleados por ID")
    @Test
    void testObtenerEmpleadosId(){
        //give
        given(empleadoRepository.findById(empleado.getId())).willReturn(Optional.of(empleado));
        //when
        Empleado empleado1= empleadoService.getEmpleadoById(empleado.getId()).get();
        //then
        assertThat(empleado1).isNotNull();
        assertThat(empleado1.getId()).isEqualTo(1L);
    }
    @DisplayName("Test para obtener empleados por ID VACIO")
    @Test
    void testObtenerEmpleadosIdVacio(){
        //give
        given(empleadoRepository.findById(3L)).willReturn(Optional.empty());
        //when
        Optional<Empleado> empleado1= empleadoService.getEmpleadoById(3L);
        //then
        assertThat(empleado1).isEqualTo(Optional.empty());
    }

    @DisplayName("Test para actualizar un empleado")
    @Test
    void testActualizarEmpleado(){
        //given
        given(empleadoRepository.save(empleado)).willReturn(empleado);
        empleado.setEmail("actualizado@gmail.com");
        empleado.setNombre("Jefferson Panta");
        ///when
        Empleado empleadoActualizado= empleadoService.saveEmpleado(empleado);
        //then
        assertThat(empleadoActualizado.getEmail()).isEqualTo("actualizado@gmail.com");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("Jefferson Panta");
    }

    @DisplayName("Test para eliminar un empleado")
    @Test
    void testEliminarEmpleado(){
        //given
        long empleadoId = 1L;
        willDoNothing().given(empleadoRepository).deleteById(empleadoId);
        //when
        empleadoService.deleteEmpleado(empleadoId);
        //then
        verify(empleadoRepository, times(1)).deleteById(empleadoId);//cuando llame al delete by id voy a esperar a que se haya ejecutado 1 sola vez
    }

}
