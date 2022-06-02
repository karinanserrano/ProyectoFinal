package com.saleggforce.egg.controladores;

import com.saleggforce.egg.entidades.Marca;
import com.saleggforce.egg.entidades.Producto;
import com.saleggforce.egg.servicios.MarcaServicio;
import com.saleggforce.egg.servicios.ProductoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoControlador {

    private final ProductoServicio productoServicio;
    private final MarcaServicio marcaServicio;

    @Autowired
    public ProductoControlador(ProductoServicio productoServicio, MarcaServicio marcaServicio) {
        this.productoServicio = productoServicio;
        this.marcaServicio = marcaServicio;
    }
//-------------------------------------------------------------------------
//empleado rol admin crea producto

    @GetMapping("/formulario")//crearProducto.html
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String nuevoProducto(ModelMap model) {
        model.addAttribute("producto", new Producto());
        List<Marca> marca = marcaServicio.listarMarcas();
        model.addAttribute("marcas", marca);
        return "producto/crearProducto";
    }

    @PostMapping("/formulario")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String mostrarNuevoproducto(@RequestParam String modelo, @RequestParam Marca marca, @RequestParam Integer precio, @RequestParam String descripcion, MultipartFile foto, MultipartFile ficha, ModelMap model, RedirectAttributes attr) {

        System.out.println("2");
        try {
            System.out.println("3");
            productoServicio.crearProducto(modelo, marca, precio, descripcion, foto, ficha);
            attr.addFlashAttribute("exito", "¡Vehículo cargado exitosamente!");
            System.out.println("4");
        } catch (Exception e) {
            attr.addFlashAttribute("error", "Ha ocurrido un error inesperado.");
            e.printStackTrace();
            return "/producto/crearProducto";
        }
        System.out.println("6");
        return "redirect:/productos/formulario";
//        try {
//            productoServicio.crearProducto(producto);
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//            return "/producto/crearProducto";
//        }
//        return "redirect:/productos/listar";
    }
//--------------------------------------------------------------------------------------------
//empleado rol admin modifica el producto

    @GetMapping("/modificar/{id}")// modificarProducto.html
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String modificarProducto(@PathVariable String id, ModelMap model) {
        model.put("producto", productoServicio.getOne(id));
        List<Marca> marca = marcaServicio.listarMarcas();
        model.addAttribute("marcas", marca);
        return "producto/modificarProducto";
    }

    @PostMapping("/modificar")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String guardarProducto(@RequestParam String id, @RequestParam Integer nuevoPrecio, MultipartFile nuevaFoto, MultipartFile nuevaFichaTecnica, @RequestParam String nuevaDescripcion, @RequestParam Marca nuevaMarca, @RequestParam String nuevoModelo, ModelMap model, RedirectAttributes attr) {
        try {
            productoServicio.modificarProducto(id, nuevoPrecio, nuevaFoto, nuevaFichaTecnica, nuevaDescripcion, nuevaMarca, nuevoModelo);
            attr.addFlashAttribute("exito", "¡Los cambios se guardaron exitosamente!");
        } catch (Exception e) {
            attr.addFlashAttribute("error", e.getMessage());
            model.put("producto", productoServicio.getOne(id));
            return "/producto/modificarProducto";
        }
        return "redirect:/productos/listar";
    }
//---------------------------------------------------------------------------------------------

    @GetMapping("/baja/{id}")//listaProducto.html
    public String baja(@PathVariable String id, ModelMap model) {
        try {
            productoServicio.bajaProducto(id);
        } catch (Exception e) {
            return "/producto/listaProducto";
        }
        return "redirect:/productos/listar";
    }
//------------------------------------------------------------------------------

    @GetMapping("/alta/{id}")//listaProducto.html
    public String alta(@PathVariable String id, ModelMap model) {

        try {
            productoServicio.altaProducto(id);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "/producto/listaProducto";
        }
        return "redirect:/productos/listar";
    }
//-----------------------------------------------------------------------------
    //Este deberia ver el admin solo

    @GetMapping("/listar")//listaProducto.html
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String listarProductos(ModelMap modelo) {
        List<Producto> productos = productoServicio.listarProductos();
        modelo.addAttribute("productos", productos);
        return "/producto/listaProducto";
    }
//------------------------------------------------------------------------------

    @GetMapping("/{id}")//listaPorMarca.html
    public String listarPorMarca(ModelMap model, @PathVariable String id) {
        List<Producto> productosPorMarca = productoServicio.listarProductosPorMarca(id);
        model.addAttribute("productos", productosPorMarca);
        return "/producto/listaPorMarca";
    }
//-----------------------------------------------------------------------------

    @GetMapping("/search") //para buscar
    public String findProductoByKeyword(ModelMap model, @Param("keyword") String keyword) {
        List<Producto> filteredProducto = productoServicio.find(keyword);
        model.addAttribute("productos", filteredProducto);
        return "/producto/listaProducto";
    }
//-----------------------------------------------------------------------------
    /*@GetMapping("/{id}/{id}")//formularioCliente.html
    public String listarPorModelo(ModelMap model, @RequestParam String modelo) {
        List<Producto> productosPorModelo = productoServicio.listarProductosPorModelo(modelo);
        model.addAttribute("productos", productosPorModelo);
        return "/cliente/formularioCliente";
    }
//------------------------------------------------------------------------------
    //Si queda tiempo, usar este para el user 
    //(osea que el user solo vea los activos) y que el admin vea todos
    @GetMapping("/listarActivos")//listaProducto.html
    public String listarActivos(ModelMap model) {
        List<Producto> productosActivos = productoServicio.listarActivos();
        model.addAttribute("productos", productosActivos);
        return "/producto/listaProducto";
    }*/
}
