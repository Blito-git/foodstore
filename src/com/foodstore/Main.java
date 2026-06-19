package com.foodstore;

import com.foodstore.ui.MenuPrincipal;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        new MenuPrincipal(scanner).mostrar();
        scanner.close();
    }
}
//