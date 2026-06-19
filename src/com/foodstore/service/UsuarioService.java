package com.foodstore.service;

import com.foodstore.dao.IBaseDAO;
import com.foodstore.dao.UsuarioDAO;
import com.foodstore.model.Usuario;

public class UsuarioService extends GenericService<Usuario> {
    private final UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected IBaseDAO<Usuario> getDAO() { return dao; }
}