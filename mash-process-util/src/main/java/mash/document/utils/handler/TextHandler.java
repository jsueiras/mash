package mash.document.utils.handler;

import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;


import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Text;

public class TextHandler  extends BaseElementHandler {

	@Override
	public void processElement(WordprocessingMLPackage template,
			Map<String, Object> context) throws Docx4JException, JAXBException {
			List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
	      
			for (int i =0;i<texts.size();i++)
			{
				Text textElement = (Text) texts.get(i);
				i = replaceText(context, texts, i, textElement,VALUE_TAG);
			}
	}

    private int replaceText(Map<String, Object> context,
		List<Object> texts, int i, Text textElement, String tag) {
	String value = textElement.getValue();
	if (value.contains(tag.substring(0,1))) {
	    ResultPlaceholder placeHolder = getAssociatedValues(texts,i,true,VALUE_TAG);
		if (placeHolder!= null)
		{		
			String replace = getValue(placeHolder.result, context);
			textElement.setValue(replace);
			i= i+ placeHolder.count;
		}	
	}
	return i;
}

}
