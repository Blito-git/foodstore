/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import Exception.EntityNotFoundException;
import Exception.PrecioInvalidoException;
import Exception.StockInvalidoException;
import Entities.Producto;
import Dao.ProductoDAO;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author ignac
 */
public class ProductoService {
    private final ProductoDAO productoDAO;
    
    public ProductoService() {
        this.productoDAO = new ProductoDAO();
    }
    public void registrarProducto(Producto producto) throws SQLException, PrecioInvalidoException, StockInvalidoException {
        
        if (producto.getPrecio() < 0) {
            throw new PrecioInvalidoException("Error: El precio del producto no puede ser inferior a $0");
        }
        if (producto.getStock() < 0) {
            throw new StockInvalidoException("Error: El stock inicial del producto no puede ser negativo");
        }
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        
        productoDAO.guardar(producto);
    }

    public void modificarProducto(Producto producto) throws SQLException, EntityNotFoundException, PrecioInvalidoException, StockInvalidoException {
        
        buscarPorId(producto.getId());
        
        if (producto.getPrecio() < 0) {
            throw new PrecioInvalidoException("Error: El precio de actualizacion no puede ser inferior a $0");
        }
        if (producto.getStock() < 0) {
            throw new StockInvalidoException("Error: El stock de actualizacion no puede ser negativo");
        }
        
        productoDAO.actualizar(producto);
    }

    public void darDeBajaProducto(Long id) throws SQLException, EntityNotFoundException {
        buscarPorId(id);
        productoDAO.eliminarLogico(id);
    }

    public Producto buscarPorId(Long id) throws SQLException, EntityNotFoundException {
        Producto producto = productoDAO.buscarPorId(id);
        if (producto == null) {
            throw new EntityNotFoundException("No se encontro ningun producto activo con el ID: " + id);
        }
        return producto;
    }

    public List<Producto> obtenerTodos() throws SQLException {
        return productoDAO.listarTodos();
    }
}
