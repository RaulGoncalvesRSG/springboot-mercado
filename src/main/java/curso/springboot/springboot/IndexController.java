package curso.springboot.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping("/")		//Faz a interceptação. Pode usar @GetMapping("/")
	public String index() {
		return "index";
	}
}
