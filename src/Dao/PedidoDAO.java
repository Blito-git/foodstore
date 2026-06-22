/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import Config.ConexionDB;
import Entities.DetallePedido;
import Entities.Pedido;
import Entities.Producto;
import Entities.Usuario;
import Enums.Estado;
import Enums.FormaPago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ignac
 */

public class PedidoDAO implements IBaseDAO<Pedido> {

    @Override
    public void guardar(Pedido entidad) throws SQLException {
        String sqlPedido = "INSERT INTO pedido (fecha, estado, total, forma_pago, usuario_id, eliminado, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_pedido (cantidad, subtotal, producto_id, pedido_id, eliminado, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);
            
            try (PreparedStatement psPedido = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                psPedido.setDate(1, Date.valueOf(entidad.getFecha()));
                psPedido.setString(2, entidad.getEstado().name());
                psPedido.setDouble(3, entidad.getTotal());
                psPedido.setString(4, entidad.getFormaPago().name());
                psPedido.setLong(5, entidad.getUsuario().getId());
                psPedido.setBoolean(6, entidad.isEliminado());
                psPedido.setTimestamp(7, Timestamp.valueOf(entidad.getCreatedAt()));
                
                psPedido.executeUpdate();
                
                try (ResultSet rs = psPedido.getGeneratedKeys()) {
                    if (rs.next()) {
                        entidad.setId(rs.getLong(1));
                    }
                }
            }
            
            
            try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                for (DetallePedido detalle : entidad.getDetallePedidos()) {
                    psDetalle.setInt(1, detalle.getCantidad());
                    psDetalle.setDouble(2, detalle.getSubTotal());
                    psDetalle.setLong(3, detalle.getProducto().getId());
                    psDetalle.setLong(4, entidad.getId()); 
                    psDetalle.setBoolean(5, detalle.isEliminado());
                    psDetalle.setTimestamp(6, Timestamp.valueOf(detalle.getCreatedAt()));
                    
                    psDetalle.executeUpdate();
                }
            }
            con.commit();
            
        } catch (SQLException e) {
            if (con != null) {
                con.rollback();
            }
            throw e; 
        } finally {
            if (con != null) {
                con.close(); 
            }
        }
    }

    @Override
    public void actualizar(Pedido entidad) throws SQLException {
        String sql = "UPDATE pedido SET estado = ?, forma_pago = ?, total = ? WHERE id = ? AND eliminado = false";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getEstado().name());
            ps.setString(2, entidad.getFormaPago().name());
            ps.setDouble(3, entidad.getTotal());
            ps.setLong(4, entidad.getId());
            
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminarLogico(Long id) throws SQLException {
        String sqlPedido = "UPDATE pedido SET eliminado = true WHERE id = ?";
        String sqlDetalles = "UPDATE detalle_pedido SET eliminado = true WHERE pedido_id = ?";
        
        try (Connection con = ConexionDB.getConexion()) {
            con.setAutoCommit(false);
            try (PreparedStatement psP = con.prepareStatement(sqlPedido);
                 PreparedStatement psD = con.prepareStatement(sqlDetalles)) {
                
                psP.setLong(1, id);
                psP.executeUpdate();
                
                psD.setLong(1, id);
                psD.executeUpdate();
                
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    @Override
    public Pedido buscarPorId(Long id) throws SQLException {
        String sqlPedido = "SELECT p.*, u.nombre as usr_nombre, u.apellido as usr_apellido, u.mail as usr_mail " +
                           "FROM pedido p INNER JOIN usuario u ON p.usuario_id = u.id " +
                           "WHERE p.id = ? AND p.eliminado = false";
        
        String sqlDetalles = "SELECT d.*, prod.nombre as prod_nombre, prod.precio as prod_precio " +
                            "FROM detalle_pedido d INNER JOIN producto prod ON d.producto_id = prod.id " +
                            "WHERE d.pedido_id = ? AND d.eliminado = false";
        
        try (Connection con = ConexionDB.getConexion()) {
            try (PreparedStatement psP = con.prepareStatement(sqlPedido)) {
                psP.setLong(1, id);
                try (ResultSet rsP = psP.executeQuery()) {
                    if (rsP.next()) {
                        Usuario usr = new Usuario();
                        usr.setId(rsP.getLong("usuario_id"));
                        usr.setNombre(rsP.getString("usr_nombre"));
                        usr.setApellido(rsP.getString("usr_apellido"));
                        usr.setEmail(rsP.getString("usr_email"));
                        
                        Pedido ped = new Pedido();
                        ped.setId(rsP.getLong("id"));
                        ped.setFecha(rsP.getDate("fecha").toLocalDate());
                        ped.setEstado(Estado.valueOf(rsP.getString("estado")));
                        ped.setFormaPago(FormaPago.valueOf(rsP.getString("forma_pago")));
                        ped.setTotal(rsP.getDouble("total"));
                        ped.setUsuario(usr);
                        ped.setEliminado(rsP.getBoolean("eliminado"));
                        ped.setCreatedAt(rsP.getTimestamp("created_at").toLocalDateTime());
                        
                        // buscar los detalles correspondientes
                        try (PreparedStatement psD = con.prepareStatement(sqlDetalles)) {
                            psD.setLong(1, id);
                            try (ResultSet rsD = psD.executeQuery()) {
                                List<DetallePedido> detalles = new ArrayList<>();
                                while (rsD.next()) {
                                    Producto prod = new Producto();
                                    prod.setId(rsD.getLong("producto_id"));
                                    prod.setNombre(rsD.getString("prod_nombre"));
                                    prod.setPrecio(rsD.getDouble("prod_precio"));
                                    
                                    DetallePedido det = new DetallePedido();
                                    det.setId(rsD.getLong("id"));
                                    det.setCantidad(rsD.getInt("cantidad"));
                                    det.setSubTotal(rsD.getDouble("subtotal"));
                                    det.setProducto(prod);
                                    det.setEliminado(rsD.getBoolean("eliminado"));
                                    det.setCreatedAt(rsD.getTimestamp("created_at").toLocalDateTime());
                                    
                                    detalles.add(det);
                                }
                                ped.setDetallePedidos(detalles);
                            }
                        }
                        return ped;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<Pedido> listarTodos() throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sqlIds = "SELECT id FROM pedido WHERE eliminado = false";
        
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sqlIds);
             ResultSet rs = ps.executeQuery()) {
            
            // reutilizo el buscarPorId 
            while (rs.next()) {
                Pedido ped = buscarPorId(rs.getLong("id"));
                if (ped != null) {
                    lista.add(ped);
                }
            }
        }
        return lista;
    }
}
