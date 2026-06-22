/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;
import Entities.*;
import Enums.Estado;
import Enums.FormaPago;
import Enums.Rol;
import Exception.EntityNotFoundException;
import Exception.StockInvalidoException;
import Service.CategoriaService;
import Service.PedidoService;
import Service.ProductoService;
import Service.UsuarioService;
import java.sql.SQLException;
import java.util.Scanner;
/**
 *
 * @author ignac
 */
public class Main {

    private static final Scanner teclado = new Scanner(System.in);
    private static final CategoriaService categoriaService = new CategoriaService();
    private static final ProductoService productoService = new ProductoService();
    private static final UsuarioService usuarioService = new UsuarioService();
    private static final PedidoService pedidoService = new PedidoService();

    public static void main(String[] args) {
        teclado.useDelimiter("\n");
        int opcion = 0;

        do {
            System.out.println("\n============== SISTEMA DE GESTION DE PEDIDOS ==============");
            System.out.println("1. GESTIONAR PRODUCTOS Y CATEGORIAS");
            System.out.println("2. GESTIONAR USUARIOS");
            System.out.println("3. INTERFAZ DE VENTAS (CREAR PEDIDOS)");
            System.out.println("4. VER HISTORIAL DE PEDIDOS");
            System.out.println("5. SALIR");
            System.out.print("Seleccione una opcion: ");
            
            try {
                opcion = Integer.parseInt(teclado.next());
                switch (opcion) {
                    case 1: menuCatalogo(); 
                    break;
                    case 2: menuUsuarios();
                    break;
                    case 3: interfazVentas(); 
                    break;
                    case 4: listarHistorialPedidos(); break;
                    case 5: System.out.println("Saliendo del sistema... "); 
                    break;
                    default: System.out.println("Opcion invalida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un numero valido");
            }
        } while (opcion != 5);
    }

    // ================= SUBMENU: CATÁLOGO (PRODUCTOS Y CATEGORIAS) =================
    private static void menuCatalogo() {
        int op = 0;
        do {
            System.out.println("\n--- MODULO DE CATALOGO ---");
            System.out.println("1. Listar Categorias");
            System.out.println("2. Agregar Categoria");
            System.out.println("3. Listar Productos");
            System.out.println("4. Agregar Producto");
            System.out.println("5. Volver al Menu Principal");
            System.out.print("Seleccione una opcion: ");
            try {
                op = Integer.parseInt(teclado.next());
                switch (op) {
                    case 1:
                        System.out.println("\n--- LISTA DE CATEGORIAS ---");
                        for (Categoria c : categoriaService.obtenerTodas()) {
                         System.out.println(c);
                        }
                        break;
                    case 2:
                        Categoria nuevaCat = new Categoria();
                        System.out.print("Nombre de la categoria: ");
                        nuevaCat.setNombre(teclado.next());
                        System.out.print("Descripcion: ");
                        nuevaCat.setDescripcion(teclado.next());
                        categoriaService.registrarCategoria(nuevaCat);
                        System.out.println("Categoria registrada con exito");
                        break;
                    case 3:
                        for (Producto p : productoService.obtenerTodos()) {
                            System.out.println("ID: " + p.getId() + " | " + p.getNombre() + " | Precio: $" + p.getPrecio() + " | Stock: " + p.getStock() + " | Disponible: " + p.isDisponible());
                        }
                        break;
                    case 4:
                        System.out.println("\n--- REGISTRAR NUEVO PRODUCTO ---");
                        System.out.print("Seleccione el ID de la Categoria para el producto: ");
                        Long catId = Long.parseLong(teclado.next());
                        Categoria catAsociada = categoriaService.buscarPorId(catId);
                        
                        Producto nuevoProd = new Producto();
                        nuevoProd.setCategoria(catAsociada);
                        System.out.print("Nombre del producto: ");
                        nuevoProd.setNombre(teclado.next());
                        System.out.print("Precio: ");
                        nuevoProd.setPrecio(Double.parseDouble(teclado.next()));
                        System.out.print("Descripcion: ");
                        nuevoProd.setDescripcion(teclado.next());
                        System.out.print("Stock inicial: ");
                        nuevoProd.setStock(Integer.parseInt(teclado.next()));
                        nuevoProd.setImagen("default.png");
                        
                        productoService.registrarProducto(nuevoProd);
                        System.out.println("Producto registrado con exito");
                        break;
                }
            } catch (Exception e) {
                
                System.out.println("}Error : " + e.getMessage());
            }
        } while (op != 5);
    }

    // ================= SUBMENU: USUARIOS =================
    private static void menuUsuarios() {
        int op = 0;
        do {
            System.out.println("\n--- MODULO DE USUARIOS ---");
            System.out.println("1. Listar Usuarios");
            System.out.println("2. Registrar Usuario");
            System.out.println("3. Volver al Menu Principal");
            System.out.print("Seleccione una opcion: ");
            try {
                op = Integer.parseInt(teclado.next());
                if (op == 1) {
                    System.out.println("\n--- LISTA DE USUARIOS ---");
                    for (Usuario u : usuarioService.obtenerTodos()) {
                        System.out.println("ID: " + u.getId() + " | " + u.getApellido() + ", " + u.getNombre() + " | Mail: " + u.getEmail() + " | Rol: " + u.getRol());
                    }
                } else if (op == 2) {
                    Usuario u = new Usuario();
                    System.out.print("Nombre: "); u.setNombre(teclado.next());
                    System.out.print("Apellido: "); u.setApellido(teclado.next());
                    System.out.print("Email: "); u.setEmail(teclado.next());
                    System.out.print("Celular: "); u.setCelular(teclado.next());
                    System.out.print("Contraseña: "); u.setContraseña(teclado.next());
                    u.setRol(Rol.USUARIO); 
                    
                    usuarioService.registrarUsuario(u);
                    System.out.println("Usuario registrado con exito");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (op != 3);
    }

    // ================= INTERFAZ CRÍTICA DE VENTAS (CREAR PEDIDO) =================
    private static void interfazVentas() {
        try {
            System.out.println("\n--- APERTURA DE NUEVA VENTA ---");
            System.out.print("Ingrese el ID del Usuario (Cliente) que compra: ");
            Long usrId = Long.parseLong(teclado.next());
            Usuario comprador = usuarioService.buscarPorId(usrId);

            Pedido nuevoPedido = new Pedido();
            nuevoPedido.setUsuario(comprador);
            nuevoPedido.setEstado(Estado.PENDIENTE);
            
            System.out.println("Seleccione Forma de Pago (1. EFECTIVO, 2. TARJETA, 3. TRANSFERENCIA): ");
            int fp = Integer.parseInt(teclado.next());
            nuevoPedido.setFormaPago(fp == 2 ? FormaPago.TARJETA : fp == 3 ? FormaPago.TRANFERENCIA : FormaPago.EFECTIVO);

            String agregarMas;
            do {
                System.out.println("\n--- AGREGAR ITEM AL PEDIDO ---");
                System.out.print("Ingrese el ID del Producto: ");
                Long prodId = Long.parseLong(teclado.next());
                Producto prod = productoService.buscarPorId(prodId);

                System.out.print("Cantidad solicitada: ");
                int cant = Integer.parseInt(teclado.next());

                
                nuevoPedido.addDetallePedido(cant, (prod.getPrecio() * cant), prod); 

                System.out.print("Desea agregar otro producto al pedido? (S/N): ");
                agregarMas = teclado.next();
            } while (agregarMas.equalsIgnoreCase("S"));

            System.out.println("\nTOTAL NETO CALCULADO: $" + nuevoPedido.getTotal());
            System.out.print("Confirmar y procesar el pedido? (S/N): ");
            if (teclado.next().equalsIgnoreCase("S")) {
                pedidoService.registrarPedido(nuevoPedido);
                System.out.println("Pedido creado y procesado con exito en el sistema");
            } else {
                System.out.println("Pedido cancelado");
            }

        } catch (EntityNotFoundException | StockInvalidoException e) {
            System.out.println("ALERTA DE NEGOCIO: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR" + e.getMessage());
        }
    }

    // ================ HISTORIAL DE PEDIDOS ================
    private static void listarHistorialPedidos() {
        System.out.println("\n--- HISTORIAL DE PEDIDOS REGISTRADOS ---");
        try {
            for (Pedido p : pedidoService.obtenerTodos()) {
                System.out.println("\n==================================================");
                System.out.println("PEDIDO ID: " + p.getId() + " | Fecha: " + p.getFecha() + " | Estado: " + p.getEstado());
                System.out.println("Cliente: " + p.getUsuario().getApellido() + ", " + p.getUsuario().getNombre());
                System.out.println("Forma de Pago: " + p.getFormaPago() + " | MONTO TOTAL: $" + p.getTotal());
                System.out.println("---------------- DETALLES DEL PEDIDO --------------");
              
                for (DetallePedido d : p.getDetallePedidos()) {
                    System.out.println(d.getProducto().getNombre() + " x" + d.getCantidad() + " | Subtotal: $" + d.getSubTotal());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al leer el historial: " + e.getMessage());
        }
    }
}
