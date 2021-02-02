package curso.springboot.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import curso.springboot.repository.PessoaRepository;

@SpringBootTest
class SpringbootApplicationTests {

	@Autowired
	PessoaRepository pessoaRepository;
	
	@Test
	public void teste() {
	}
}
