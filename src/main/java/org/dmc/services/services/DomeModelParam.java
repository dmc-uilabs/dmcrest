package org.dmc.services.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DomeModelParam {
	private String type = null;
	private String name = null;
	private String unit = null;
	private String category = null;
	private String value = null;
	private String parameterid = null;
	private String instancename = null;

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("unit")
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@JsonProperty("category")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@JsonProperty("value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@JsonProperty("parameterid")
	public String getParameterid() {
		return parameterid;
	}

	public void setParameterid(String parameterid) {
		this.parameterid = parameterid;
	}

	@JsonProperty("instancename")
	public String getInstancename() {
		return instancename;
	}

	public void setInstancename(String instancename) {
		this.instancename = instancename;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DomeModelParam domeModelParam = (DomeModelParam) o;
		return Objects.equals(type, domeModelParam.type) && Objects.equals(name, domeModelParam.name)
				&& Objects.equals(unit, domeModelParam.unit) && Objects.equals(category, domeModelParam.category)
				&& Objects.equals(value, domeModelParam.value)
				&& Objects.equals(parameterid, domeModelParam.parameterid)
				&& Objects.equals(instancename, domeModelParam.instancename);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DomeModelParam {\n");

		sb.append("  type: ").append(type).append("\n");
		sb.append("  name: ").append(name).append("\n");
		sb.append("  unit: ").append(unit).append("\n");
		sb.append("  category: ").append(category).append("\n");
		sb.append("  value: ").append(value.toString()).append("\n");
		sb.append("  parameterid: ").append(parameterid).append("\n");
		sb.append("  instancename: ").append(instancename).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
	
/*	public static void main(String[] args)
	{
		String testString = "{\"serviceId\":\"13\",\"inParams\":{\"localPath\":{\"type\":\"String\",\"name\":\"localPath\",\"unit\":\"\",\"category\":null,\"value\":\"/tmp\",\"parameterid\":\"73185a14-d398-1004-8645-569b82669417\",\"instancename\":null,\"defaultValue\":\"D:/CMCTemp\",\"$$hashKey\":\"object:714\"},\"inFile\":{\"type\":\"File\",\"name\":\"inFile\",\"unit\":\"\",\"category\":null,\"value\":{},\"parameterid\":\"73185a12-d398-1004-8645-569b82669417\",\"instancename\":null,\"defaultValue\":\"0\",\"$$hashKey\":\"object:715\"}},\"outParams\":{\"outFilename\":{\"type\":\"String\",\"name\":\"outFilename\",\"unit\":\"\",\"category\":null,\"value\":null,\"parameterid\":\"73185a16-d398-1004-8645-569b82669417\",\"instancename\":\"outFilename\"}}}";
    	JSONObject inputs = new JSONObject(testString);
    	String sId = inputs.getString("serviceId");
    	JSONObject inpars = inputs.getJSONObject("inParams");	
    	Map<String,DomeModelParam> parMaps = new HashMap<String,DomeModelParam>();
    	
		Iterator it = inpars.keys();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			String variableName = (String)pair.getKey();
			JSONObject variableValue = (JSONObject) pair.getValue();
			DomeModelParam parValue = new DomeModelParam();
			
			parValue.setType(variableValue.getString("type"));
			parValue.setName(variableValue.getString("name"));
			parValue.setUnit(variableValue.getString("unit"));
			parValue.setCategory(variableValue.getString("category"));
			parValue.setValue(variableValue.getString("value"));
			parValue.setParameterid(variableValue.getString("parameterid"));
			parValue.setInstancename(variableValue.getString("instancename"));
			parValue.setInstancename(variableValue.getString("defaultValue"));			
			
			// Need a type change here.
			parMaps.put(variableName, parValue);
		}
		
		System.out.println("Done");
	}
*/
}
