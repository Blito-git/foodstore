package com.foodstore.dao;

import com.foodstore.model.Rol;
import com.foodstore.model.Usuario;
import com.foodstore.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements IBaseDAO<Usuario> {

    @Override
    public void crear(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, apellido, mail, celular, contrasena, rol, eliminado) VALUES (?,?,?,?,?,?,0)";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getMail());
            ps.setString(4, u.getCelular());
            ps.setString(5, u.getContrasena());
            ps.setString(6, u.getRol().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) u.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public Usuario buscarPorId(Long id) {
        String sql = "SELECT * FROM usuarios WHERE id = ? AND eliminado = 0";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Usuario> listarActivos() {
        String sql = "SELECT * FROM usuarios WHERE eliminado = 0";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public void actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET nombre=?, apellido=?, mail=?, celular=?, contrasena=?, rol=? WHERE id=? AND eliminado=0";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getMail());
            ps.setString(4, u.getCelular());
            ps.setString(5, u.getContrasena());
            ps.setString(6, u.getRol().name());
            ps.setLong(7, u.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "UPDATE usuarios SET eliminado = 1 WHERE id = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage(), e);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getLong("id"));
        u.setNombre(rs.getString("nombre"));
        u.setApellido(rs.getString("apellido"));
        u.setMail(rs.getString("mail"));
        u.setCelular(rs.getString("celular"));
        u.setContrasena(rs.getString("contrasena"));
        u.setRol(Rol.valueOf(rs.getString("rol")));
        u.setEliminado(rs.getBoolean("eliminado"));
        return u;
    }
}