package com.thiagov2a.screenmatch.model;

import java.text.Normalizer;
import java.util.regex.Pattern;

public enum Categoria {
    ACCION("Action", "Acción"),
    AVENTURA("Adventure", "Aventura"),
    ANIMACION("Animation", "Animación"),
    BIOGRAFICO("Biographical", "Biográfico"),
    COMEDIA("Comedy", "Comedia"),
    CRIMEN("Crime", "Crimen"),
    DOCUMENTAL("Documentary", "Documental"),
    DRAMA("Drama", "Drama"),
    FANTASIA("Fantasy", "Fantasía"),
    FAMILIAR("Family", "Familiar"),
    GUERRA("War", "Guerra"),
    HISTORIA("History", "Historia"),
    HORROR("Horror", "Horror"),
    MUSICAL("Musical", "Musical"),
    MISTERIO("Mystery", "Misterio"),
    ROMANCE("Romance", "Romance"),
    CIENCIA_FICCION("Sci-Fi", "Ciencia Ficción"),
    SUSPENSE("Suspense", "Suspense"),
    THRILLER("Thriller", "Thriller"),
    WESTERN("Western", "Western"),
    OTRO("N/A", "Otro");

    private String nombreOmdb;
    private String nombreEspanol;

    Categoria(String nombreOmdb, String nombreEspanol) {
        this.nombreOmdb = nombreOmdb;
        this.nombreEspanol = nombreEspanol;
    }

    public String getNombreOmdb() {
        return nombreOmdb;
    }

    public String getNombreEspanol() {
        return nombreEspanol;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.nombreOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("No se encontró la categoría: " + text);
    }

    public static Categoria fromEspanol(String text) {
        String normalizedText = normalize(text);
        for (Categoria categoria : Categoria.values()) {
            if (normalize(categoria.nombreEspanol).contains(normalizedText)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("No se encontró la categoría: " + text);
    }

    private static String normalize(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase();
    }
}
