package com.foodstore.model;

public class DetallePedido {
    private Long id;
    private Long pedidoId;
    private Long productoId;
    private int cantidad;
    private double subtotal;
    private String nombreProducto; // Campo auxiliar para UI

    public DetallePedido() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    @Override
    public String toString() {
        return String.format("  - Prod ID: %d (%s) x%d | Subtotal: $%.2f", productoId, nombreProducto, cantidad, subtotal);
    }
}