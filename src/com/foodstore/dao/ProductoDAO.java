package com.foodstore.dao;

import com.foodstore.model.Producto;
import com.foodstore.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO implements IBaseDAO<Producto> {

    @Override
    public void crear(Producto p) {
        String sql = "INSERT INTO productos (nombre, precio, descripcion, stock, imagen, disponible, categoria_id, eliminado) VALUES (?,?,?,?,?,?,?,0)";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setString(3, p.getDescripcion());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getImagen());
            ps.setBoolean(6, p.getDisponible());
            ps.setLong(7, p.getCategoriaId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear producto: " + e.getMessage(), e);
        }
    }

    @Override
    public Producto buscarPorId(Long id) {
        String sql = "SELECT * FROM productos WHERE id = ? AND eliminado = 0";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Producto> listarActivos() {
        String sql = "SELECT * FROM productos WHERE eliminado = 0";
        List<Producto> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public void actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre=?, precio=?, descripcion=?, stock=?, imagen=?, disponible=?, categoria_id=? WHERE id=? AND eliminado=0";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setString(3, p.getDescripcion());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getImagen());
            ps.setBoolean(6, p.getDisponible());
            ps.setLong(7, p.getCategoriaId());
            ps.setLong(8, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "UPDATE productos SET eliminado = 1 WHERE id = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto: " + e.getMessage(), e);
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("id"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getDouble("precio"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setStock(rs.getInt("stock"));
        p.setImagen(rs.getString("imagen"));
        p.setDisponible(rs.getBoolean("disponible"));
        p.setCategoriaId(rs.getLong("categoria_id"));
        p.setEliminado(rs.getBoolean("eliminado"));
        return p;
    }
}