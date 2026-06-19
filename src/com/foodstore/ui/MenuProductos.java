package com.foodstore.ui;

import com.foodstore.exception.EntityNotFoundException;
import com.foodstore.model.Producto;
import com.foodstore.service.CategoriaService;
import com.foodstore.service.ProductoService;
import java.util.List;

public class MenuProductos {
    private final ProductoService service = new ProductoService();
    private final CategoriaService catService = new CategoriaService();
    private final Consola consola;

    public MenuProductos(Consola consola) { this.consola = consola; }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Módulo: Productos ---");
            System.out.println("1. Listar Catálogo");
            System.out.println("2. Registrar Producto");
            System.out.println("3. Modificar Producto");
            System.out.println("4. Eliminar Producto");
            System.out.println("0. Volver");
            int op = consola.leerEntero("Opción: ");
            switch (op) {
                case 1 -> listar();
                case 2 -> crear();
                case 3 -> editar();
                case 4 -> eliminar();
                case 0 -> volver = true;
            }
        }
    }

    private void listar() {
        List<Producto> lista = service.listarActivos();
        if (lista.isEmpty()) { System.out.println("Catálogo vacío."); return; }
        lista.forEach(System.out::println);
    }

    private void crear() {
        if (catService.listarActivos().isEmpty()) {
            System.out.println("❌ Error: Se requiere crear una Categoría antes.");
            return;
        }
        Producto p = new Producto();
        p.setNombre(consola.leerTexto("Nombre: "));
        p.setPrecio(consola.leerDouble("Precio: "));
        p.setDescripcion(consola.leerTextoOpcional("Descripción: "));
        p.setStock(consola.leerEntero("Stock Inicial: "));
        p.setImagen(consola.leerTextoOpcional("URL Imagen: "));
        p.setCategoriaId(consola.leerLong("ID Categoría Relacionada: "));
        
        try {
            catService.buscarPorId(p.getCategoriaId());
            service.crear(p);
            System.out.println("✅ Producto añadido al catálogo.");
        } catch (Exception e) {
            System.out.println("❌ Operación rechazada: " + e.getMessage());
        }
    }

    private void editar() {
        listar();
        long id = consola.leerLong("ID de Producto a editar: ");
        try {
            Producto p = service.buscarPorId(id);
            p.setNombre(consola.leerTexto("Nuevo nombre: "));
            p.setPrecio(consola.leerDouble("Nuevo precio: "));
            p.setDescripcion(consola.leerTextoOpcional("Nueva descripción: "));
            p.setStock(consola.leerEntero("Nuevo Stock: "));
            System.out.print("¿Disponible? (1=SI, 0=NO): ");
            p.setDisponible(consola.leerEntero("") == 1);
            service.actualizar(p);
            System.out.println("✅ Producto actualizado correctamente.");
        } catch (EntityNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void eliminar() {
        listar();
        long id = consola.leerLong("ID de Producto a dar de baja: ");
        try {
            service.eliminar(id);
            System.out.println("✅ Registro desactivado.");
        } catch (EntityNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
}