/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import Exception.EntityNotFoundException;
import Exception.StockInvalidoException;
import Entities.DetallePedido;
import Entities.Pedido;
import Entities.Producto;
import Dao.PedidoDAO;
import Dao.ProductoDAO;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author ignac
 */
public class PedidoService {
    private final PedidoDAO pedidoDAO;
    private final ProductoDAO productoDAO; 

    public PedidoService() {
        this.pedidoDAO = new PedidoDAO();
        this.productoDAO = new ProductoDAO();
    }

    public void registrarPedido(Pedido pedido) throws SQLException, StockInvalidoException, EntityNotFoundException {
       
        for (DetallePedido detalle : pedido.getDetallePedidos()) {
            Producto prodBD = productoDAO.buscarPorId(detalle.getProducto().getId());
            
            if (prodBD == null) {
                throw new EntityNotFoundException("El producto '" + detalle.getProducto().getNombre() + " ya no existe en el catalogo");
            }
            
            if (prodBD.getStock() < detalle.getCantidad()) {
                throw new StockInvalidoException("Stock insuficiente para '" + prodBD.getNombre() + "Solicitado: " + detalle.getCantidad() + " Disponible: " + prodBD.getStock());
            }
            
            int nuevoStock = prodBD.getStock() - detalle.getCantidad();
            prodBD.setStock(nuevoStock);
            productoDAO.actualizar(prodBD);
        }
        
        pedidoDAO.guardar(pedido);
    }

    public void actualizarEstadoPedido(Long id,Enums.Estado nuevoEstado) throws SQLException, EntityNotFoundException {
        Pedido pedido = pedidoDAO.buscarPorId(id);
        if (pedido == null) {
            throw new EntityNotFoundException("No se encontro pedido con el ID: " + id);
        }
        pedido.setEstado(nuevoEstado);
        pedidoDAO.actualizar(pedido);
    }

    public void darDeBajaPedido(Long id) throws SQLException, EntityNotFoundException {
        Pedido pedido = pedidoDAO.buscarPorId(id);
        if (pedido == null) {
            throw new EntityNotFoundException("No se encontro ningun pedido con el ID: " + id);
        }
        pedidoDAO.eliminarLogico(id);
    }

    public Pedido buscarPorId(Long id) throws SQLException, EntityNotFoundException {
        Pedido pedido = pedidoDAO.buscarPorId(id);
        if (pedido == null) {
            throw new EntityNotFoundException("No se encontro ningun pedido con el ID: " + id);
        }
        return pedido;
    }

    public List<Pedido> obtenerTodos() throws SQLException {
        return pedidoDAO.listarTodos();
    }
}
