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

	private String ip;

	@Lob
	private byte[] privateKey;

	private Long idVictim;

	public Target(Long id, String ip, byte[] privateKey, Long idVictim) {
		super();
		this.id = id;
		this.ip = ip;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public byte[] getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(byte[] privateKey) {
		this.privateKey = privateKey;
	}

	public Long getIdVictim() {
		return idVictim;
	}

	public void setIdVictim(Long idVictim) {
		this.idVictim = idVictim;
	}

}
