package com.saleggforce.egg.repositorios;

import com.saleggforce.egg.entidades.Marca;
import com.saleggforce.egg.entidades.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, String> {

    @Query("Select p from Producto p where p.modelo LIKE %?1%"
            + "OR p.marca.nombre LIKE %?1%")
    public List<Producto> find(String keyword);
    
    @Query("SELECT p FROM Producto p WHERE p.modelo LIKE %:modelo%")
    public List<Producto> findByModelo(@Param("modelo") String modelo);

    @Query("SELECT p FROM Producto p WHERE p.marca LIKE %:marca%")
    public List<Producto> findByMarca(@Param("marca") Marca marca);

    @Query("SELECT a FROM Producto a WHERE a.alta = true ORDER BY a.modelo")
    public List<Producto> findByActivo();

    //@Query("SELECT p FROM Producto p JOIN Marca m ON p.id = m.id WHERE m.id = :idMarca")
    //public List<Producto> listarModeloPorMarca(@Param("idMarca") String idMarca);
    @Query("SELECT p FROM Producto p WHERE p.marca.id = :id")
    public List<Producto> listarModeloPorMarca(@Param("id") String id);
}
