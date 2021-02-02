package curso.springboot.model;

import java.io.Serializable;
import java.util.Base64;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private Double preco;
	private Integer quantidade;

	@ManyToOne
	private Categoria categoria;

	@Lob
	private byte[] imagem;

	private String nomeFileImagem;
	private String tipoFileImagem; // ContentType

	@Transient
	private String fotoTemp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	public String getNomeFileImagem() {
		return nomeFileImagem;
	}

	public void setNomeFileImagem(String nomeFileImagem) {
		this.nomeFileImagem = nomeFileImagem;
	}

	public String getTipoFileImagem() {
		return tipoFileImagem;
	}

	public void setTipoFileImagem(String tipoFileImagem) {
		this.tipoFileImagem = tipoFileImagem;
	}

	public String getFotoTemp() {
		String fotoBase64 = Base64.getEncoder().encodeToString(getImagem());
		fotoTemp = "data:" + tipoFileImagem + ";base64," + fotoBase64;
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
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
