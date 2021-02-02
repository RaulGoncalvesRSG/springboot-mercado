package curso.springboot.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import curso.springboot.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	default Page<Produto> findProdudoByNomePage(String nome, Pageable pageable){
		Produto produto = new Produto();
		produto.setNome(nome);
	
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Produto> example = Example.of(produto, exampleMatcher);
		
		return findAll(example, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("nome")));
	}
}
