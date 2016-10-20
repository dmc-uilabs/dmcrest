package org.dmc.services.data.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "directory")
public class Directory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Directory parent;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	private List<Directory> children;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Directory getParent() {
		return parent;
	}

	public void setParent(Directory parent) {
		this.parent = parent;
	}

	public List<Directory> getChildren() {
		return children;
	}

	public void setChildren(List<Directory> children) {
		children.stream().forEach((a) -> a.setParent(this));
		this.children = children;
	}

}
