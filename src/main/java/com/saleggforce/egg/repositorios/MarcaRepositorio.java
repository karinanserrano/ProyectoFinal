package com.saleggforce.egg.repositorios;

import com.saleggforce.egg.entidades.Marca;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepositorio extends JpaRepository<Marca, String> {

    // @Query("SELECT m FROM Marca m WHERE m.marca = :marca")
    //public Marca findByMarca(@Param("marca") String marca);
    
    @Query("Select m from Marca m where m.nombre LIKE %?1%")
    public List<Marca> find(String keyword);
}
