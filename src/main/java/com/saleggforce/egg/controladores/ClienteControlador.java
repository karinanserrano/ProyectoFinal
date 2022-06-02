package com.saleggforce.egg.controladores;

import com.saleggforce.egg.entidades.Cliente;
import com.saleggforce.egg.entidades.Producto;
import com.saleggforce.egg.enums.Estado;
import com.saleggforce.egg.enums.HorarioContacto;
import com.saleggforce.egg.errores.ErrorServicio;
import com.saleggforce.egg.servicios.ClienteServicio;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteControlador {

    private final ClienteServicio clienteServicio;
    private final ProductoServicio productoServicio;

    @Autowired
    public ClienteControlador(ClienteServicio clienteServicio, ProductoServicio productoServicio) {
        this.clienteServicio = clienteServicio;
        this.productoServicio = productoServicio;
    }

    @GetMapping//cliente/listaCliente
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String listar(ModelMap model) {
        List<Cliente> clientes = clienteServicio.listarClientes();
        model.addAttribute("clientes", clientes);
        return "cliente/listaCliente";
    }

    @GetMapping("/formulario/{idProducto}")//cliente/formularioCliente
    public String nuevoCliente(@PathVariable String idProducto, ModelMap model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("producto", productoServicio.getOne(idProducto));
        return "cliente/formularioCliente";
    }

    @PostMapping("/formulario/{id}")//cliente/formularioCliente
    public String guardarCliente(@ModelAttribute Cliente cliente, ModelMap modelo, @PathVariable String id,
            RedirectAttributes attr) {
        // model.addAttribute("producto", productoServicio.getOne(idProducto));
        try {
            clienteServicio.crearCliente(cliente, id);
            attr.addFlashAttribute("exito", "¡Gracias por contactarte con nosotros! A la brevedad un vendedor se comunicará con vos.");
            //model.addAttribute("cliente", cliente);
            return "redirect:/clientes/formulario/{id}";
        } catch (ErrorServicio e) {
            modelo.put("error", "Ha ocurrido un error inesperado");
            return "redirect:/clientes/formulario/{id}";
        }
        //return "redirect:/clientes/formulario/{id}";
    }

    @GetMapping("/gestion/{id}")//cliente/gestionCliente
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String gestion(@ModelAttribute Cliente cliente, ModelMap model) {
        model.put("cliente", clienteServicio.getOne(cliente.getId()));
        return "cliente/gestionCliente";
    }

    @PostMapping("/gestion/{id}")//cliente/gestionCliente
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String procesarGestion(@PathVariable String id, String nuevoNombre,
            String nuevoApellido, String nuevoMail, String nuevoTelefono,
            HorarioContacto nuevoHorarioContacto, Estado nuevoEstado, String nuevoComentario, ModelMap model, RedirectAttributes attr) {
        try {
            clienteServicio.modificarCliente(id, nuevoNombre, nuevoApellido, nuevoMail, nuevoTelefono, nuevoHorarioContacto, nuevoEstado, nuevoComentario);
            attr.addFlashAttribute("exito", "¡Los cambios se guardaron exitosamente!");
        } catch (Exception e) {
            attr.addFlashAttribute("error", "Ha ocurrido un error inesperado.");
            return "cliente/gestionCliente";
        }
        return "redirect:/clientes/gestion/{id}";
    }

    @GetMapping("/modificar/{id}")//cliente/modificarCliente
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String modificar(@ModelAttribute Cliente cliente, ModelMap model) {
        model.put("cliente", clienteServicio.getOne(cliente.getId()));
        return "cliente/modificarCliente";
    }

    @PostMapping("/modificar/{id}")//cliente/modificarCliente
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String procesarModificar(@PathVariable String id, String nuevoNombre,
            String nuevoApellido, String nuevoMail, String nuevoTelefono,
            HorarioContacto nuevoHorarioContacto, Estado nuevoEstado, String nuevoComentario, ModelMap model, RedirectAttributes attr) {
        try {
            clienteServicio.modificarCliente(id, nuevoNombre, nuevoApellido, nuevoMail, nuevoTelefono, nuevoHorarioContacto, nuevoEstado, nuevoComentario);
            attr.addFlashAttribute("exito", "¡Los cambios se guardaron exitosamente!");
        } catch (Exception e) {
            attr.addFlashAttribute("error", "Ha ocurrido un error inesperado");
            return "cliente/listaCliente";
        }
        return "redirect:/clientes/modificar/{id}";
    }

    @GetMapping("/baja/{id}")//cliente/listaCliente
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String bajaCliente(@PathVariable String id, ModelMap model) {
        try {
            clienteServicio.bajaCliente(id);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "cliente/listaCliente";
        }
        return "redirect:/clientes";
    }

    @GetMapping("/alta/{id}")//cliente/listaCliente
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String altaCliente(@PathVariable String id, ModelMap model) {
        try {
            clienteServicio.altaCliente(id);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "cliente/listaCliente";
        }
        return "redirect:/clientes";
    }

    @GetMapping("/buscar")//en el nav o head
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String buscar(@RequestParam String nombre, ModelMap model) {
        try {
            clienteServicio.buscarPorNombre(nombre);
        } catch (Exception e) {
            model.put("error", "no se encontro ningun cliente con ese nombre");
            return "cliente/listaCliente";
        }
        return "cliente/listaCliente";
    }

    @PostMapping("/buscar/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String seleccionar(@PathVariable String id, ModelMap model) {
        try {
            clienteServicio.buscarPorNombre(id);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "cliente/gestionCliente";
    }

    @GetMapping("/search") //para buscar
    public String findClientesByKeyword(ModelMap model, @Param("keyword") String keyword) {
        List<Cliente> filteredCliente = clienteServicio.find(keyword);
        model.addAttribute("clientes", filteredCliente);
        return "cliente/listaCliente";
    }

}
//clientes/formulario
//metodo nuevoCliente
//
//clientes/gestion{id}
//metodo gestionCliente
//
//clientes/alta/{id}
//metodo altaCliente
//
//clientes/baja/{id}
//metodo bajaCliente
//
//clientes/listar
//metodo listarClientes
//
//clientes/buscar
//metodo buscarPorNombre
