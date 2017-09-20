package org.dmc.services.data.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "service_run_parameter")
public class ServiceRunParameter extends BaseEntity {
	
	@Id
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "parameter_id")
	private ServiceInterfaceParameter parameter;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "run_id")
	private ServiceRun run;
	
	private String value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ServiceInterfaceParameter getParameter() {
		return parameter;
	}

	public void setParameter(ServiceInterfaceParameter parameter) {
		this.parameter = parameter;
	}

	public ServiceRun getRun() {
		return run;
	}

	public void setRun(ServiceRun run) {
		this.run = run;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
