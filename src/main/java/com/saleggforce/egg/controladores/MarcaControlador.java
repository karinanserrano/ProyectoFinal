package com.saleggforce.egg.controladores;

import com.saleggforce.egg.entidades.Marca;
import com.saleggforce.egg.servicios.MarcaServicio;
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

@RequestMapping("/marcas")
@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
public class MarcaControlador {

    private final MarcaServicio marcaServicio;

    @Autowired
    public MarcaControlador(MarcaServicio marcaServicio) {
        this.marcaServicio = marcaServicio;
    }

//  Crear marcas
    @GetMapping("/formulario")//marcas/crearMarca
    public String nuevaMarca(@RequestParam(required = false) String id, ModelMap model, RedirectAttributes attr) {
        if (id == null) {
            model.addAttribute("marca", new Marca());
        } else {
            try {
                Marca marca = marcaServicio.buscarMarcaPorId(id);
                model.addAttribute("marca", marca);
                return "marcas/formulario";
            } catch (Exception e) {
                attr.addFlashAttribute("error", e.getMessage());
                return "marcas/formulario";
            }
        }
        return "marcas/crearMarca";
    }

// Crear marcas procesar formulario
    @PostMapping("/formulario")//empleado/listaMarcas
    public String procesarFormulario(@ModelAttribute Marca marca, ModelMap model, RedirectAttributes attr) {
        try {
            marcaServicio.crearMarca(marca);
            model.addAttribute("marca", marca);
            attr.addFlashAttribute("exito", "¡Marca cargada exitosamente!");
        } catch (Exception e) {
            attr.addFlashAttribute("error", "Ha ocurrido un error inesperado.");
            //model.put("error", "Hubo un error al crear la marca, por favor reintente.");
            return "marcas/formulario";
        }
        return "redirect:/marcas/formulario";
    }

    //    Metodo que busca todos las macasen el repositorio y los lista
    @GetMapping("/listar") //marca/listaMarcas
    public String listar(ModelMap model) {
        List<Marca> marca = marcaServicio.listarMarcas();
        model.addAttribute("marcas", marca);
        return "marcas/listaMarcas";
    }

    //  Metodo para modificar marcas
    @GetMapping("/modificar/{id}")//marcas/modificarMarca
    public String modificarMarca(@ModelAttribute Marca marca, ModelMap model, @PathVariable String id) {
        model.put("marca", marcaServicio.getOne(id));
        return "marcas/modificarMarca";
    }

    @PostMapping("/modificar/{id}")//marca/listaMarcas
    public String guardarCambiosMarca(@PathVariable String id, @RequestParam String nuevoNombre, ModelMap model, RedirectAttributes attr) {
        try {
            marcaServicio.modificarMarca(id, nuevoNombre);
            attr.addFlashAttribute("exito", "¡Los cambios se guardaron exitosamente!");
        } catch (Exception e) {
            attr.addFlashAttribute("error", "Ha ocurrido un error inesperado.");
            return "marca/modificar";
        }
        System.out.println("5");
        return "redirect:/marcas/modificar/{id}";
    }

    @GetMapping("/search") //para buscar
    public String findMarcaByKeyword(ModelMap model, @Param("keyword") String keyword) {
        List<Marca> filteredMarca = marcaServicio.find(keyword);
        model.addAttribute("marcas", filteredMarca);
        return "marcas/listaMarcas";
    }
}
