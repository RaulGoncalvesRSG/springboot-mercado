package curso.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import curso.springboot.model.Categoria;

public interface CategoriaRepository extends CrudRepository<Categoria, Long>{

	@Query("select c from Categoria c order by c.nome")
	List<Categoria> findAllOrdenarPorNome();
}
