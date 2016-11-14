package dwoz.waldo.web.services;

import java.util.List;

public class WaldoServicePayload<T> {

	String name;
	List<T> result;
	T template;
	
	public T getTemplate() {
		return template;
	}

	public void setTemplate(T template) {
		this.template = template;
	}

	public WaldoServicePayload(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}
	
	
}
