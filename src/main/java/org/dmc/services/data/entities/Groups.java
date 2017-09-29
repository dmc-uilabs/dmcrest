package org.dmc.services.data.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "groups")
public class Groups extends BaseEntity {
	
	@Id
	@Column(name = "group_id")
	Integer id;
	String group_name;
	
//	String homepage;
//	Integer is_public;
//	  status character(1) NOT NULL DEFAULT 'A'::bpchar,
//	  unix_group_name character varying(200) NOT NULL DEFAULT ''::character varying,
//	  unix_box character varying(20) NOT NULL DEFAULT 'shell1'::character varying,
//	  http_domain character varying(80),
//	  short_description text,
//	  register_purpose text,
//	  license_other text,
//	  register_time integer NOT NULL DEFAULT 0,
//	  rand_hash text,
//	  use_mail integer NOT NULL DEFAULT 1,
//	  use_survey integer NOT NULL DEFAULT 1,
//	  use_forum integer NOT NULL DEFAULT 1,
//	  use_pm integer NOT NULL DEFAULT 1,
//	  use_scm integer NOT NULL DEFAULT 1,
//	  use_news integer NOT NULL DEFAULT 1,
//	  type_id integer NOT NULL DEFAULT 1,
//	  use_docman integer NOT NULL DEFAULT 1,
//	  new_doc_address text NOT NULL DEFAULT ''::text,
//	  send_all_docs integer NOT NULL DEFAULT 0,
//	  use_pm_depend_box integer NOT NULL DEFAULT 1,
//	  use_ftp integer DEFAULT 1,
//	  use_tracker integer DEFAULT 1,
//	  use_frs integer DEFAULT 1,
//	  use_stats integer DEFAULT 1,
//	  enable_pserver integer DEFAULT 1,
//	  enable_anonscm integer DEFAULT 1,
//	  license integer DEFAULT 100,
//	  scm_box text,
//	  use_docman_search integer DEFAULT 1,
//	  force_docman_reindex integer DEFAULT 0,
//	  use_webdav integer DEFAULT 1,
//	  use_docman_create_online integer DEFAULT 0,
//	  is_template integer NOT NULL DEFAULT 0,
//	  built_from_template integer NOT NULL DEFAULT 0,
//	  export_control boolean DEFAULT false,
//	  gallery_folder integer NOT NULL DEFAULT 0,
//	  profile_image integer NOT NULL DEFAULT 0,
//	  user_id integer,
//	  due_date timestamp without time zone,
//	  requires_approval boolean,
//	  directory_id integer,
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

}
