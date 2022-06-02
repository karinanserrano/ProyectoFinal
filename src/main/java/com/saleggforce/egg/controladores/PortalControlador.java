package com.saleggforce.egg.controladores;

import com.saleggforce.egg.entidades.Marca;
import com.saleggforce.egg.servicios.EmpleadoServicio;
import com.saleggforce.egg.servicios.MarcaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller 
public class PortalControlador {

    private final EmpleadoServicio empleadoServicio;
    private final MarcaServicio marcaServicio;

    @Autowired
    public PortalControlador(EmpleadoServicio empleadoServicio, MarcaServicio marcaServicio) {
        this.empleadoServicio = empleadoServicio;
        this.marcaServicio = marcaServicio;
    }

    //Muestra la pagina de inicio
    @GetMapping()
    public String index(ModelMap model) {
        List<Marca> marca = marcaServicio.listarMarcas();
        model.addAttribute("marcas", marca);
        return "index.html";
    }

    //Muestra la pagina de login y procesa el logincheck que lleva a una nueva vista
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o password incorrecto");
        }
        if (logout != null) {
            model.put("logout", "La sesión finalizo con éxito!");
        }
        return "empleados/login";
    }
}
