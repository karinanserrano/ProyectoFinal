package com.saleggforce.egg.servicios;

import com.saleggforce.egg.entidades.Foto;
import com.saleggforce.egg.repositorios.FotoRepositorio;
import java.io.IOException;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {

    private FotoRepositorio fotoRepositorio;

    @Autowired
    public FotoServicio(FotoRepositorio fotoRepositorio) {
        this.fotoRepositorio = fotoRepositorio;
    }

    @Transactional
    public Foto guardar(MultipartFile imagen) throws Exception {
        if (imagen != null && !imagen.isEmpty()) {
            try {
                Foto foto = new Foto();
                foto.setMime(imagen.getContentType());
                foto.setNombre(imagen.getName());
                foto.setContenido(imagen.getBytes());
                return fotoRepositorio.save(foto);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public Foto actualizar(String idFoto, MultipartFile imagen) throws Exception {
        if (imagen != null) {

            Foto foto = new Foto();

            if (idFoto != null) {
                Optional<Foto> respuesta = fotoRepositorio.findById(idFoto);
                if (respuesta.isPresent()) {
                    foto = respuesta.get();
                }
            }

            foto.setMime(imagen.getContentType());
            foto.setNombre(imagen.getName());
            foto.setContenido(imagen.getBytes());

            return fotoRepositorio.save(foto);
        } else {
            return null;

        }
    }

}
