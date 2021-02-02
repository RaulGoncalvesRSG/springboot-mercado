package curso.springboot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.TelefoneRepository;

@Controller
public class TelefoneController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@GetMapping("/telefones/{idPessoa}")
	public ModelAndView telefones(@PathVariable("idPessoa") Integer idPessoa) {
		Pessoa pessoa = pessoaRepository.findById(idPessoa).get();
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaObj", pessoa);
		modelAndView.addObject("telefoneObj", new Telefone());
		modelAndView.addObject("telefones", pessoa.getTelefones());		//Preenche a tabela de telefones
		
		return modelAndView;
	}
	
	@PostMapping("**/addTelefonePessoa/{pessoaId}")
	public ModelAndView addTelefone(Telefone telefone, @PathVariable("pessoaId") Integer idPessoa) {
		Pessoa pessoa = pessoaRepository.findById(idPessoa).get();
		
		//Verificação feita manualmente
		if (telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty()) {
			
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			modelAndView.addObject("pessoaObj", pessoa);
			modelAndView.addObject("telefoneObj", telefone);
			modelAndView.addObject("telefones", pessoa.getTelefones());
			
			List<String> msg = new ArrayList<>();
			
			if (telefone.getNumero().isEmpty()) {
				msg.add("Número deve ser informado");
			}
			
			if (telefone.getTipo().isEmpty()) {
				msg.add("Tipo deve ser informado");
			}
			
			modelAndView.addObject("msg", msg);
			return modelAndView;
		}
		
		//O Spring automaticamente faz o mapeamento do obj preenchido no formulário. Ele preenche pelo atributo "name"
		
		telefone.setPessoa(pessoa);
		telefoneRepository.save(telefone);
		//Falta salvar o telefone em Pessoa
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaObj", pessoa);
		modelAndView.addObject("telefoneObj", new Telefone());
		modelAndView.addObject("telefones", pessoa.getTelefones());
		
		return modelAndView;
	}
	
	@GetMapping("/editarTelefone/{idTelefone}")
	public ModelAndView editarTelefone(@PathVariable("idTelefone") Integer idTelefone) {
		Pessoa pessoa = telefoneRepository.findById(idTelefone).get().getPessoa();
		Telefone telefone = telefoneRepository.findById(idTelefone).get();
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaObj", pessoa);
		modelAndView.addObject("telefoneObj", telefone);
		modelAndView.addObject("telefones", pessoa.getTelefones());
		
		return modelAndView;
	}
	
	@GetMapping("/removerTelefone/{idTelefone}")
	public ModelAndView excluirTelefone(@PathVariable("idTelefone") Integer idTelefone) {
		Pessoa pessoa = telefoneRepository.findById(idTelefone).get().getPessoa();
		telefoneRepository.deleteById(idTelefone);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaObj", pessoa);
		modelAndView.addObject("telefoneObj", new Telefone());
		modelAndView.addObject("telefones", pessoa.getTelefones());
		
		return modelAndView;
	}
}
