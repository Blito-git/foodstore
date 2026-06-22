/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import Config.ConexionDB;
import Entities.Categoria;
import Entities.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ignac
 */
public class ProductoDAO implements IBaseDAO<Producto> {

    @Override
    public void guardar(Producto entidad) throws SQLException {
        String sql = "INSERT INTO producto (nombre, precio, descripcion, stock, imagen, disponible, categoria_id, eliminado, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, entidad.getNombre());
            ps.setDouble(2, entidad.getPrecio());
            ps.setString(3, entidad.getDescripcion());
            ps.setInt(4, entidad.getStock());
            ps.setString(5, entidad.getImagen());
            ps.setBoolean(6, entidad.isDisponible());
            ps.setLong(7, entidad.getCategoria().getId());
            ps.setBoolean(8, entidad.isEliminado());
            ps.setTimestamp(9, Timestamp.valueOf(entidad.getCreatedAt()));
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getLong(1));
                }
            }
        }
    }

    @Override
    public void actualizar(Producto entidad) throws SQLException {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, descripcion = ?, stock = ?, imagen = ?, disponible = ?, categoria_id = ? WHERE id = ? AND eliminado = false";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, entidad.getNombre());
            ps.setDouble(2, entidad.getPrecio());
            ps.setString(3, entidad.getDescripcion());
            ps.setInt(4, entidad.getStock());
            ps.setString(5, entidad.getImagen());
            ps.setBoolean(6, entidad.isDisponible());
            ps.setLong(7, entidad.getCategoria().getId());
            ps.setLong(8, entidad.getId());
            
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminarLogico(Long id) throws SQLException {
        String sql = "UPDATE producto SET eliminado = true WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Producto buscarPorId(Long id) throws SQLException {
        String sql = "SELECT p.*, c.nombre as cat_nombre, c.descripcion as cat_descripcion " +
                     "FROM producto p " +
                     "INNER JOIN categoria c ON p.categoria_id = c.id " +
                     "WHERE p.id = ? AND p.eliminado = false";
        
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria cat = new Categoria();
                    cat.setId(rs.getLong("categoria_id"));
                    cat.setNombre(rs.getString("cat_nombre"));
                    cat.setDescripcion(rs.getString("cat_descripcion"));
                    
                    
                    Producto prod = new Producto();
                    prod.setId(rs.getLong("id"));
                    prod.setNombre(rs.getString("nombre"));
                    prod.setPrecio(rs.getDouble("precio"));
                    prod.setDescripcion(rs.getString("descripcion"));
                    prod.setStock(rs.getInt("stock"));
                    prod.setImagen(rs.getString("imagen"));
                    prod.setCategoria(cat);
                    prod.setEliminado(rs.getBoolean("eliminado"));
                    prod.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    
                    return prod;
                }
            }
        }
        return null;
    }

    @Override
    public List<Producto> listarTodos() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre as cat_nombre, c.descripcion as cat_descripcion " +
                     "FROM producto p " +
                     "INNER JOIN categoria c ON p.categoria_id = c.id " +
                     "WHERE p.eliminado = false";
        
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setId(rs.getLong("categoria_id"));
                cat.setNombre(rs.getString("cat_nombre"));
                cat.setDescripcion(rs.getString("cat_descripcion"));
                
                Producto prod = new Producto();
                prod.setId(rs.getLong("id"));
                prod.setNombre(rs.getString("nombre"));
                prod.setPrecio(rs.getDouble("precio"));
                prod.setDescripcion(rs.getString("descripcion"));
                prod.setStock(rs.getInt("stock"));
                prod.setImagen(rs.getString("imagen"));
                prod.setCategoria(cat);
                prod.setEliminado(rs.getBoolean("eliminado"));
                prod.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                lista.add(prod);
            }
        }
        return lista;
    }
}
