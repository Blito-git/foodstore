package com.foodstore.dao;

import java.util.List;

public interface IBaseDAO<T> {
    void crear(T entidad);
    T buscarPorId(Long id);
    List<T> listarActivos();
    void actualizar(T entidad);
    void eliminar(Long id);
}