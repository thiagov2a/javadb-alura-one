package com.thiagov2a.screenmatch.service;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);

}
