package com.resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ViewScoped;
import javax.inject.*;
import javax.servlet.http.HttpServletResponse;

@Named
@ViewScoped
public class HelloWorld implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String firstName = "Ramy Ibrahim";
	private String lastName = "Doe";

	@PostConstruct
	public void init() {
		System.out.println("@PostConstruct");
	}

	public HelloWorld() {
		System.out.println("Constructor");
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String showGreeting() {
		return "Hello " + firstName + " " + lastName + "!";
	}

	public void writeContactMsg(ContactBean contactBean) throws IOException {
		Path path = Paths.get("msgs.txt");
		byte[] strToBytes = System.lineSeparator().concat(contactBean.toString()).getBytes();
		Files.write(path, strToBytes, StandardOpenOption.APPEND);
	}

	public void downloadPdf() throws IOException {
		// Get the FacesContext
		FacesContext facesContext = FacesContext.getCurrentInstance();

		// Get HTTP response
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

		// Set response headers
		response.reset(); // Reset the response in the first place
		response.setHeader("Content-Type", "application/pdf"); // Set only the content type

		// Open response output stream
		OutputStream responseOutputStream = response.getOutputStream();

		// Read PDF contents
		URL url = new URL("http://download.itcuties.com/jsf2/jsf2-download-pdf/itcuties-logo-concept.pdf");
		InputStream pdfInputStream = url.openStream();

		// Read PDF contents and write them to the output
		byte[] bytesBuffer = new byte[2048];
		int bytesRead;
		while ((bytesRead = pdfInputStream.read(bytesBuffer)) > 0) {
			responseOutputStream.write(bytesBuffer, 0, bytesRead);
		}

		// Make sure that everything is out
		responseOutputStream.flush();

		// Close both streams
		pdfInputStream.close();
		responseOutputStream.close();

		// JSF doc:
		// Signal the JavaServer Faces implementation that the HTTP response for this
		// request has already been generated
		// (such as an HTTP redirect), and that the request processing lifecycle should
		// be terminated
		// as soon as the current phase is completed.
		facesContext.responseComplete();

	}

	public void download() throws IOException {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();

		ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the
							// buffer beforehand. We want to get rid of them, else it may collide.
		ec.setResponseContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all
														// types. Use
		// if necessary ExternalContext#getMimeType() for auto-detection based
		// on filename.
		ec.setResponseContentLength(90000000); // Set it with the file size. This header is optional. It will work
											// if it's omitted, but the download progress will be unknown.
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "ramy_ibrahim.pdf" + "\""); // The Save As popup
																									// magic is done
																									// here. You can
																									// give it any file
																									// name you want,
																									// this only won't
																									// work in MSIE, it
																									// will use current
																									// request URL as
																									// file name
																									// instead.

		OutputStream output = ec.getResponseOutputStream();
		// Now you can write the InputStream of the file to the above OutputStream the
		// usual way.
		// ...

		// Read PDF contents
		URL url = new URL("http://download.itcuties.com/jsf2/jsf2-download-pdf/itcuties-logo-concept.pdf");
		InputStream pdfInputStream = url.openStream();

		// Read PDF contents and write them to the output
		byte[] bytesBuffer = new byte[2048];
		int bytesRead;
		while ((bytesRead = pdfInputStream.read(bytesBuffer)) > 0) {
			output.write(bytesBuffer, 0, bytesRead);
		}

		// Make sure that everything is out
		output.flush();

		// Close both streams
		pdfInputStream.close();
		output.close();

		fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously
								// will fail since it's already written with a file and closed.
	}
}
