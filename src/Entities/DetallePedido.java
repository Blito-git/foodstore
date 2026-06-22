/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

/**
 *
 * @author ignac
 */
public class DetallePedido extends Base {
    private double subTotal;
    private int cantidad;
    private Producto producto;

    public DetallePedido() {
        super();
    }

    public DetallePedido(int cantidad, Producto producto) {
        super();
        this.cantidad = cantidad;
        this.producto = producto;
        this.subTotal = (producto != null) ? cantidad * producto.getPrecio() : 0.0;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        if (this.producto != null) {
            this.subTotal = cantidad * this.producto.getPrecio();
        }
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null) {
            this.subTotal = this.cantidad * producto.getPrecio();
        }
    }

    public int getCantidad() {
        return cantidad;
    }


    public Producto getProducto() {
        return producto;
    }

    @Override
    public String toString() {
        return "DetallePedido{" + "subTotal=" + subTotal + ", cantidad=" +
                cantidad + ", producto=" + producto + '}';
    }

    
    
    
    
}
