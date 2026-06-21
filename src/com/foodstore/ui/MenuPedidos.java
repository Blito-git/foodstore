package com.foodstore.ui;

import com.foodstore.exception.EntityNotFoundException;
import com.foodstore.model.*;
import com.foodstore.service.PedidoService;
import com.foodstore.service.ProductoService;
import com.foodstore.service.UsuarioService;
import java.util.List;

public class MenuPedidos {
    private final PedidoService service = new PedidoService();
    private final ProductoService prodService = new ProductoService();
    private final UsuarioService usrService = new UsuarioService();
    private final Consola consola;

    public MenuPedidos(Consola consola) { this.consola = consola; }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Módulo: Comandas y Pedidos ---");
            System.out.println("1. Listar Historial Activo");
            System.out.println("2. Inspeccionar Detalle de Pedido");
            System.out.println("3. Confeccionar Nuevo Pedido");
            System.out.println("4. Cambiar Estado / Parámetros");
            System.out.println("5. Eliminar Pedido");
            System.out.println("0. Volver");
            int op = consola.leerEntero("Opción: ");
            switch (op) {
                case 1 -> listar();
                case 2 -> verDetalle();
                case 3 -> crear();
                case 4 -> cambiarEstado();
                case 5 -> eliminar();
                case 0 -> volver = true;
            }
        }
    }

    private void listar() {
        List<Pedido> lista = service.listarActivos();
        if (lista.isEmpty()) { System.out.println("Sin movimientos comerciales."); return; }
        lista.forEach(System.out::println);
    }

    private void verDetalle() {
        listar();
        long id = consola.leerLong("ID del Pedido: ");
        try {
            List<DetallePedido> detalles = service.listarDetalles(id);
            System.out.println("--- Desglose de Artículos ---");
            detalles.forEach(System.out::println);
        } catch (EntityNotFoundException e) {
            System.out.println(" " + e.getMessage());
        }
    }

    private void crear() {
        long usrId = consola.leerLong("ID de Cliente Operador: ");
        try {
            usrService.buscarPorId(usrId);
        } catch (Exception e) {
            System.out.println(" El usuario no existe en la base.");
            return;
        }

        Pedido p = new Pedido();
        p.setUsuarioId(usrId);
        p.setFormaPago(consola.leerEnum("Seleccionar pasarela de pago", FormaPago.class));

        boolean cargando = true;
        while (cargando) {
            long prodId = consola.leerLong("ID del Producto a agregar (0 para cerrar la carga): ");
            if (prodId == 0) { cargando = false; continue; }
            
            try {
                Producto prod = prodService.buscarPorId(prodId);
                int cant = consola.leerEntero("Cantidad de unidades: ");
                if (cant <= 0) { System.out.println("Cantidad inválida."); continue; }
                p.addDetallePedido(prod, cant);
                System.out.println("Item cargado al carrito.");
            } catch (Exception e) {
                System.out.println("Producto inexistente o inactivo.");
            }
        }

        if (p.getDetalles().isEmpty()) {
            System.out.println("Orden vacía. Transacción cancelada.");
            return;
        }

        try {
            service.crear(p);
            System.out.printf("Éxito. Comanda #%d confirmada. Importe Debitados: $%.2f%n", p.getId(), p.getTotal());
        } catch (Exception e) {
            System.out.println("Falla crítica de stock o integridad: " + e.getMessage());
        }
    }

    private void cambiarEstado() {
        listar();
        long id = consola.leerLong("ID del Pedido: ");
        try {
            Pedido p = service.buscarPorId(id);
            p.setEstado(consola.leerEnum("Nuevo Estado de la Comanda", Estado.class));
            p.setFormaPago(consola.leerEnum("Actualizar método de pago", FormaPago.class));
            service.actualizar(p);
            System.out.println("Comanda re-encuadrada.");
        } catch (EntityNotFoundException e) {
            System.out.println(" " + e.getMessage());
        }
    }

    private void eliminar() {
        listar();
        long id = consola.leerLong("ID de Pedido a anular: ");
        try {
            service.eliminar(id);
            System.out.println("Pedido anulado lógicamente.");
        } catch (EntityNotFoundException e) {
            System.out.println(" " + e.getMessage());
        }
    }
}