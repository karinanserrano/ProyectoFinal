package com.saleggforce.egg.controladores;

import com.saleggforce.egg.entidades.Empleado;
import com.saleggforce.egg.errores.ErrorServicio;
import com.saleggforce.egg.servicios.EmpleadoServicio;
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
@RequestMapping("/empleados")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
public class EmpleadoControlador {

    private final EmpleadoServicio empleadoServicio;

    @Autowired
    public EmpleadoControlador(EmpleadoServicio empleadoServicio) {
        this.empleadoServicio = empleadoServicio;
    }

    //    Metodo para inicio de empleados
    @GetMapping //empleado/listaEmpleados
    public String inicioEmpleados(ModelMap model) {
        return "empleados/inicioEmpleado";
    }

//    Metodo que busca todos los empleados en el repositorio y los lista
    @GetMapping("/listar") //empleado/listaEmpleados
    public String listar(ModelMap model) {
        List<Empleado> empleados = empleadoServicio.listarEmpleados();
        model.addAttribute("empleados", empleados);
        return "empleados/listaEmpleados";
    }

//  Metodo que mediante un formulario crea un nuevo empleado, 
//  el mismo setea el atributo estado como pendiente rol y alta como 
//  true por defecto   
//  con ModelMap traemos los atributos del objeto
//  El try es para que autocomplete el formulario y el catch para marcar el error, por el cual no se completa el formulario    
    @GetMapping("/formulario")//empleado/listaEmpleados
    public String nuevoEmpleado(@RequestParam(required = false) String id, ModelMap model, RedirectAttributes attr) {
        if (id == null) {
            model.addAttribute("empleado", new Empleado());
        } else {
            try {
                Empleado empleado = empleadoServicio.buscarEmpleadoPorId(id);
                model.addAttribute("empleado", empleado);
                return "empleados/crearEmpleado";
            } catch (Exception e) {
                attr.addFlashAttribute("error", e.getMessage());
                return "empleados/crearEmpleado";
            }
        }
        return "empleados/crearEmpleado";
    }

//  Metodo (Post) que procese el formulario, recibe el objeto completo a traves 
//  de la anotacion @ModelAttribute.
//  en la vista del formulario se debe indicar a que url se dirige la peticion
//  indicar en el formulario que genera una peticion POST  
    @PostMapping("/formulario")//empleado/listaEmpleados
    public String procesarFormulario(@ModelAttribute Empleado empleado, ModelMap model, RedirectAttributes attr) {
        try {
            empleadoServicio.crearEmpleado(empleado);
//        empleadoServicio.crearEmpleado(empleado, clave);
            model.addAttribute("empleado", empleado);
            attr.addFlashAttribute("exito", "¡Vendedor cargado exitosamente!");
        } catch (Exception e) {
            attr.addFlashAttribute("error", "Ha ocurrido un error inesperado.");
            //   model.put("error", "Hubo un error al crear el usuario, por favor reintente.");
            return "empleados/crearEmpleado";
        }
        return "redirect:/empleados/formulario";
    }

//  Metodo para modificar empleados    
    @GetMapping("/modificar/{id}")//empleado/modificarEmpleado
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String modificarEmpleado(@ModelAttribute Empleado empleado, ModelMap model, @PathVariable String id) {
        model.put("empleado", empleadoServicio.getOne(id));
        return "empleados/modificarEmpleado";
    }

    @PostMapping("/modificar/{id}")//empleado/listaEmpleados
    public String guardarCambiosEmpleado(@PathVariable String id, @RequestParam String nuevoNombre, @RequestParam String nuevoApellido, @RequestParam String nuevoUsuario, @RequestParam String nuevoDocumento, @RequestParam String nuevaClave, ModelMap model, RedirectAttributes attr) throws Exception {
        try {
            empleadoServicio.modificarEmpleado(id, nuevoNombre, nuevoApellido, nuevoUsuario, nuevoDocumento, nuevaClave);
            attr.addFlashAttribute("exito", "¡Los cambios se guardaron exitosamente!");
        } catch (Exception e) {
            attr.addFlashAttribute("error", "Ha ocurrido un problema inesperado.");
            return "empleados/modificarEmpleado";
        }
        return "redirect:/empleados/modificar/{id}";
    }

//  Metodo Get para dar de alta y baja los empleados 
//  Agrego el autorizado solo admin  para ver si funciona(no se si asi funcion)    
    @GetMapping("/alta/{id}")//empleado/listaEmpleados
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String altaEmpleado(@PathVariable String id, ModelMap model) {
        try {
            empleadoServicio.altaEmpleado(id);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "empleados/listaEmpleados";
        }
        return "redirect:/empleados/listar";
    }

    @GetMapping("/baja/{id}")//empleado/listaEmpleados
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String bajaEmpleado(@PathVariable String id, ModelMap model) {
        try {
            empleadoServicio.bajaEmpleado(id);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "empleados/listaEmpleados";
        }
        return "redirect:/empleados/listar";
    }

    @GetMapping("/search") //para buscar
    public String findEmpleadoByKeyword(ModelMap model, @Param("keyword") String keyword) {
        List<Empleado> filteredEmpleado = empleadoServicio.find(keyword);
        model.addAttribute("empleados", filteredEmpleado);
        return "empleados/listaEmpleados";
    }

}
//empleados/formulario
//metodo nuevoEmpleado
//
//empleados/modificar/{id}
//metodo modificarEmpleado
//
//empleados/alta/{id}
//metodo altaEmpleado
//
//empleados/baja/{id}
//metodo bajaEmpleado
//
//empleados/listar
//metodo listarEmpleados
