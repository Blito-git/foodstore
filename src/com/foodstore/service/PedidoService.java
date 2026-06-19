package com.foodstore.service;

import com.foodstore.dao.IBaseDAO;
import com.foodstore.dao.PedidoDAO;
import com.foodstore.dao.ProductoDAO;
import com.foodstore.model.DetallePedido;
import com.foodstore.model.Pedido;
import com.foodstore.model.Producto;
import java.util.List;

public class PedidoService extends GenericService<Pedido> {
    private final PedidoDAO dao = new PedidoDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();

    @Override
    protected IBaseDAO<Pedido> getDAO() { return dao; }

    @Override
    public void crear(Pedido pedido) {
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto prod = productoDAO.buscarPorId(detalle.getProductoId());
            if (prod == null || !prod.getDisponible()) {
                throw new RuntimeException("El producto ID " + detalle.getProductoId() + " no está disponible.");
            }
            if (prod.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + prod.getNombre() + " (Disponibles: " + prod.getStock() + ")");
            }
        }
        super.crear(pedido);
    }

    public List<DetallePedido> listarDetalles(Long pedidoId) {
        buscarPorId(pedidoId);
        return dao.listarDetallesPorPedido(pedidoId);
    }
}