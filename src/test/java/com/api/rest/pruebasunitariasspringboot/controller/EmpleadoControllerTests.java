package com.api.rest.pruebasunitariasspringboot.controller;

import com.api.rest.pruebasunitariasspringboot.entities.Empleado;
import com.api.rest.pruebasunitariasspringboot.service.EmpleadoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest //pruebas a los controladores
public class EmpleadoControllerTests {

    @Autowired
    private MockMvc mockMvc; //realizar peticiones http

    @MockBean //reemplaza cualquier bean existente del mismo tipo en el contexto de la aplicación
    private EmpleadoService empleadoService;

    @Autowired
    private ObjectMapper objectMapper; //ObjectMapper es una clase proporcionada por la biblioteca Jackson que se utiliza para convertir objetos Java en representaciones JSON y viceversa.

    @Test
    void testGuardarEmpleado() throws Exception {
        //given //dado a que guardaremos algun empleado
        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre("Jefferson")
                .email("pantaX@gmail.com")
                .apellido("Panta").build();
        given(empleadoService.saveEmpleado(any(Empleado.class))) //any(Empleado.class) significa que se espera que el método saveEmpleado sea llamado con cualquier instancia de la clase Empleado.
                .willAnswer((invocation) ->invocation.getArgument(0)); //obtenemos el argumento numero 0 //En este caso, el lambda devuelve el primer argumento que se pasó al método mockeado (invocation.getArgument(0)).
        //Entonces, en términos más sencillos, este fragmento de código indica que cuando el método saveEmpleado del servicio empleadoService sea invocado durante la prueba, en lugar de realizar alguna lógica real, simplemente devolverá el mismo objeto Empleado que se le pasó como argumento.
        //when/ cuando hagamos una peticion post, esperamos que el tipo del contenido sea json
        ResultActions response = mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empleado))); //convertimos a formato json
        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre",is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleado.getEmail())));
    }

    @Test
    void testListarEmpleados() throws Exception {
        //given
        List<Empleado> listaEmpleados = new ArrayList<>();
        listaEmpleados.add(Empleado.builder().nombre("Christian").apellido("Ramirez").email("c1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Gabriel").apellido("Ramirez").email("g1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Julen").apellido("Ramirez").email("cj@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Biaggio").apellido("Ramirez").email("b1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Adrian").apellido("Ramirez").email("a@gmail.com").build());
        given(empleadoService.getAllEmpleados()).willReturn(listaEmpleados);

        //when
        ResultActions response = mockMvc.perform(get("/api/empleados"));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(listaEmpleados.size())));
    }
    @Test
    void testObtenerEmpleadoPorId() throws Exception{
        //given
        long empleadoId = 1L;
        Empleado empleado = Empleado.builder()
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.of(empleado));

        //when
        ResultActions response = mockMvc.perform(get("/api/empleados/{id}",empleadoId));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre",is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleado.getEmail())));
    }
    @Test
    void testObtenerEmpleadoNoEncontrado() throws Exception{
        //given
        long empleadoId = 1L;
        Empleado empleado = Empleado.builder()
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.empty());

        //when
        ResultActions response = mockMvc.perform(get("/api/empleados/{id}",empleadoId));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testActualizarEmpleado() throws Exception{
        //given
        long empleadoId = 1L;
        Empleado empleadoGuardado = Empleado.builder()
                .nombre("Christian")
                .apellido("Lopez")
                .email("c1@gmail.com")
                .build();

        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Christian Raul")
                .apellido("Ramirez")
                .email("c231@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.of(empleadoGuardado));
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(put("/api/empleados/{id}",empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre",is(empleadoActualizado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleadoActualizado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleadoActualizado.getEmail())));
    }

    @Test
    void testActualizarEmpleadoNoEncontrado() throws Exception{
        //given
        long empleadoId = 1L;
        Empleado empleadoGuardado = Empleado.builder()
                .nombre("Christian")
                .apellido("Lopez")
                .email("c1@gmail.com")
                .build();

        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Christian Raul")
                .apellido("Ramirez")
                .email("c231@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.empty());
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(put("/api/empleados/{id}",empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testEliminarEmpleado() throws Exception{
        //given
        long empleadoId = 1L;
        willDoNothing().given(empleadoService).deleteEmpleado(empleadoId);

        //when
        ResultActions response = mockMvc.perform(delete("/api/empleados/{id}",empleadoId));

        //then
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
