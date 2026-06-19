package com.foodstore.service;

import com.foodstore.dao.CategoriaDAO;
import com.foodstore.dao.IBaseDAO;
import com.foodstore.model.Categoria;

public class CategoriaService extends GenericService<Categoria> {
    private final CategoriaDAO dao = new CategoriaDAO();

    @Override
    protected IBaseDAO<Categoria> getDAO() { return dao; }
}