package com.capitalone.dashboard.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Base64;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Document(collection = "configuration")
public class Configuration extends BaseModel {

	private final static String PREFIX = "BASE64:";
	private static final Pattern COMPILE = Pattern.compile(PREFIX);

	public Configuration(String collectorName, Set<Map<String, String>> info) {
		super();
		this.collectorName = collectorName;
		this.info = info;
	}

	public Set<Map<String, String>> decryptOrEncrptInfo() {
		for (Map<String, String> info : info) {
			if (!info.isEmpty()) {
				String password = info.get("password");
				if(password.contains(PREFIX)){
					Base64.Decoder decoder = Base64.getDecoder();
					password = new String(decoder.decode(COMPILE.matcher(password).replaceFirst("").trim()));
				} else {
					Base64.Encoder encoder = Base64.getEncoder();
					password = PREFIX + encoder.encodeToString(password.getBytes());
				}
				info.replace("password", password);
			}
		}
		return info;
	}

	public Configuration() {
	}

	private String collectorName;
	private Set<Map<String, String>> info = new HashSet<>();

	public String getCollectorName() {
		return collectorName;
	}

	public void setCollectorName(String collectorName) {
		this.collectorName = collectorName;
	}

	public Set<Map<String, String>> getInfo() {
		return info;
	}

	public void setInfo(Set<Map<String, String>> info) {
		this.info = info;
	}


}
