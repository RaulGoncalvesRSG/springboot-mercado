package curso.springboot.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Pessoa;

@Repository
@Transactional			
//Transactional: Spring controla toda a parte de persistência, o momento de abrir e fechar as transações
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {
	//JpaRepository estende CrudRepository. Ele adiciona mais funcionalidades na interface
	
	@Query("select p from Pessoa p where p.nome like %?1%")
	List<Pessoa> findPessoaByNome(String nome);
	
	@Query("select p from Pessoa p where p.sexo = ?1")
	List<Pessoa> findPessoaBySexo(String sexo);
	
	@Query("select p from Pessoa p where p.nome like %?1% and p.sexo = ?2")
	List<Pessoa> findPessoaByNomeSexo(String nome, String sexo);
	
	//default - criando um método dentro da interface
	//Este método é usado com JpaRepository
	default Page<Pessoa> findPessoaByNomePage(String nome, Pageable pageable) {
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		
		/*Essa paginação só funciona se trabalhar com as próprias classes do spring
		Configura a pesquisa para consultar por partes do nome no BD igual o like com SQL. Quando trabalha com
		paginações, precissa das classes spring*/
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		//Une o obj com valor e a configuração para consultar
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
		
		return findAll(example, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("nome")));
	}
	
	default Page<Pessoa> findPessoaByNomeSexoPage(String nome, String sexo, Pageable pageable) {
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		pessoa.setSexo(sexo);
		
		/*matchingAll - condição de "E" na pesquisa. Ex: o nome pesquisado tem o sexo que foi especificado
		matchingAny - condição de "OU" na pesquisa. Ex: se tiver o nome ou o sexo */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("sexo", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
		
		return findAll(example, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("nome")));
	}
}
