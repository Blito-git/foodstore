/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Entities.Categoria;
import Config.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ignac
 */
public class CategoriaDAO implements IBaseDAO<Categoria> {

    @Override
    public void guardar(Categoria entidad) throws SQLException {
        String sql = "INSERT INTO categoria (nombre, descripcion, eliminado, created_at) VALUES (?, ?, ?, ?)";
        
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getDescripcion());
            ps.setBoolean(3, entidad.isEliminado());
            ps.setTimestamp(4, Timestamp.valueOf(entidad.getCreatedAt()));
            
            ps.executeUpdate();
            
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getLong(1));
                }
            }
        }
    }

    @Override
    public void actualizar(Categoria entidad) throws SQLException {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ? AND eliminado = false";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getDescripcion());
            ps.setLong(3, entidad.getId());
            
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminarLogico(Long id) throws SQLException {
        // En vez de un DELETE físico, hacemos un UPDATE poniendo eliminado = true (Soft Delete)
        String sql = "UPDATE categoria SET eliminado = true WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Categoria buscarPorId(Long id) throws SQLException {
        // Solo buscamos categorías que no estén eliminadas lógicamente
        String sql = "SELECT * FROM categoria WHERE id = ? AND eliminado = false";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria cat = new Categoria();
                    cat.setId(rs.getLong("id"));
                    cat.setNombre(rs.getString("nombre"));
                    cat.setDescripcion(rs.getString("descripcion"));
                    cat.setEliminado(rs.getBoolean("eliminado"));
                    cat.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return cat;
                }
            }
        }
        return null; 
    }

    @Override
    public List<Categoria> listarTodos() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoria WHERE eliminado = false";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setId(rs.getLong("id"));
                cat.setNombre(rs.getString("nombre"));
                cat.setDescripcion(rs.getString("descripcion"));
                cat.setEliminado(rs.getBoolean("eliminado"));
                cat.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                lista.add(cat);
            }
        }
        return lista;
    }
}
