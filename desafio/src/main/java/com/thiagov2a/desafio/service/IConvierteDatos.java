package com.thiagov2a.desafio.service;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);

}
