package com.thiagov2a.desafio.main;

import com.thiagov2a.desafio.model.Datos;
import com.thiagov2a.desafio.model.DatosLibro;
import com.thiagov2a.desafio.service.ConsumoAPI;
import com.thiagov2a.desafio.service.ConvierteDatos;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private final Scanner input = new Scanner(System.in);
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConvierteDatos convierteDatos = new ConvierteDatos();
    private static final String URL_BASE = "https://gutendex.com/books/";

    public void menu() {
        var json = consumoAPI.obtenerDatos(URL_BASE);
        var libros = convierteDatos.obtenerDatos(json, Datos.class);

        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            var opcion = obtenerOpcion();

            switch (opcion) {
                case 1:
                    mostrarLibrosMasDescargados(libros);
                    break;
                case 2:
                    buscarLibroPorTitulo();
                    break;
                case 3:
                    mostrarEstadisticasDeDescargas(libros);
                    break;
                case 4:
                    salir = true;
                    System.out.println("Gracias por usar el catálogo. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
                    break;
            }
        }
        input.close();
    }

    private void mostrarMenu() {
        System.out.println("\n--- Bienvenido al catálogo de libros ---");
        System.out.println("1. Ver libros más descargados");
        System.out.println("2. Buscar libro por título");
        System.out.println("3. Datos de descargas");
        System.out.println("4. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private int obtenerOpcion() {
        while (true) {
            try {
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada no válida. Por favor, ingrese una opción disponible: ");
            }
        }
    }

    private String obtenerTitulo() {
        return input.nextLine().trim();
    }

    private void mostrarLibrosMasDescargados(Datos libros) {
        System.out.println("\n--- Libros más descargados ---");
        final int[] index = {1};
        libros.resultados().stream()
                .sorted(Comparator.comparing(DatosLibro::numeroDeDescargas).reversed())
                .limit(10)
                .map(l -> index[0]++ + ". Título: " + l.titulo() +
                          " | Autor: " + l.autores().get(0).nombre() +
                          " | Número de descargas: " + l.numeroDeDescargas())
                .forEach(System.out::println);
    }

    private void buscarLibroPorTitulo() {
        System.out.print("\nIngrese el título del libro: ");
        var tituloBusqueda = obtenerTitulo();

        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloBusqueda.replace(" ", "+"));
        var librosBusqueda = convierteDatos.obtenerDatos(json, Datos.class);

        Optional<DatosLibro> libroEncontrado = librosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toLowerCase().contains(tituloBusqueda.toLowerCase()))
                .findFirst();

        if (libroEncontrado.isPresent()) {
            var libro = libroEncontrado.get();
            System.out.println("\n--- Libro encontrado ---");
            System.out.println("Título: " + libro.titulo() +
                               " | Autor: " + libro.autores().get(0).nombre() +
                               " | Número de descargas: " + libro.numeroDeDescargas());
        } else {
            System.out.println("\nLibro no encontrado");
        }
    }

    private void mostrarEstadisticasDeDescargas(Datos libros) {
        DoubleSummaryStatistics est = libros.resultados().stream()
                .filter(d -> d.numeroDeDescargas() > 0)
                .collect(Collectors.summarizingDouble(DatosLibro::numeroDeDescargas));
        System.out.println("\n--- Estadísticas de descargas ---");
        System.out.println("Mínimo: " + est.getMin());
        System.out.println("Máximo: " + est.getMax());
        System.out.println("Promedio: " + est.getAverage());
        System.out.println("Suma total: " + est.getSum());
    }
}
