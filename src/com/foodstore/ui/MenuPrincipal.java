package com.foodstore.ui;

import java.util.Scanner;

public class MenuPrincipal {
    private final Consola consola;
    private final MenuCategorias menuCategorias;
    private final MenuProductos menuProductos;
    private final MenuUsuarios menuUsuarios;
    private final MenuPedidos menuPedidos;

    public MenuPrincipal(Scanner scanner) {
        this.consola = new Consola(scanner);
        this.menuCategorias = new MenuCategorias(consola);
        this.menuProductos = new MenuProductos(consola);
        this.menuUsuarios = new MenuUsuarios(consola);
        this.menuPedidos = new MenuPedidos(consola);
    }

    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n=== FOOD STORE - MENÚ PRINCIPAL ===");
            System.out.println("1. Gestión de Categorías");
            System.out.println("2. Gestión de Productos");
            System.out.println("3. Gestión de Usuarios");
            System.out.println("4. Gestión de Pedidos");
            System.out.println("0. Salir de la Aplicación");
            int op = consola.leerEntero("Opción: ");
            switch (op) {
                case 1 -> menuCategorias.mostrar();
                case 2 -> menuProductos.mostrar();
                case 3 -> menuUsuarios.mostrar();
                case 4 -> menuPedidos.mostrar();
                case 0 -> salir = true;
                default -> System.out.println("Opción no válida.");
            }
        }
    }
}