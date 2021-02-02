package curso.springboot.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
//Faz um escaneamento das entidades. OBS: não é obrigatório, o próprio spring já faz isso
@EntityScan(basePackages="curso.springboot.model")
@ComponentScan(basePackages= {"curso.*"})				//Força o mapeamento para o controller
@EnableJpaRepositories(basePackages= {"curso.springboot.repository"})
@EnableTransactionManagement
@EnableWebMvc
public class SpringbootApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//Mapea a URL e depois a página para qual vai ser redirecionada
		registry.addViewController("/login").setViewName("/login");
		//Seta essa bean como de baixa precedência/prioridade na inicialização do projeto
		registry.setOrder(Ordered.LOWEST_PRECEDENCE);		
	}
	
	/*Para liberar a pasta static  e o projeto conseguir ler os arquivos javascript, css e imagem em algum 
	 projetos necessário adicionar o classpat e o caminho das pastas do projeto*/
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/img/**", "/css/**", "/js/**").addResourceLocations("classpath:/static/img/",
				"classpath:/static/css/", "classpath:/static/js/");
	}
}
