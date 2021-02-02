package curso.springboot.springboot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class Testes {

	@Test
	public void testeCalcularIdade() {
		Date dataNasc = new Date("09/09/2018");
		Calendar dataAtual = Calendar.getInstance();
		
		Calendar nascimento = Calendar.getInstance();
		nascimento.setTime(dataNasc);
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println("Nascimento: " +format.format(nascimento.getTime()));
		
		int idadeCalculo = dataAtual.get(Calendar.YEAR) - nascimento.get(Calendar.YEAR);
		
		int mesAtual = dataAtual.get(Calendar.MONTH);
		int mesNascimento = nascimento.get(Calendar.MONTH);
		
		int diaAtual = dataAtual.get(Calendar.DAY_OF_MONTH);
		int diaNascimento = nascimento.get(Calendar.DAY_OF_MONTH);
			
		if (mesAtual < mesNascimento) {
			idadeCalculo--;
		}
		else {
			if (diaAtual < diaNascimento) {
				idadeCalculo--;
			}
		}
		System.out.println("Idade: " + idadeCalculo);
	}
}
