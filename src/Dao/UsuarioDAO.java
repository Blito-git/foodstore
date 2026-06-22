/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import Config.ConexionDB;
import Entities.Usuario;
import Enums.Rol;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ignac
 */
public class UsuarioDAO implements IBaseDAO<Usuario> {

    @Override
    public void guardar(Usuario entidad) throws SQLException {
        String sql = "INSERT INTO usuario (nombre, apellido, mail, celular, contraseña, rol, eliminado, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellido());
            ps.setString(3, entidad.getEmail());
            ps.setString(4, entidad.getCelular());
            ps.setString(5, entidad.getContraseña());
            ps.setString(6, entidad.getRol().name());
            ps.setBoolean(7, entidad.isEliminado());
            ps.setTimestamp(8, Timestamp.valueOf(entidad.getCreatedAt()));
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getLong(1));
                }
            }
        }
    }

    @Override
    public void actualizar(Usuario entidad) throws SQLException {
        String sql = "UPDATE usuario SET nombre = ?, apellido = ?, mail = ?, celular = ?, contraseña = ?, rol = ? WHERE id = ? AND eliminado = false";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellido());
            ps.setString(3, entidad.getEmail());
            ps.setString(4, entidad.getCelular());
            ps.setString(5, entidad.getContraseña());
            ps.setString(6, entidad.getRol().name());
            ps.setLong(7, entidad.getId());
            
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminarLogico(Long id) throws SQLException {
        String sql = "UPDATE usuario SET eliminado = true WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Usuario buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ? AND eliminado = false";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usr = new Usuario();
                    usr.setId(rs.getLong("id"));
                    usr.setNombre(rs.getString("nombre"));
                    usr.setApellido(rs.getString("apellido"));
                    usr.setEmail(rs.getString("mail"));
                    usr.setCelular(rs.getString("celular"));
                    usr.setContraseña(rs.getString("contraseña"));
                    usr.setRol(Rol.valueOf(rs.getString("rol")));
                    usr.setEliminado(rs.getBoolean("eliminated" != null ? "eliminado" : "eliminado")); 
                    usr.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return usr;
                }
            }
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE eliminado = false";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Usuario usr = new Usuario();
                usr.setId(rs.getLong("id"));
                usr.setNombre(rs.getString("nombre"));
                usr.setApellido(rs.getString("apellido"));
                usr.setEmail(rs.getString("mail"));
                usr.setCelular(rs.getString("celular"));
                usr.setContraseña(rs.getString("contraseña"));
                usr.setRol(Rol.valueOf(rs.getString("rol")));
                usr.setEliminado(rs.getBoolean("eliminado"));
                usr.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                lista.add(usr);
            }
        }
        return lista;
    }
}
