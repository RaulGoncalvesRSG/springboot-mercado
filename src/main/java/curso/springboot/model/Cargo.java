package curso.springboot.model;

public enum Cargo {

	JUNIOR("Júnior"), PLENO("Pleno"), SENIOR("Senior");

	private String nome;
	private String valor;

	private Cargo(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getValor() {
		return valor = this.name();			//Pega os names JUNIOR, PLENO e SENIOR
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}
