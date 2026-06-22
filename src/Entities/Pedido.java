/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import Enums.Estado;
import Enums.FormaPago;
import Interfaces.Calculable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ignac
 */
public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    private Estado estado;
    private double total;
    private FormaPago formaPago;
    private Usuario usuario;
    private List<DetallePedido> detallePedidos = new ArrayList<>();

    public Pedido() {
        super();
        this.fecha = LocalDate.now(); 
        this.estado = Estado.PENDIENTE; 
        this.total = 0.0;    
    }
    public Pedido(FormaPago formaPago, Usuario usuario) {
        super();
        this.fecha = LocalDate.now();
        this.estado = Estado.PENDIENTE;
        this.formaPago = formaPago;
        this.usuario = usuario;
        this.total = 0.0;
    }
    public void addDetallePedido(int cantidad, Double subtotal, Producto producto) {
        DetallePedido nuevoDetalle = new DetallePedido(cantidad, producto);
        if (subtotal != null && subtotal > 0) {
            nuevoDetalle.setSubTotal(subtotal);
        }
        this.detallePedidos.add(nuevoDetalle);
        calcularTotal(); 
    }
    
    public DetallePedido findeDetallePedidoByProducto(Producto producto) {
         for (DetallePedido detallePedido : detallePedidos) {
            if (detallePedido.getProducto().getId().equals(producto.getId())){
                return detallePedido;
            }
        }
        return null;
    }
    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido aEliminar = findeDetallePedidoByProducto(producto);
        if (aEliminar != null) {
            this.detallePedidos.remove(aEliminar);
            calcularTotal(); 
        }
    }
    

    @Override
    public void calcularTotal() {
        double suma = 0.0;
        for (DetallePedido d : detallePedidos) {
            suma += d.getSubTotal();
        }
        this.total = suma;
    }
    
// get y set 
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetallePedido> getDetallePedidos() {
        return detallePedidos;
    }

    public void setDetallePedidos(List<DetallePedido> detallePedidos) {
        this.detallePedidos = detallePedidos;
    }
    

    @Override
    public String toString() {
        return "Pedido{" + "fecha=" + fecha + ", estado=" + estado + ", total=" + total + ", formaPago=" + 
                formaPago + ", usuario=" + usuario + ", detallePedidos=" + detallePedidos + '}';
    }

    
    
}
