package com.thiagov2a.screenmatch.main;

import com.thiagov2a.screenmatch.model.*;
import com.thiagov2a.screenmatch.repository.SerieRepository;
import com.thiagov2a.screenmatch.service.ConsumoAPI;
import com.thiagov2a.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "http://www.omdbapi.com/?t=";
    private static final String URL_TIPO = "&Type=series";
    private static final String API_KEY = "&apikey=" + System.getenv("OMDB_APIKEY");

    private final Scanner input = new Scanner(System.in);
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConvierteDatos convierteDatos = new ConvierteDatos();
    // private final List<DatosSerie> datosSeries = new ArrayList<>();
    private final SerieRepository serieRepository;
    // private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void menu() {
        boolean salir = false;

        while (!salir) {
            // Cargar series del repositorio al local
            // this.series = serieRepository.findAll();

            mostrarMenuPrincipal();
            var opcion = obtenerOpcion();

            switch (opcion) {
                case 1:
                    buscarSerie();
                    break;
                case 2:
                    buscarEpisodios();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    mostrarTop5Series();
                    break;
                case 5:
                    buscarSeriesPorCategoria();
                    break;
                case 6:
                    filtrarSeriesPorTemporadas();
                    break;
                case 7:
                    filtrarSeriesPorEvaluacion();
                    break;
                case 0:
                    salir = true;
                    System.out.println("Gracias por usar ScreenMatch. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
                    break;
            }
        }
        input.close();
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n=== ScreenMatch: Catálogo de Series ===");
        System.out.println("1. Buscar series");
        System.out.println("2. Buscar episodios");
        System.out.println("3. Mostrar series buscadas");
        System.out.println("4. Mostrar las 5 mejores series");
        System.out.println("5. Buscar series por categoría");
        System.out.println("6. Filtrar series por temporadas");
        System.out.println("7. Filtrar series por evaluacion");
        System.out.println("0. Salir");
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

    private String obtenerNombre() {
        return input.nextLine().trim();
    }

    // TODO: Guardar datos de Episodio para cada Serie encontrada (tomar como base el método 'buscarEpisodios')
    // TODO: Hacer que el 'buscarDatosSerie' encuentre mas de una Serie para darle la opción al usuario de elegir la que buscaba
    private void buscarSerie() {
        System.out.print("\nIngrese el nombre de la serie: ");
        String nombreSerie = obtenerNombre();

        var datosSerie = buscarDatosSerie(nombreSerie);

        if (datosSerie == null) {
            System.out.println("No se encontró la serie. Por favor, intente de nuevo.");
        } else {
            // Verificar si la serie ya existe en el repositorio
            Optional<Serie> serieExistente = serieRepository.findByTituloIgnoreCase(datosSerie.titulo());

            if (serieExistente.isPresent()) {
                System.out.println("La serie ya existe en el repositorio: " + datosSerie.titulo());
            } else {
                var serie = new Serie(datosSerie);
                serieRepository.save(serie);
                System.out.println("Serie encontrada y guardada: " + datosSerie.titulo());
            }
        }
    }

    private DatosSerie buscarDatosSerie(String nombreSerie) {
        String json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + URL_TIPO + API_KEY);
        var datosSerie = convierteDatos.obtenerDatos(json, DatosSerie.class);
        if (datosSerie.respuesta().equalsIgnoreCase("False")) {
            return null;
        }
        return datosSerie;
    }

    private void buscarEpisodios() {
        System.out.print("\nIngrese el nombre de la serie para buscar episodios: ");
        String nombreSerie = obtenerNombre();

        List<Serie> seriesEncontradas = serieRepository.findByTituloContainsIgnoreCase(nombreSerie);

        if (seriesEncontradas.isEmpty()) {
            System.out.println("Serie no encontrada en la lista. Por favor, verifique el nombre o busque la serie primero.");
            return;
        }

        // Si se encontraron múltiples series, permitir al usuario seleccionar una
        Serie serie = null;
        if (seriesEncontradas.size() > 1) {
            System.out.println("Se encontraron múltiples series. Por favor, seleccione una:");
            for (int i = 0; i < seriesEncontradas.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, seriesEncontradas.get(i).getTitulo());
            }
            int opcion = 0;
            boolean opcionValida = false;
            while (!opcionValida) {
                System.out.print("Seleccione el número de la serie: ");
                opcion = obtenerOpcion();
                if (opcion >= 1 && opcion <= seriesEncontradas.size()) {
                    opcionValida = true;
                } else {
                    System.out.println("Opción no válida. Por favor, seleccione un número entre 1 y " + seriesEncontradas.size());
                }
            }
            serie = seriesEncontradas.get(opcion - 1);
        } else {
            serie = seriesEncontradas.get(0);
        }

        List<Episodio> episodios = obtenerEpisodios(serie);

        if (episodios.isEmpty()) {
            System.out.println("No se encontraron episodios para la serie.");
            return;
        }

        if (serie.getEpisodios().isEmpty()) {
            serie.setEpisodios(episodios);
            serieRepository.save(serie);
        }

        mostrarMenuEpisodios(episodios, serie.getTitulo());
    }

    private List<Episodio> obtenerEpisodios(Serie serie) {
        List<DatosTemporada> temporadas = new ArrayList<>();

        if (serie.getTotalDeTemporadas() >= 1) {
            for (int i = 1; i <= serie.getTotalDeTemporadas(); i++) {
                String json = consumoAPI.obtenerDatos(URL_BASE + serie.getTitulo().replace(" ", "+") + "&Season=" + i + API_KEY);
                var datosTemporada = convierteDatos.obtenerDatos(json, DatosTemporada.class);
                temporadas.add(datosTemporada);
            }

            return temporadas.stream()
                    .flatMap(t -> t.episodios().stream()
                            .map(d -> new Episodio(t.numero(), d)))
                    .toList();
        }
        return new ArrayList<>();
    }

    private void mostrarMenuEpisodios(List<Episodio> episodios, String nombreSerie) {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- Menú de Episodios: " + nombreSerie + " ---");
            System.out.println("1. Mostrar los 5 mejores episodios");
            System.out.println("2. Buscar episodio por título");
            System.out.println("3. Datos de evaluaciones");
            System.out.println("0. Regresar al menú principal");
            System.out.print("Seleccione una opción: ");
            var opcion = obtenerOpcion();

            switch (opcion) {
                case 1:
                    mostrarTopEpisodios(episodios);
                    break;
                case 2:
                    buscarEpisodioPorTitulo(episodios);
                    break;
                case 3:
                    mostrarEstadisticasDeEvaluaciones(episodios);
                    break;
                case 0:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
                    break;
            }
        }
    }

    private void mostrarTopEpisodios(List<Episodio> episodios) {
        System.out.println("\n--- Top 5 Episodios ---");

        List<Episodio> top5Episodios = serieRepository.buscarTop5Episodios();
        if (top5Episodios.isEmpty()) {
            System.out.println("No se encontraron episodios mejor evaluados.");
            return;
        }

        top5Episodios.forEach(e -> System.out.println("Temporada: " + e.getTemporada() +
                                                      " | Episodio: " + e.getNumeroDeEpisodio() +
                                                      " | Título: " + e.getTitulo() +
                                                      " | Evaluación: " + e.getEvaluacion()));
    }

    private void buscarEpisodioPorTitulo(List<Episodio> episodios) {
        System.out.print("\nIngrese el título parcial del episodio: ");
        String tituloParcial = obtenerNombre();

        List<Episodio> episodiosEncontrados = serieRepository.buscarEpisodiosPorNombre(tituloParcial);

        if (episodiosEncontrados.isEmpty()) {
            System.out.println("No se encontró ningún episodio con el título proporcionado.");
        } else {
            System.out.println("\n--- Episodios Encontrados ---");
            episodiosEncontrados.stream()
                    .filter(e -> e.getEvaluacion() > 0.0)
                    .sorted(Comparator.comparing(Episodio::getEvaluacion).reversed())
                    .limit(10)
                    .forEach(e -> System.out.println("Temporada: " + e.getTemporada() +
                                                     " | Episodio: " + e.getNumeroDeEpisodio() +
                                                     " | Título: " + e.getTitulo() +
                                                     " | Evaluación: " + e.getEvaluacion()));
        }
    }

    private void mostrarEstadisticasDeEvaluaciones(List<Episodio> episodios) {
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("\n--- Estadísticas de Evaluaciones ---");
        System.out.println("Media: " + est.getAverage());
        System.out.println("Mejor episodio: " + est.getMax());
        System.out.println("Peor episodio: " + est.getMin());
        System.out.println("Cantidad: " + est.getCount());
    }

    private void mostrarSeriesBuscadas() {
        List<Serie> series = serieRepository.findAll();
        if (series.isEmpty()) {
            System.out.println("No se han buscado series aún.");
        } else {
            System.out.println("\n--- Series Buscadas ---");
            series.stream()
                    .sorted(Comparator.comparing(Serie::getTitulo).reversed())
                    .forEach(System.out::println);
        }
    }

    private void mostrarTop5Series() {
        List<Serie> top5Series = serieRepository.findTop5ByOrderByEvaluacionDesc();
        if (top5Series.isEmpty()) {
            System.out.println("No se han encontrado series.");
        } else {
            System.out.println("\n--- Top 5 Series ---");
            top5Series.forEach(System.out::println);
        }
    }

    private void buscarSeriesPorCategoria() {
        String categoria;
        Categoria categoriaEnum;

        while (true) {
            System.out.print("\nIngrese la categoría de las series (o escriba 'salir' para terminar): ");
            categoria = obtenerNombre();

            // Opción para salir del bucle
            if (categoria.equalsIgnoreCase("salir")) {
                System.out.println("Saliendo del menú de búsqueda por categoría.");
                return; // Termina el método y regresa al menú principal
            }

            try {
                // Intentar convertir el texto de la categoría a un enum
                categoriaEnum = Categoria.fromEspanol(categoria);
                break; // Sale del bucle si la conversión es exitosa
            } catch (IllegalArgumentException e) {
                // Manejar la excepción si la categoría no es válida
                System.out.println("Categoría no válida. Por favor, intente de nuevo.");
            }
        }

        // Buscar series por categoría
        List<Serie> seriesEncontradas = serieRepository.findByCategoria(categoriaEnum);

        if (seriesEncontradas.isEmpty()) {
            System.out.println("No se encontraron series para la categoría " + categoriaEnum.getNombreEspanol() + ".");
        } else {
            System.out.println("\n--- Series de " + categoriaEnum.getNombreEspanol() + " Encontradas ---");
            seriesEncontradas.forEach(System.out::println);
        }
    }

    private void filtrarSeriesPorTemporadas() {
        System.out.print("\nIngrese el número de temporadas para filtrar: ");
        int numeroDeTemporadas = obtenerOpcion();

        int tipoFiltro;
        List<Serie> seriesFiltradas;

        while (true) {
            System.out.println("Seleccione el tipo de filtro:");
            System.out.println("1. Menor o igual");
            System.out.println("2. Mayor o igual");
            System.out.print("Seleccione una opción: ");
            tipoFiltro = obtenerOpcion();

            switch (tipoFiltro) {
                case 1:
                    seriesFiltradas = serieRepository.buscarPorTemporadaMenorIgual(numeroDeTemporadas);
                    System.out.println("\n--- Series con " + numeroDeTemporadas + " temporadas o menos ---");
                    break;
                case 2:
                    seriesFiltradas = serieRepository.buscarPorTemporadaMayorIgual(numeroDeTemporadas);
                    System.out.println("\n--- Series con " + numeroDeTemporadas + " temporadas o más ---");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
                    continue;
            }
            break;
        }

        if (seriesFiltradas.isEmpty()) {
            System.out.println("No se encontraron series con el número de temporadas especificado.");
        } else {
            seriesFiltradas.forEach(System.out::println);
        }
    }

    private void filtrarSeriesPorEvaluacion() {
        System.out.print("\nIngrese la evaluación para filtrar: ");
        double evaluacion = obtenerEvaluacion();

        int tipoFiltro;
        List<Serie> seriesFiltradas;

        while (true) {
            System.out.println("Seleccione el tipo de filtro:");
            System.out.println("1. Menor o igual");
            System.out.println("2. Mayor o igual");
            System.out.print("Seleccione una opción: ");
            tipoFiltro = obtenerOpcion();

            switch (tipoFiltro) {
                case 1:
                    seriesFiltradas = serieRepository.buscarPorEvaluacionMenorIgual(evaluacion);
                    System.out.println("\n--- Series con evaluación menor o igual a " + evaluacion + " ---");
                    break;
                case 2:
                    seriesFiltradas = serieRepository.buscarPorEvaluacionMayorIgual(evaluacion);
                    System.out.println("\n--- Series con evaluación mayor o igual a " + evaluacion + " ---");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
                    continue;
            }
            break;
        }

        if (seriesFiltradas.isEmpty()) {
            System.out.println("No se encontraron series con la evaluación especificada.");
        } else {
            seriesFiltradas.forEach(System.out::println);
        }
    }

    private double obtenerEvaluacion() {
        while (true) {
            try {
                // Leer la entrada del usuario
                String entrada = input.nextLine();

                // Reemplazar la coma por punto
                entrada = entrada.replace(',', '.');

                // Intentar parsear el double
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Entrada no válida. Por favor, ingrese una evaluación disponible: ");
            }
        }
    }
}
