package com.foodstore.ui;

import java.util.Scanner;

public class Consola {
    private final Scanner scanner;

    public Consola(Scanner scanner) { this.scanner = scanner; }

    public String leerTexto(String prompt) {
        String valor;
        do {
            System.out.print(prompt);
            valor = scanner.nextLine().trim();
            if (valor.isEmpty()) System.out.println("  El campo no puede estar vacío.");
        } while (valor.isEmpty());
        return valor;
    }

    public String leerTextoOpcional(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Ingresá un número entero válido.");
            }
        }
    }

    public long leerLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Ingresá un ID numérico válido.");
            }
        }
    }

    public double leerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double v = Double.parseDouble(scanner.nextLine().trim());
                if (v <= 0) { System.out.println("  Debe ser mayor a 0."); continue; }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("  Ingresá un valor decimal correcto.");
            }
        }
    }

    public <T extends Enum<T>> T leerEnum(String prompt, Class<T> tipo) {
        T[] valores = tipo.getEnumConstants();
        while (true) {
            System.out.print(prompt + " (");
            for (int i = 0; i < valores.length; i++) {
                System.out.print((i + 1) + "=" + valores[i].name() + (i < valores.length - 1 ? ", " : ""));
            }
            System.out.print("): ");
            try {
                int op = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (op >= 0 && op < valores.length) return valores[op];
                System.out.println("  Opción fuera de rango.");
            } catch (NumberFormatException e) {
                System.out.println("  Seleccioná un número de la lista.");
            }
        }
    }
}