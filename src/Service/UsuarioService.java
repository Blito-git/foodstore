/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import Exception.EntityNotFoundException;
import Entities.Usuario;
import Dao.UsuarioDAO;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author ignac
 */
public class UsuarioService {
    private final UsuarioDAO usuarioDAO;
    
    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public void registrarUsuario(Usuario usuario) throws SQLException {
       
        if (usuario.getEmail()== null || !usuario.getEmail().contains("@") || !usuario.getEmail().contains(".")) {
            throw new IllegalArgumentException("Error: El correo electronico es invalido");
        }
        if (usuario.getContraseña() == null || usuario.getContraseña().trim().isEmpty()) {
            throw new IllegalArgumentException("Error: La contraseña es obligatoria");
        }
        
        usuarioDAO.guardar(usuario);
    }

    public void modificarUsuario(Usuario usuario) throws SQLException, EntityNotFoundException {
        buscarPorId(usuario.getId());
 
        if (usuario.getEmail()== null || !usuario.getEmail().contains("@") || !usuario.getEmail().contains(".")) {
            throw new IllegalArgumentException("Error: El correo de actualizacion es invalido");
        }
        
        usuarioDAO.actualizar(usuario);
    }

    public void darDeBajaUsuario(Long id) throws SQLException, EntityNotFoundException {
        buscarPorId(id);
        usuarioDAO.eliminarLogico(id);
    }

    public Usuario buscarPorId(Long id) throws SQLException, EntityNotFoundException {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new EntityNotFoundException("No se encontro ningun usuario activo con el ID: " + id);
        }
        return usuario;
    }

    public List<Usuario> obtenerTodos() throws SQLException {
        return usuarioDAO.listarTodos();
    }
}
