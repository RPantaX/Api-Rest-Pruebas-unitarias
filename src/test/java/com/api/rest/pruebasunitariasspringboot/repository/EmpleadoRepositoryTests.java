package com.api.rest.pruebasunitariasspringboot.repository;
import static org.assertj.core.api.Assertions.assertThat;
import com.api.rest.pruebasunitariasspringboot.entities.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest//indicamos que en esta clase solo podemos hacer tests o pruebas solo a la capa de repositorio jpa/no prueba ni controladores ni services, solo prueba entidades y repositorios
public class EmpleadoRepositoryTests {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    private Empleado empleado;
    @BeforeEach
    void guardarEmpleado(){
        empleado= Empleado.builder()
                .nombre("Christian")
                .apellido("Ramirez")
                .email("pantax@gmail.com")
                .build();
    }


    @DisplayName("Test para guardar un empleado")
    @Test
    void testGuardadEmpleado(){
        //metodologia BDD
        //given - dado o condición previa a configuración - Dado este empleado
        Empleado empleado1= Empleado.builder()
                .nombre("Pepe")
                .apellido("Lopes")
                .email("p12@gmail.com")
                .build();
        //when - acción o el comportamiento que vamos a probar - cuando llamemos al repositorio y guardemos al empleado
        Empleado empleadoGuardado= empleadoRepository.save(empleado1);
        //then - verificar la salida -
        assertThat(empleadoGuardado).isNotNull();
        assertThat(empleadoGuardado.getId()).isGreaterThan(0); //esperamos que no sea nulo y su id sea mayor a 0
        //todo se lee: dado el empleado cuando lo guardemos esperamos que no sea nullo y que su id sea mayor a 0
    }
    @DisplayName("Test para listar a los empleados")
    @Test
    void testListarEmpleados(){
        //BDD
        //given
        Empleado empleado1= Empleado.builder()
                .nombre("Julen")
                .apellido("Oliva")
                .email("j2@gmail.com")
                .build();
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado);
        //when
        List<Empleado> empleados= empleadoRepository.findAll();
        //THEN
        assertThat(empleados).isNotNull();
        assertThat(empleados.size()).isEqualTo(2);
    }

    @DisplayName("Listar empleado por ID")
    @Test
    void testObtenerEmpleadoId(){
        //given
        empleadoRepository.save(empleado);
        //when
        Empleado empleadoGuardado= empleadoRepository.findById(empleado.getId()).get();
        //then
        assertThat(empleadoGuardado).isNotNull();
    }
    @DisplayName("Test para actualizar un empleado")
    @Test
    void testActualizarEmpleadd(){
        //given
        empleadoRepository.save(empleado);
        //when
        Empleado empleadoGuardado= empleadoRepository.findById(empleado.getId()).get();
        empleadoGuardado.setEmail("pantajefferson123@gmail.com");
        empleadoGuardado.setApellido("Kuchinski");
        empleadoGuardado.setNombre("Roberto");
        Empleado empleadoActualizado= empleadoRepository.save(empleadoGuardado);
        //then
        assertThat(empleadoActualizado).isNotNull();
        assertThat(empleadoActualizado.getId()).isEqualTo(1L);
        assertThat(empleadoActualizado.getEmail()).isEqualTo("pantajefferson123@gmail.com");
        assertThat(empleadoActualizado.getApellido()).isEqualTo("Kuchinski");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("Roberto");

    }
    @DisplayName("Test para eliminar un empleado")
    @Test
    void testEliminarEmpleadd(){
        //given
        empleadoRepository.save(empleado);

        //WHEN
        empleadoRepository.delete(empleado);
        Optional<Empleado> empleados= empleadoRepository.findById(empleado.getId()); //true si lo encuentra, false si no lo encuentra
        //THEN
        assertThat(empleados).isEmpty();
    }


}
