package com.saleggforce.egg.servicios;

import com.saleggforce.egg.entidades.Empleado;
import com.saleggforce.egg.enums.Role;
import com.saleggforce.egg.errores.ErrorServicio;
import com.saleggforce.egg.repositorios.EmpleadoRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class EmpleadoServicio implements UserDetailsService {

    private final EmpleadoRepositorio empleadoRepositorio;

    @Autowired
    public EmpleadoServicio(EmpleadoRepositorio empleadoRepositorio) {
        this.empleadoRepositorio = empleadoRepositorio;
    }

    @Transactional
    public void crearEmpleado(Empleado empleado) throws Exception {
        empleado.setAlta(true);
        empleado.setRole(Role.USER);
        validarEmpleado(empleado);
        empleado.setClave(new BCryptPasswordEncoder().encode(empleado.getClave()));

        empleadoRepositorio.save(empleado);
    }

    @Transactional
    public void modificarEmpleado(String id, String nuevoNombre, String nuevoApellido,
            String nuevoUsuario, String nuevoDni, String nuevaClave) throws Exception {

        Empleado empleado = empleadoRepositorio.getOne(id);

        empleado.setNombre(nuevoNombre);
        empleado.setApellido(nuevoApellido);
        empleado.setUsuario(nuevoUsuario);
        empleado.setDni(nuevoDni);
        if (nuevaClave != null && !nuevaClave.isEmpty()) {
            empleado.setClave(new BCryptPasswordEncoder().encode(nuevaClave));
        }

        validarEmpleado(empleado);

        empleadoRepositorio.save(empleado);
    }

    @Transactional
    public Empleado altaEmpleado(String id) throws ErrorServicio {
        Optional<Empleado> respuesta = empleadoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Empleado empleado = respuesta.get();
            empleado.setAlta(true);
            return empleado;
        } else {
            throw new ErrorServicio("No se encontró el empleado.");
        }
    }

    @Transactional
    public Empleado bajaEmpleado(String id) throws ErrorServicio {
        Optional<Empleado> respuesta = empleadoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Empleado empleado = respuesta.get();
            empleado.setAlta(false);
            return empleado;
        } else {
            throw new ErrorServicio("No se encontró el empleado.");
        }
    }

    @Transactional
    public List<Empleado> listarEmpleados() {
        List<Empleado> empleados = empleadoRepositorio.findAll();
        return empleados;
    }

    @Transactional
    public List<Empleado> find(String keyword) {
        return empleadoRepositorio.find(keyword);
    }

    @Override
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {

        Empleado empleado = empleadoRepositorio.findByUsuario(usuario);

        if (empleado != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

            //Creo una lista de permisos! 
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + empleado.getRole());
            permisos.add(p1);

            //Esto me permite guardar el OBJETO USUARIO LOG, para luego ser utilizado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("empleadosession", empleado); // llave + valor

            User user = new User(empleado.getUsuario(), empleado.getClave(), permisos);
            return user;

        } else {
            return null;
        }
    }

    @Transactional
    public Empleado getOne(String id) {
        return empleadoRepositorio.getOne(id);
    }

    public Empleado buscarEmpleadoPorId(String id) throws ErrorServicio {
        Optional<Empleado> respuesta = empleadoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Empleado empleado = respuesta.get();
            return empleado;
        } else {
            throw new ErrorServicio("No se encuentra el empleado.");
        }
    }

    public void validarEmpleado(Empleado empleado) throws ErrorServicio {
        if (empleado.getNombre().trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede estar vacío");
        }
        if (empleado.getApellido().trim().isEmpty()) {
            throw new ErrorServicio("El apellido no puede estar vacío");
        }
        if (empleado.getUsuario().trim().isEmpty()) {
            throw new ErrorServicio("El usuario no puede estar vacío");
        }

        Empleado respuesta = empleadoRepositorio.findByUsuario(empleado.getUsuario());
        if (respuesta != null && respuesta.getId() != empleado.getId()) {
            throw new ErrorServicio("El usuario ya existe.");
        }

        if (empleado.getDni().trim().isEmpty()) {
            throw new ErrorServicio("El DNI no puede estar vacío");
        }
        if (empleado.getClave().trim().isEmpty() || empleado.getClave().length() < 5) {
            throw new ErrorServicio("La clave no puede estar vacía ni tener menos de 5 caracteres.");
        }
        if (empleado.getRole() == null) {
            throw new ErrorServicio("El rol no puede estar vacío");
        }

    }
}
