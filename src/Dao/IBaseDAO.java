/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author ignac
 */
public interface IBaseDAO<T> {
    void guardar(T entidad) throws SQLException;
    void actualizar(T entidad) throws SQLException;
    void eliminarLogico(Long id) throws SQLException;
    T buscarPorId(Long id) throws SQLException;
    List<T> listarTodos() throws SQLException;
}
