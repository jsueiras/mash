package mash.document.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mash.document.utils.handler.ElementHandler;
import mash.document.utils.handler.TableHandler;
import mash.document.utils.handler.TextHandler;

import org.docx4j.convert.out.pdf.viaXSLFO.PdfSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import com.mash.model.catalog.Person;
import com.mash.model.catalog.Referral;


public class DocumentRenderer {

	private Collection<ElementHandler> handlers;

	public DocumentRenderer() {
		handlers = new ArrayList<ElementHandler>();
		handlers.add(new TextHandler());
		handlers.add(new TableHandler());
	}

	private WordprocessingMLPackage getTemplate(String name)
			throws Docx4JException, FileNotFoundException {
		WordprocessingMLPackage template = WordprocessingMLPackage
				.load(getStreamTemplate(name));
		return template;
	}

	public InputStream getStreamTemplate(String name) {

		return this.getClass().getResourceAsStream("/" + name);
	}

	
	public void writeAsPdf(String templateName,Map<String, Object> ctx, OutputStream out) {

		try {

			WordprocessingMLPackage template = getTemplate(templateName);

			for (ElementHandler handler : handlers) {
				handler.processElement(template, ctx);
			}

			saveAsPdf(out, template);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	

	public void writeAsWord(String templateName,Map<String, Object> ctx, OutputStream out) {

		try {

			WordprocessingMLPackage template = getTemplate(templateName);

			for (ElementHandler handler : handlers) {
				handler.processElement(template, ctx);
			}

			saveAsWord(out, template);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	
	private void saveAsWord(OutputStream out, WordprocessingMLPackage template)
			throws Exception, Docx4JException {
		
		template.save(out);
	}


	private void saveAsPdf(OutputStream out, WordprocessingMLPackage template)
			throws Exception, Docx4JException {
		template.setFontMapper(new IdentityPlusMapper());
		// Set up converter
		org.docx4j.convert.out.pdf.PdfConversion c = new org.docx4j.convert.out.pdf.viaXSLFO.Conversion(
				template);
		// Write to output stream

		c.output(out, new PdfSettings());
	}

}
