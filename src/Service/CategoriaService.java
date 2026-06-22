/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import Exception.EntityNotFoundException;
import Entities.Categoria;
import Dao.CategoriaDAO;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author ignac
 */
public class CategoriaService {
    private final CategoriaDAO categoriaDAO;
    
    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAO();
    }
    public void registrarCategoria(Categoria categoria) throws SQLException {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoria no puede estar vacio");
        }
        categoriaDAO.guardar(categoria);
    }

    public void modificarCategoria(Categoria categoria) throws SQLException, EntityNotFoundException {
        buscarPorId(categoria.getId());
        categoriaDAO.actualizar(categoria);
    }

    public void darDeBajaCategoria(Long id) throws SQLException, EntityNotFoundException {
        buscarPorId(id);
        categoriaDAO.eliminarLogico(id);
    }

    public Categoria buscarPorId(Long id) throws SQLException, EntityNotFoundException {
        Categoria categoria = categoriaDAO.buscarPorId(id);
        if (categoria == null) {
            throw new EntityNotFoundException("No se encontró ninguna categoría activa con el ID: " + id);
        }
        return categoria;
    }

    public List<Categoria> obtenerTodas() throws SQLException {
        return categoriaDAO.listarTodos();
    }
}
