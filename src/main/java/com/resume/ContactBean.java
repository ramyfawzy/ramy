package com.resume;

import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named
@ViewScoped
public class ContactBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name ;
	private String email ;
	private String subject ;
	private String message ;
	
	private StreamedContent file;
	
	@PostConstruct
	public void init() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		InputStream stream = ec.getResourceAsStream("/resources/images/RamyIbrahim.pdf");
		file = new DefaultStreamedContent(stream, "application/pdf", "RamyIbrahim.pdf");
	}

	public ContactBean() {
	}

	public ContactBean(String name, String email, String subject, String message) {
		super();
		this.name = name;
		this.email = email;
		this.subject = subject;
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public StreamedContent getFile() {
		return file;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		builder.append(formatter.format(date)).append(" [name=").append(name).append(", email=").append(email).append(", subject=")
				.append(subject).append(", message=").append(message).append("]");
		return builder.toString();
	}
	
	

}
