package curso.springboot.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Produto;
import curso.springboot.repository.CategoriaRepository;
import curso.springboot.repository.ProdutoRepository;

@Controller
public class ProdutoController {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping(value = "/produto")
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("produto");
		modelAndView.addObject("produtos", produtoRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		
		return modelAndView;
	}
	
	@GetMapping(value = "/produtosPaginacao")
	public ModelAndView carregarProdutosPorPaginacao(@PageableDefault(size = 5) Pageable pageable,
			ModelAndView modelAndView, @RequestParam("nomePesquisa") String nomePesquisa) {
		
		Page<Produto> pageProduto = produtoRepository.findProdudoByNomePage(nomePesquisa, pageable);
		modelAndView.addObject("produtos", pageProduto);
		modelAndView.addObject("nomePesquisa", nomePesquisa);
		modelAndView.setViewName("/produto");
		
		return modelAndView;
	}
	
	@GetMapping("/cadastroProduto")
	public ModelAndView cadastroProduto() {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroProduto");
		
		modelAndView.addObject("produtoObj", new Produto());
		modelAndView.addObject("categorias", categoriaRepository.findAllOrdenarPorNome());
		
		return modelAndView;
	}
	
	@PostMapping(value = "**/salvarProduto", consumes = {"multipart/form-data"})
	public ModelAndView salvar(Produto produto, final MultipartFile file) {
		
		if (file.getSize() > 0) {
			try {
				
				String tipo = file.getContentType();
				tipo = tipo.split("/")[1];				//Pega a extensão do arquivo
				
				//Verifica se o arquivo é uma imagem
				if (tipo.equals("jpeg") || tipo.equals("png") || tipo.equals("jpg")) {
					produto.setImagem(file.getBytes());
					produto.setNomeFileImagem(file.getOriginalFilename());
					produto.setTipoFileImagem(file.getContentType());
				}
				else {				//Retorna para a tela para remover o arquivo que não é imagem
					ModelAndView modelAndView = new ModelAndView("cadastro/cadastroProduto");
					modelAndView.addObject("produtoObj", produto);
					modelAndView.addObject("categorias", categoriaRepository.findAllOrdenarPorNome());
					
					//Mostrar msg de erro na tela
					
					return modelAndView;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			if (produto.getId() != null && produto.getId() > 0) {
				Produto produtoTemp = produtoRepository.findById(produto.getId()).get();
				produto.setImagem(produtoTemp.getImagem());
				produto.setNomeFileImagem(produtoTemp.getNomeFileImagem());
				produto.setTipoFileImagem(produtoTemp.getTipoFileImagem());
			}
		}
		
		produtoRepository.save(produto);
		
		ModelAndView modelAndView = new ModelAndView("produto");
		modelAndView.addObject("produtos", produtoRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		
		return modelAndView;
	}
	
	@GetMapping("/editarProduto/{idProduto}")
	public ModelAndView editar(@PathVariable("idProduto") Long idProduto) {
		Produto produto = produtoRepository.findById(idProduto).get();
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroProduto");
		modelAndView.addObject("produtoObj", produto);
		modelAndView.addObject("categorias", categoriaRepository.findAllOrdenarPorNome());
		
		return modelAndView;
	}
	
	@GetMapping("/removerProduto/{idProduto}")
	public ModelAndView excluir(@PathVariable("idProduto") Long idProduto) {
		produtoRepository.deleteById(idProduto);
		
		ModelAndView modelAndView = new ModelAndView("produto");
		modelAndView.addObject("produtos", produtoRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		
		return modelAndView;
	}
}
