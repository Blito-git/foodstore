package com.foodstore.dao;

import com.foodstore.model.*;
import com.foodstore.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO implements IBaseDAO<Pedido> {

    @Override
    public void crear(Pedido p) {
        String sqlPedido = "INSERT INTO pedidos (usuario_id, fecha, estado, total, forma_pago, eliminado) VALUES (?,?,?,?,?,0)";
        String sqlDetalle = "INSERT INTO detalles_pedido (pedido_id, producto_id, cantidad, subtotal) VALUES (?,?,?,?)";
        String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id = ?";

        Connection conn = null;
        try {
            conn = ConexionDB.obtenerConexion();
            conn.setAutoCommit(false); // Transaccionalidad explicita

            try (PreparedStatement psP = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                psP.setLong(1, p.getUsuarioId());
                psP.setDate(2, Date.valueOf(p.getFecha()));
                psP.setString(3, p.getEstado().name());
                psP.setDouble(4, p.calcularTotal());
                psP.setString(5, p.getFormaPago().name());
                psP.executeUpdate();
                
                try (ResultSet rs = psP.getGeneratedKeys()) {
                    if (rs.next()) p.setId(rs.getLong(1));
                }
            }

            try (PreparedStatement psD = conn.prepareStatement(sqlDetalle);
                 PreparedStatement psS = conn.prepareStatement(sqlStock)) {
                for (DetallePedido d : p.getDetalles()) {
                    psD.setLong(1, p.getId());
                    psD.setLong(2, d.getProductoId());
                    psD.setInt(3, d.getCantidad());
                    psD.setDouble(4, d.getSubtotal());
                    psD.executeUpdate();

                    psS.setInt(1, d.getCantidad());
                    psS.setLong(2, d.getProductoId());
                    psS.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw new RuntimeException("Transacción fallida. Pedido revertido: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    @Override
    public Pedido buscarPorId(Long id) {
        String sql = "SELECT * FROM pedidos WHERE id = ? AND eliminado = 0";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar pedido: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Pedido> listarActivos() {
        String sql = "SELECT * FROM pedidos WHERE eliminado = 0";
        List<Pedido> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pedidos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public void actualizar(Pedido p) {
        String sql = "UPDATE pedidos SET estado = ?, forma_pago = ? WHERE id = ? AND eliminado = 0";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getEstado().name());
            ps.setString(2, p.getFormaPago().name());
            ps.setLong(3, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "UPDATE pedidos SET eliminado = 1 WHERE id = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar lógicamente el pedido: " + e.getMessage(), e);
        }
    }

    public List<DetallePedido> listarDetallesPorPedido(Long pedidoId) {
        String sql = "SELECT dp.*, p.nombre AS nombre_producto FROM detalles_pedido dp " +
                     "JOIN productos p ON dp.producto_id = p.id WHERE dp.pedido_id = ?";
        List<DetallePedido> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, pedidoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedido d = new DetallePedido();
                    d.setId(rs.getLong("id"));
                    d.setPedidoId(pedidoId);
                    d.setProductoId(rs.getLong("producto_id"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setSubtotal(rs.getDouble("subtotal"));
                    d.setNombreProducto(rs.getString("nombre_producto"));
                    lista.add(d);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar detalles: " + e.getMessage(), e);
        }
        return lista;
    }

    private Pedido mapear(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId(rs.getLong("id"));
        p.setUsuarioId(rs.getLong("usuario_id"));
        p.setFecha(rs.getDate("fecha").toLocalDate());
        p.setEstado(Estado.valueOf(rs.getString("estado")));
        p.setTotal(rs.getDouble("total"));
        p.setFormaPago(FormaPago.valueOf(rs.getString("forma_pago")));
        p.setEliminado(rs.getBoolean("eliminado"));
        return p;
    }
}