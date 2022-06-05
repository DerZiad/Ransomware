package onion.aluka.datas;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "targets")
public class Target {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Lob
	private String privateKey;

	private Long idVictim;

	public Target(Long id, String privateKey, Long idVictim) {
		this.id = id;
		this.privateKey = privateKey;
		this.idVictim = idVictim;
	}

	public Target() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public Long getIdVictim() {
		return idVictim;
	}

	public void setIdVictim(Long idVictim) {
		this.idVictim = idVictim;
	}

}
