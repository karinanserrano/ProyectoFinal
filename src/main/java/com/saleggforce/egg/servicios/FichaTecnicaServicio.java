package com.saleggforce.egg.servicios;

import com.saleggforce.egg.entidades.FichaTecnica;
import com.saleggforce.egg.repositorios.FichaTecnicaRepositorio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FichaTecnicaServicio {

    private FichaTecnicaRepositorio fichaTecnicaRepositorio;

    @Autowired
    public FichaTecnicaServicio(FichaTecnicaRepositorio fichaTecnicaRepositorio) {
        this.fichaTecnicaRepositorio = fichaTecnicaRepositorio;
    }

    @Transactional
    public FichaTecnica guardar(MultipartFile imagen) throws Exception {
        if (imagen != null && !imagen.isEmpty()) {
            try {
                FichaTecnica ficha = new FichaTecnica();
                ficha.setMime(imagen.getContentType());
                ficha.setNombre(imagen.getName());
                ficha.setContenido(imagen.getBytes());
                return fichaTecnicaRepositorio.save(ficha);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public FichaTecnica actualizar(String idFoto, MultipartFile imagen) throws Exception {
        if (imagen != null) {

            FichaTecnica ficha = new FichaTecnica();

            if (idFoto != null) {
                Optional<FichaTecnica> respuesta = fichaTecnicaRepositorio.findById(idFoto);
                if (respuesta.isPresent()) {
                    ficha = respuesta.get();
                }
            }

            ficha.setMime(imagen.getContentType());
            ficha.setNombre(imagen.getName());
            ficha.setContenido(imagen.getBytes());

            return fichaTecnicaRepositorio.save(ficha);
        } else {
            return null;

        }
    }

}
