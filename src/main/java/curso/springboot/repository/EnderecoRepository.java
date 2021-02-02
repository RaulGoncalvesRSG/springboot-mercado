package curso.springboot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Endereco;

@Transactional
@Repository
public interface EnderecoRepository extends CrudRepository<Endereco, Integer> {

}
