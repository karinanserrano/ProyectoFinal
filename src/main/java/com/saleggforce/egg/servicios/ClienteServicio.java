package com.saleggforce.egg.servicios;

import com.saleggforce.egg.entidades.Cliente;
import com.saleggforce.egg.entidades.Producto;
import com.saleggforce.egg.enums.Estado;
import static com.saleggforce.egg.enums.Estado.PENDIENTE;
import com.saleggforce.egg.enums.HorarioContacto;
import com.saleggforce.egg.errores.ErrorServicio;
import com.saleggforce.egg.repositorios.ClienteRepositorio;
import com.saleggforce.egg.repositorios.ProductoRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServicio {

    public final ClienteRepositorio clienteRepositorio;
    public final ProductoRepositorio productoRepositorio;

    @Autowired
    public ClienteServicio(ClienteRepositorio clienteRepositorio, ProductoRepositorio productoRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
        this.productoRepositorio = productoRepositorio;
    }

    @Transactional
    public void crearCliente(Cliente cliente, String idProducto) throws ErrorServicio {
        Optional<Producto> respuesta = productoRepositorio.findById(idProducto);
        if (respuesta.isPresent()) {
            Producto producto = respuesta.get();
            cliente.setProducto(producto);
        }
        cliente.setFechaConsulta(new Date());
        cliente.setAlta(true);
        cliente.setEstado(PENDIENTE);
        cliente.setComentario((new Date().getDay() + 1) + "/" + (new Date().getMonth() + 1) + "/" + (new Date().getYear() + 1900) + ": ALTA || ");
        validarCliente(cliente);
        clienteRepositorio.save(cliente);
    }

    public void modificarCliente(String id, String nuevoNombre,
            String nuevoApellido, String nuevoMail, String nuevoTelefono,
            HorarioContacto nuevoHorarioContacto, Estado nuevoEstado, String nuevoComentario) throws ErrorServicio {
        Optional<Cliente> respuesta = clienteRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();

            if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
                cliente.setNombre(nuevoNombre);
            }

            if (nuevoApellido != null && !nuevoApellido.isEmpty()) {
                cliente.setApellido(nuevoApellido);
            }

            if (nuevoMail != null && !nuevoMail.isEmpty()) {
                cliente.setMail(nuevoMail);
            }

            if (nuevoTelefono != null && !nuevoTelefono.isEmpty()) {
                cliente.setTelefono(nuevoTelefono);
            }

            if (nuevoHorarioContacto != null) {
                cliente.setHorarioContacto(nuevoHorarioContacto);
            }

            if (nuevoEstado != null) {
                cliente.setEstado(nuevoEstado);
            }

            if (nuevoComentario != null && !nuevoComentario.isEmpty()) {
                cliente.setComentario(cliente.getComentario().concat((new Date().getDay() + 1) + "/" + (new Date().getMonth() + 1) + "/" + (new Date().getYear() + 1900) + ": " + nuevoComentario + " || "));
            }

            validarCliente(cliente);
            clienteRepositorio.save(cliente);
        } else {
            throw new ErrorServicio("No existe el cliente");
        }
    }

    @Transactional
    public Cliente altaCliente(String id) throws ErrorServicio {
        Cliente cliente = clienteRepositorio.getOne(id);
        cliente.setAlta(true);
        return cliente;
    }

    @Transactional
    public Cliente bajaCliente(String id) throws ErrorServicio {
        Cliente cliente = clienteRepositorio.getOne(id);
        cliente.setAlta(false);
        return cliente;
    }

    @Transactional
    public List<Cliente> listarClientes() {
        return clienteRepositorio.finByAllOrderByEstado(PENDIENTE);
    }

    //Busca por nombre o apellido
    @Transactional
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepositorio.findByApellidoOrNombre(nombre);
    }

    public Cliente getOne(String id) {
        return clienteRepositorio.getOne(id);
    }

    @Transactional
    public List<Cliente> find(String keyword) {
        return clienteRepositorio.find(keyword);
    }

    private void validarCliente(Cliente cliente) throws ErrorServicio {
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new ErrorServicio("El nombre no debe estar vacio");
        }
        if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
            throw new ErrorServicio("El apellido no debe estar vacio");
        }
        if (cliente.getMail() == null || cliente.getMail().isEmpty()) {
            throw new ErrorServicio("El mail no puede estar vacío");
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().isEmpty()) {
            throw new ErrorServicio("El telefono no puede estar vacío");
        }
    }
}
