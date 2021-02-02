package curso.springboot.model;

import java.io.Serializable;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull(message = "Nome não pode ser nulo")
	@NotEmpty(message = "Nome não pode ser vazio")
	private String nome;

	@NotNull(message = "Sobrenome não pode ser nulo")
	@NotEmpty(message = "Sobrenome não pode ser vazio")
	private String sobrenome;

	@NotNull(message = "Idade não pode ser nulo")
	@Min(value = 18, message = "Idade mínima de 18 anos!")
	private Integer idade;

	@OneToMany(mappedBy = "pessoa", cascade = CascadeType.REMOVE)
	private List<Telefone> telefones;

	@CPF(message = "CPF inválido")
	@NotNull(message = "CPF não pode ser nulo")
	@NotEmpty(message = "CPF não pode ser vazio")
	private String cpf;

	@OneToOne(cascade = CascadeType.REMOVE)
	private Endereco endereco;

	private String sexo;

	@ManyToOne
	private Profissao profissao;

	@Enumerated(EnumType.STRING)
	private Cargo cargo;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;

	@Transient
	private Integer idadeCalculo;

	@Lob
	private byte[] curriculo;

	private String nomeFileCurriculo;
	private String tipoFileCurriculo; // ContentType

	@Transient
	private String fotoTemp;				//Imagem formada para aparecer na tela

	public Pessoa() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Profissao getProfissao() {
		return profissao;
	}

	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Integer getIdadeCalculo() {
		if (dataNascimento != null) {
			Calendar dataAtual = Calendar.getInstance();

			Calendar nascimento = Calendar.getInstance();
			nascimento.setTime(dataNascimento);

			idadeCalculo = dataAtual.get(Calendar.YEAR) - nascimento.get(Calendar.YEAR);

			int mesAtual = dataAtual.get(Calendar.MONTH);
			int mesNascimento = nascimento.get(Calendar.MONTH);

			int diaAtual = dataAtual.get(Calendar.DAY_OF_MONTH);
			int diaNascimento = nascimento.get(Calendar.DAY_OF_MONTH);

			if (mesAtual < mesNascimento) {
				idadeCalculo--;
			} else {
				if (diaAtual < diaNascimento) {
					idadeCalculo--;
				}
			}
		}

		return idadeCalculo;
	}

	public byte[] getCurriculo() {
		return curriculo;
	}

	public void setCurriculo(byte[] curriculo) {
		this.curriculo = curriculo;
	}

	public String getNomeFileCurriculo() {
		return nomeFileCurriculo;
	}

	public void setNomeFileCurriculo(String nomeFileCurriculo) {
		this.nomeFileCurriculo = nomeFileCurriculo;
	}

	public String getTipoFileCurriculo() {
		return tipoFileCurriculo;
	}

	public void setTipoFileCurriculo(String tipoFileCurriculo) {
		this.tipoFileCurriculo = tipoFileCurriculo;
	}

	public String getFotoTemp() {
		String fotoBase64 = Base64.getEncoder().encodeToString(getCurriculo());
		fotoTemp = "data:" + tipoFileCurriculo + ";base64," + fotoBase64;
		return fotoTemp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
