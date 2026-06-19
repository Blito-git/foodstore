package com.foodstore.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido extends Base implements Calculable {
    private Long usuarioId;
    private LocalDate fecha = LocalDate.now();
    private Estado estado = Estado.PENDIENTE;
    private double total;
    private FormaPago formaPago;
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido() {}

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public FormaPago getFormaPago() { return formaPago; }
    public void setFormaPago(FormaPago formaPago) { this.formaPago = formaPago; }

    public List<DetallePedido> getDetalles() { return detalles; }

    public void addDetallePedido(Producto p, int cantidad) {
        DetallePedido detalle = new DetallePedido();
        detalle.setProductoId(p.getId());
        detalle.setCantidad(cantidad);
        detalle.setSubtotal(p.getPrecio() * cantidad);
        detalle.setNombreProducto(p.getNombre());
        detalles.add(detalle);
    }

    @Override
    public double calcularTotal() {
        this.total = detalles.stream().mapToDouble(DetallePedido::getSubtotal).sum();
        return this.total;
    }

    @Override
    public String toString() {
        return String.format("ID Pedido: %d | Usuario ID: %d | Fecha: %s | Estado: %s | Total: $%.2f | Pago: %s", 
                getId(), usuarioId, fecha, estado, total, formaPago);
    }
}