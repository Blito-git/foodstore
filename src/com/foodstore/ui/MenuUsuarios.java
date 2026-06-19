package com.foodstore.ui;

import com.foodstore.exception.EntityNotFoundException;
import com.foodstore.model.Rol;
import com.foodstore.model.Usuario;
import com.foodstore.service.UsuarioService;
import java.util.List;

public class MenuUsuarios {
    private final UsuarioService service = new UsuarioService();
    private final Consola consola;

    public MenuUsuarios(Consola consola) { this.consola = consola; }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Módulo: Usuarios ---");
            System.out.println("1. Listar Usuarios");
            System.out.println("2. Registrar Usuario");
            System.out.println("3. Actualizar Datos");
            System.out.println("4. Dar de Baja");
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
        List<Usuario> lista = service.listarActivos();
        if (lista.isEmpty()) { System.out.println("No existen usuarios cargados."); return; }
        lista.forEach(System.out::println);
    }

    private void crear() {
        Usuario u = new Usuario();
        u.setNombre(consola.leerTexto("Nombre: "));
        u.setApellido(consola.leerTexto("Apellido: "));
        u.setMail(consola.leerTexto("Email (Único): "));
        u.setCelular(consola.leerTextoOpcional("Celular: "));
        u.setContrasena(consola.leerTexto("Contraseña: "));
        u.setRol(consola.leerEnum("Asignar Rol", Rol.class));
        try {
            service.crear(u);
            System.out.println("✅ Usuario registrado.");
        } catch (Exception e) {
            System.out.println("❌ Error: Mail duplicado o datos corruptos.");
        }
    }

    private void editar() {
        listar();
        long id = consola.leerLong("ID de usuario a editar: ");
        try {
            Usuario u = service.buscarPorId(id);
            u.setNombre(consola.leerTexto("Nuevo nombre: "));
            u.setApellido(consola.leerTexto("Nuevo apellido: "));
            u.setMail(consola.leerTexto("Nuevo mail: "));
            u.setCelular(consola.leerTextoOpcional("Nuevo celular: "));
            u.setContrasena(consola.leerTexto("Nueva contraseña: "));
            u.setRol(consola.leerEnum("Nuevo Rol", Rol.class));
            service.actualizar(u);
            System.out.println("✅ Ficha de usuario actualizada.");
        } catch (EntityNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void eliminar() {
        listar();
        long id = consola.leerLong("ID de usuario a remover: ");
        try {
            service.eliminar(id);
            System.out.println("✅ Usuario desactivado.");
        } catch (EntityNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
}