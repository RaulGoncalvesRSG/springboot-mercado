package curso.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Endereco;
import curso.springboot.model.Pessoa;
import curso.springboot.repository.EnderecoRepository;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.ProfissaoRepository;
import curso.springboot.util.ReportUtil;

@Controller
public class PesssoaController {
	 
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ProfissaoRepository profissaoRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired	
	private ReportUtil<Pessoa> reportUtil;
	
	@GetMapping("/menu")
	public ModelAndView menu() {
		ModelAndView modelAndView = new ModelAndView("menu");
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/cadastroPessoa")
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroPessoa");
		modelAndView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of
				(0, 5, Sort.by(Order.asc("nome")))));
		modelAndView.addObject("profissoes", profissaoRepository.findAll());	//Joga para o ${profissoes}
		
		//Passa o obj vazio para iniciar a tela, pois o th:object espera um obj no formulário
		return modelAndView;
	}
	
	@GetMapping("/pessoasPag")
	public ModelAndView carregarPessoasPorPaginacao(@PageableDefault(size = 5) Pageable pageable, 
				ModelAndView modelAndView, @RequestParam("nomePesquisa") String nomePesquisa) {
		//pageable reconhece para qual pág ele precisa ir. A quanditdade padrão de elementos foi definida em 5
		
		Page<Pessoa> pagePessoa = pessoaRepository.findPessoaByNomePage(nomePesquisa, pageable);
		modelAndView.addObject("pessoas", pagePessoa);
		modelAndView.addObject("nomePesquisa", nomePesquisa);
		modelAndView.addObject("profissoes", profissaoRepository.findAll());
		modelAndView.setViewName("cadastro/cadastroPessoa");
		
		return modelAndView;
	}
	
	@GetMapping("/pessoa")
	public ModelAndView cadastroPessoa() {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/pessoa");
		modelAndView.addObject("pessoa", new Pessoa());	
		modelAndView.addObject("endereco", new Endereco());	
		modelAndView.addObject("profissoes", profissaoRepository.findAll());	//Joga para o ${profissoes}
		
		return modelAndView;
	}
	
	/* ** ignora tudo que tiver antes de "/salvarPessoa", intercepta o /salvarPessoa de qualquer forma
	Valid - para fazer as validações dos campos. BindingResult - emite as msgs na tela
	
	consumes = {"multipart/form-data"} - Para trabalhar com formulário de upload
	Para isso, utiliza o tipo: final MultipartFile file*/
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarPessoa", consumes = {"multipart/form-data"})
	private ModelAndView salvar(@Valid Pessoa pessoa, @Valid Endereco endereco, @RequestParam("enderecoId") 
			Integer enderecoId, BindingResult bindingResult, final MultipartFile file) {
		
		//Se tiver algum erro
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("cadastro/pessoa");
			modelAndView.addObject("pessoaObj", pessoa);			//Retorna o objeto que começou a ser cadastrado
			
			List<String> msg = new ArrayList<String>();
			
			//Pega todos os erros encontrados
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage());			//getDefaultMessage() vem das anotações @NotEmpty
			}
			
			modelAndView.addObject("msg", msg);						//Retorna a lista de erros para a view
			return modelAndView;
		}
		
		if (file.getSize() > 0) {			//Se tiver adicionando um novo arquivo
			try {
				pessoa.setCurriculo(file.getBytes());
				pessoa.setNomeFileCurriculo(file.getOriginalFilename());
				pessoa.setTipoFileCurriculo(file.getContentType());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			if (pessoa.getId() != null && pessoa.getId() > 0) {			//Está editando a Pessoa
				Pessoa pessoaTemp = pessoaRepository.findById(pessoa.getId()).get();
				pessoa.setCurriculo(pessoaTemp.getCurriculo());
				pessoa.setTipoFileCurriculo(pessoaTemp.getTipoFileCurriculo());
				pessoa.setNomeFileCurriculo(pessoaTemp.getNomeFileCurriculo());
			}
		}
		
		if (enderecoId != null) {
			endereco.setId(enderecoId);
			enderecoRepository.save(endereco);
			pessoa.setEndereco(endereco);
		}
		else {							//Está cadastrando um novo usuário e um novo endereço
			Endereco enderecoPessoa = enderecoRepository.save(endereco);	//enderecoPessoa recupera o ID
			pessoa.setEndereco(enderecoPessoa);
		}
		
		pessoaRepository.save(pessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroPessoa");
		modelAndView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		modelAndView.addObject("profissoes", profissaoRepository.findAll());
		
		return modelAndView;
	}
	
	/*Quando retorna um objeto para uma URL, o método precisa do obj ModelAndView
	PathVariable - pega a variável que está editando através do parâmetro. Coloca o tipo da variável que está recebendo 
	GetMapping - substitui toda a parte "RequestMapping(method = RequestMethod.GET"*/
	@GetMapping("/editarPessoa/{idPessoa}")
	public ModelAndView editar(@PathVariable("idPessoa") Integer idPessoa) {
		Pessoa pessoa = pessoaRepository.findById(idPessoa).get();
		//Quando clica em editar, retorna para a mesma tela
		ModelAndView modelAndView = new ModelAndView("cadastro/pessoa");
		modelAndView.addObject("pessoaObj", pessoa);			//Injeta no th:object
		modelAndView.addObject("pessoa", pessoa);
		
		if (pessoa.getEndereco() != null) {
			modelAndView.addObject("endereco", pessoa.getEndereco());	
		}
		else {
			modelAndView.addObject("endereco", new Endereco());	
		}
		
		modelAndView.addObject("profissoes", profissaoRepository.findAll());
		//O pessoas.content da view exige o PageRequest
		return modelAndView;
	}
	
	@GetMapping("/removerPessoa/{idPessoa}")
	public ModelAndView excluir(@PathVariable("idPessoa") Integer idPessoa) {
		pessoaRepository.deleteById(idPessoa);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroPessoa");
		modelAndView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		
		return modelAndView;
	}
	
	@GetMapping("/downloadCurriculo/{idPessoa}")
	public void downloadCurriculo(@PathVariable("idPessoa") Integer idPessoa, HttpServletResponse response) {
		Pessoa pessoa = pessoaRepository.findById(idPessoa).get();
		
		if (pessoa.getCurriculo() != null) {
			response.setContentLength(pessoa.getCurriculo().length);	//Seta o tamanho da resposta
			//O tipo também pode ser genérico passando como parâmetro: "application/octet-stream"
			response.setContentType(pessoa.getTipoFileCurriculo());		//Tipo do arquivo para download
			
			//Define o cabeçalho da resposta (essa definição é padrão)
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", pessoa.getNomeFileCurriculo());
			response.setHeader(headerKey, headerValue);
			
			try {
				response.getOutputStream().write(pessoa.getCurriculo()); //Finaliza a resposta passando o arquivo
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//PostMapping - equivalente a usar RequestMapping(method = RequestMethod.POST
	//RequestParam - também pode ser usado no Get
	@PostMapping("**/pesquisarPessoa")
	public ModelAndView pesquisar(@RequestParam("nomePesquisa") String nome, 
			@RequestParam("sexoPesquisa") String sexo, 
			@PageableDefault(size = 5, sort = {"nome"}) Pageable pageable) {
		
		Page<Pessoa> pessoas = null;
		
		if (sexo != null && !sexo.isEmpty()) {		//Se o sexo foi selecionado no campo de pesquisa
			pessoas = pessoaRepository.findPessoaByNomeSexoPage(nome, sexo, pageable);
		}
		else {
			pessoas = pessoaRepository.findPessoaByNomePage(nome, pageable);
		}
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroPessoa");
		modelAndView.addObject("pessoas", pessoas);
		modelAndView.addObject("nomePesquisa", nome);	//Para manter o nome no campo de pesquisa 
		
		return modelAndView;
	}
	
	@GetMapping("**/pesquisarPessoa")
	public void imprimirPDF(@RequestParam("nomePesquisa") String nome, @RequestParam("sexoPesquisa") String sexo,
			HttpServletRequest request, HttpServletResponse response) {
		
		List<Pessoa> pessoas = new ArrayList<>();
		List<Pessoa> p = (List<Pessoa>) request.getSession().getAttribute("pessoaID");
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroPessoa");

		//Busca por nome e sexo
		if (sexo != null && !sexo.isEmpty() && nome != null && !nome.isEmpty()) {
			pessoas = pessoaRepository.findPessoaByNomeSexo(nome, sexo);
		}
		//Busca somente por nome. O nome dentro do campo de pesquisa no momento do click no botão
		else if (nome != null && !nome.isEmpty()) {
			pessoas = pessoaRepository.findPessoaByNome(nome);
		}
		else if (sexo != null && !sexo.isEmpty()) {				//Busca somente por sexo
			pessoas = pessoaRepository.findPessoaBySexo(sexo);
		}
		else {							//Não informou nenhuma dado durante a pesquisa
			pessoas = (List<Pessoa>) pessoaRepository.findAll();
		}
		
		try {
			byte[] pdf = reportUtil.gerarRelatorio(pessoas, "pessoa", request.getServletContext());
			
			response.setContentLength(pdf.length);		//Tamanho da resposta
			//Para arquivos, vídeos, pdf, etc. Pode fazer download com qualquer arquivo com esse tipo de resposta
			response.setContentType("application/octet-stream");
			
			//Define o cabeçalho da resposta. Na parte do regex faz com que monte o cabeçalho corretament
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
			response.setHeader(headerKey, headerValue);
			
			response.getOutputStream().write(pdf);		//Finaliza a resposta para o navegador
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
