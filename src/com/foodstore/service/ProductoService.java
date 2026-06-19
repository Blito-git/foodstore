package com.foodstore.service;

import com.foodstore.dao.IBaseDAO;
import com.foodstore.dao.ProductoDAO;
import com.foodstore.model.Producto;

public class ProductoService extends GenericService<Producto> {
    private final ProductoDAO dao = new ProductoDAO();

    @Override
    protected IBaseDAO<Producto> getDAO() { return dao; }
}