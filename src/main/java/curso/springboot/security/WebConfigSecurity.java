package curso.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{

	@Autowired
	ImplUserDatailsService implUserDatailsService;
	
	@Override				//Configura as solicitações de acesso http
	protected void configure(HttpSecurity http) throws Exception {
		//Cada método é uma configuração que está sendo ativada
		http.csrf()
		.disable()		//Desativa as configurações padrão de memória para fazer a configuração manual
		.authorizeRequests()							//Permite restringir acesso
		.antMatchers(HttpMethod.GET, "/").permitAll()	//Qualquer usuário acessa a página inicial
		//Apenas ADMIN terá acesso à página especificada
		.antMatchers(HttpMethod.GET, "/cadastroPessoa").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and().formLogin().permitAll()					//Permite qualquer usuário
		.loginPage("/login")							//Diz qual é a pag de login
		.defaultSuccessUrl("/menu")			//Se logar vai para a pag especificada
		.failureUrl("/login?error=true")				//Se falhar o login, Continua na pag e mostra o erro
		//Mapea a URL de logout e invalida usuário autenticado
		.and().logout().logoutSuccessUrl("/login")		//Quando fizer logout, volta para a pag de login
		//Quando passar essa url, vai invalidar e encerrar a sessão
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		
		//Essas configurações fazem criar um formulário automático
	}
	
	@Override				//Cria autenticação do usuário com BD ou em memória
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//NoOpPasswordEncoder.getInstance() - não é utilizado a criptografia
		//new BCryptPasswordEncoder() - Utiliznado a criptografia
		
		//O método loadUserByUsername(username) é chamado automaticamente pelo implUserDatailsService
		auth.userDetailsService(implUserDatailsService)
		.passwordEncoder(NoOpPasswordEncoder.getInstance());
	}
	
	@Override					//Ignora URL específicas
	public void configure(WebSecurity web) throws Exception {
		//O materialize aparece na pág login, então o usuário precisa de acesso mesmo se n tiver logado
		web.ignoring().antMatchers("/materialize/**");
	}
}
