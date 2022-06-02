package com.saleggforce.egg.repositorios;

import com.saleggforce.egg.entidades.Empleado;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepositorio extends JpaRepository<Empleado, String> {

    @Query("SELECT u FROM Empleado u WHERE u.usuario = :usuario")
    public Empleado findByUsuario(@Param("usuario") String usuario);

    @Query("Select e from Empleado e where e.nombre LIKE %?1%"
            + "OR e.apellido LIKE %?1%"
            + "OR e.usuario LIKE %?1%")
    public List<Empleado> find(String keyword);
}
