package com.saleggforce.egg.repositorios;

import com.saleggforce.egg.entidades.Cliente;
import com.saleggforce.egg.enums.Estado;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String> {

    @Query("SELECT c FROM Cliente c WHERE c.nombre LIKE %?1%"
            + "OR c.apellido LIKE %?1%"
            + "OR c.estado LIKE %?1%"
            + "OR c.producto.modelo LIKE %?1%"
            + "OR c.horarioContacto LIKE %?1%")
    public List<Cliente> find(String keyword);

    @Query("SELECT c FROM Cliente c WHERE c.nombre LIKE %?1%"
            + "OR c.apellido LIKE %?1%")
    public List<Cliente> findByApellidoOrNombre(@Param("palabraClave") String palabraClave);

    @Query("SELECT c FROM Cliente c ORDER BY c.estado")
    public List<Cliente> finByAllOrderByEstado(@Param("estado") Estado estado);

    public Cliente getOne(String id);
}
