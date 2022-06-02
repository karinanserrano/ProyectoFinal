package com.saleggforce.egg.servicios;

import com.saleggforce.egg.entidades.Marca;
import com.saleggforce.egg.errores.ErrorServicio;
import com.saleggforce.egg.repositorios.MarcaRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarcaServicio {

    private final MarcaRepositorio marcaRepositorio;

    @Autowired
    public MarcaServicio(MarcaRepositorio marcaRepositorio) {
        this.marcaRepositorio = marcaRepositorio;
    }

    @Transactional
    public void crearMarca(Marca marca) throws ErrorServicio {
        if (marca.getNombre().trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede estar vacio");
        }
        marcaRepositorio.save(marca);
    }

    @Transactional
    public List<Marca> listarMarcas() {
        return marcaRepositorio.findAll();
    }

    @Transactional
    public List<Marca> listarProductos() {
        return marcaRepositorio.findAll();
    }

    @Transactional
    public List<Marca> find(String keyword) {
        return marcaRepositorio.find(keyword);
    }

    public Marca buscarMarcaPorId(String id) throws ErrorServicio {
        Optional<Marca> respuesta = marcaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Marca marca = respuesta.get();
            return marca;
        } else {
            throw new ErrorServicio("No se encuentra la marca.");
        }
    }

    @Transactional
    public Marca getOne(String id) {
        return marcaRepositorio.getOne(id);

    }

    @Transactional
    public void modificarMarca(String id, String nuevoNombre) throws ErrorServicio {
        Marca marca = marcaRepositorio.getOne(id);
        marca.setNombre(nuevoNombre);
        marcaRepositorio.save(marca);
    }

}
