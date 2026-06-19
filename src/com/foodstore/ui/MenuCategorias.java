package com.foodstore.ui;

import com.foodstore.exception.EntityNotFoundException;
import com.foodstore.model.Categoria;
import com.foodstore.service.CategoriaService;
import java.util.List;

public class MenuCategorias {
    private final CategoriaService service = new CategoriaService();
    private final Consola consola;

    public MenuCategorias(Consola consola) { this.consola = consola; }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Módulo: Categorías ---");
            System.out.println("1. Listar Activas");
            System.out.println("2. Crear Nueva");
            System.out.println("3. Editar Existente");
            System.out.println("4. Eliminar (Baja Lógica)");
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
        List<Categoria> lista = service.listarActivos();
        if (lista.isEmpty()) { System.out.println("No hay categorías registradas."); return; }
        lista.forEach(System.out::println);
    }

    private void crear() {
        String nombre = consola.leerTexto("Nombre: ");
        String desc = consola.leerTextoOpcional("Descripción: ");
        service.crear(new Categoria(nombre, desc));
        System.out.println("✅ Categoría almacenada con éxito.");
    }

    private void editar() {
        listar();
        long id = consola.leerLong("ID de categoría a modificar: ");
        try {
            Categoria c = service.buscarPorId(id);
            c.setNombre(consola.leerTexto("Nuevo nombre: "));
            c.setDescripcion(consola.leerTextoOpcional("Nueva descripción: "));
            service.actualizar(c);
            System.out.println("✅ Cambios aplicados con éxito.");
        } catch (EntityNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void eliminar() {
        listar();
        long id = consola.leerLong("ID de categoría a eliminar: ");
        try {
            service.eliminar(id);
            System.out.println("✅ Eliminación lógica realizada.");
        } catch (EntityNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
}