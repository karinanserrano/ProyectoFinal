package com.saleggforce.egg.servicios;

import com.saleggforce.egg.controladores.FichaTecnicaControlador;
import com.saleggforce.egg.entidades.FichaTecnica;
import com.saleggforce.egg.entidades.Foto;
import com.saleggforce.egg.entidades.Marca;
import com.saleggforce.egg.entidades.Producto;
import com.saleggforce.egg.errores.ErrorServicio;
import com.saleggforce.egg.repositorios.ProductoRepositorio;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductoServicio {

    @Autowired
    private FotoServicio fotoServicio;

    @Autowired
    private FichaTecnicaServicio fichaTecnicaServicio;

    private final ProductoRepositorio productoRepositorio;

    @Autowired
    public ProductoServicio(ProductoRepositorio productoRepositorio, FichaTecnicaServicio fichaTecnicaServicio) {
        this.productoRepositorio = productoRepositorio;
        this.fichaTecnicaServicio = fichaTecnicaServicio;
    }

    @Transactional
    public void crearProducto(String modelo, Marca marca, Integer precio, String descricion, MultipartFile imagen, MultipartFile ficha) throws ErrorServicio, Exception {

        Producto producto = new Producto();
        producto.setModelo(modelo);
        producto.setMarca(marca);
        producto.setPrecio(precio);
        producto.setDescripcion(descricion);
        validarProducto(producto);
        if (producto.getAlta() == null) {
            producto.setAlta(Boolean.TRUE);
        }
        FichaTecnica fichaTecnica = fichaTecnicaServicio.guardar(ficha);
        producto.setFichaTecnica(fichaTecnica);
        Foto foto = fotoServicio.guardar(imagen);
        producto.setFoto(foto);
        productoRepositorio.save(producto);
//    public void crearProducto(Producto producto) throws ErrorServicio {
//        validarProducto(producto);
//        if (producto.getAlta() == null) {
//            producto.setAlta(Boolean.TRUE);
//        }
//        productoRepositorio.save(producto);
    }

    @Transactional
    public void modificarProducto(String id, Integer precio, MultipartFile imagen, MultipartFile ficha, String descripcion, Marca marca, String modelo) throws ErrorServicio, Exception {

        Optional<Producto> respuesta = productoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Producto producto = respuesta.get();
            
            producto.setPrecio(precio);

            if (modelo != null && !modelo.isEmpty()) {
                producto.setModelo(modelo);
            }
            
              if (marca != null) {
                producto.setMarca(marca);
            }

            Foto foto = fotoServicio.guardar(imagen);
            FichaTecnica fichaTecnica = fichaTecnicaServicio.guardar(ficha);

            if (ficha != null && !ficha.isEmpty()) {
                producto.setFichaTecnica(fichaTecnica);
            }

            if (imagen != null && !imagen.isEmpty()) {
                producto.setFoto(foto);
            }

            producto.setDescripcion(descripcion);
            validarProducto(producto);
            productoRepositorio.save(producto);

//             if (nuevaClave != null && !nuevaClave.isEmpty()) {
//            empleado.setClave(new BCryptPasswordEncoder().encode(nuevaClave));
//        }
        } else {
            throw new ErrorServicio("No se encontro el producto.");
        }
//        Optional<Producto> respuesta = productoRepositorio.findById(id);
//
//        if (respuesta.isPresent()) {
//            Producto producto = respuesta.get();
//
//            producto.setFichaTecnica(fichaTecnica);
//            producto.setPrecio(precio);
//            producto.setFoto(foto);
//
//            validarProducto(producto);
//            productoRepositorio.save(producto);
//        } else {
//            throw new ErrorServicio("No se encontro el producto.");
//        }
    }

    @Transactional
    public void altaProducto(String id) {
        Optional<Producto> respuesta = productoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Producto producto = respuesta.get();

            producto.setAlta(Boolean.TRUE);
            productoRepositorio.save(producto);
        }
    }

    @Transactional
    public void bajaProducto(String id) {
        Optional< Producto> respuesta = productoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Producto producto = respuesta.get();
            producto.setAlta(Boolean.FALSE);
            productoRepositorio.save(producto);
        }
    }

    @Transactional
    public List<Producto> listarProductos() {
        return productoRepositorio.findAll();
    }

    @Transactional
    public List<Producto> listarProductosPorMarca(String id) {
        return productoRepositorio.listarModeloPorMarca(id)
                .stream()
                .filter(producto -> producto.getAlta() == true)
                .collect(Collectors.toList());
    }

//    @Transactional// ES LO MISMO QUE EL METODO DE ARRIBA
//    public List<Producto> listarProductosPorMarca(String id) {
//        List<Producto> productosActivos = new ArrayList();
//        for (Producto producto : productoRepositorio.listarModeloPorMarca(id)) {
//            if(producto.getAlta() == true){
//                productosActivos.add(producto);
//            }
//        }
//        return productosActivos;
//    }

    /*
    @Transactional
    public List<Producto> listarProductosPorModelo(String modelo) {
        return productoRepositorio.findByModelo(modelo);
    }
     */
    @Transactional
    public Producto getOne(String id) {
        return productoRepositorio.getOne(id);
    }

    @Transactional
    public List<Producto> listarActivos() {
        return productoRepositorio.findByActivo();
    }

    @Transactional
    public List<Producto> find(String keyword) {
        return productoRepositorio.find(keyword);
    }

    public void validarProducto(Producto producto) throws ErrorServicio {

        if (producto.getModelo().trim().isEmpty()) {
            throw new ErrorServicio("El modelo no puede estar vacío.");
        }
        if (producto.getMarca() == null) {
            throw new ErrorServicio("La marca no puede estar vacio");
        }
        if (producto.getPrecio() == null) {
            throw new ErrorServicio("El precio no puede estar vacío.");
        }
        if (producto.getDescripcion() == null) {
            throw new ErrorServicio("La descripcion no puede estar vacia");
        }
    }

}
