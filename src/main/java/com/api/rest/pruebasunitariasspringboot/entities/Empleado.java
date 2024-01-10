package com.api.rest.pruebasunitariasspringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="empleados")
@Data/*genera @Getter, @Setter, @ToString, @EqualsAndHashCode, y opcionalmente @NoArgsConstructor y @AllArgsConstructor. */
@AllArgsConstructor
@NoArgsConstructor
@Builder //crea instancias de objetos mediante la generacion automatica de un constructor de tipo Builder
public class Empleado {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "apellido", nullable = false)
    private String apellido;
    @Column(name = "email", nullable = false)
    private String email;
}
