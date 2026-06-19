package com.foodstore.service;

import com.foodstore.dao.IBaseDAO;
import com.foodstore.exception.EntityNotFoundException;
import java.util.List;

public abstract class GenericService<T> {
    protected abstract IBaseDAO<T> getDAO();

    public void crear(T entidad) { getDAO().crear(entidad); }
    public List<T> listarActivos() { return getDAO().listarActivos(); }
    public void actualizar(T entidad) { 
        getDAO().actualizar(entidad); 
    }
    public void eliminar(Long id) { 
        buscarPorId(id);
        getDAO().eliminar(id); 
    }
    public T buscarPorId(Long id) {
        T entidad = getDAO().buscarPorId(id);
        if (entidad == null) throw new EntityNotFoundException("No se encontró la entidad con el ID: " + id);
        return entidad;
    }
}