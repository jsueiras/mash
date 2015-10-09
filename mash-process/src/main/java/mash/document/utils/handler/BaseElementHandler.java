package mash.document.utils.handler;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.apache.commons.beanutils.BeanUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Text;

public abstract class BaseElementHandler implements ElementHandler {

	List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
		List<Object> result = new ArrayList<Object>();
		if (obj instanceof JAXBElement)
			obj = ((JAXBElement<?>) obj).getValue();

		if (obj.getClass().equals(toSearch))
			result.add(obj);
		else if (obj instanceof ContentAccessor) {
			List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child : children) {
				result.addAll(getAllElementFromObject(child, toSearch));
			}

		}
		return result;
	}

	 ResultPlaceholder getAssociatedValues(List<Object> texts, int i,
			boolean replace, String tag) {
		List<Text> associated = new ArrayList<Text>();
		Text firstElement = (Text) texts.get(i);
		if (firstElement.getValue().trim().length() > 1) {
			if (!firstElement.getValue().trim().startsWith(tag))
				return null;
		} else {
			if (i == texts.size())
				return null;
			else {
				i = i + 1;
				Text secondElement = (Text) texts.get(i);
				if (secondElement.getValue().startsWith(OPEN_TAG))
					associated.add(firstElement);
				else
					return null;
			}
		}

		for (int j = i; j < texts.size(); j++) {
			Text element = (Text) texts.get(j);
			associated.add(element);
			if (element.getValue().indexOf(END_TAG) >= 0) {

				String result = "";
				for (Text text : associated) {
					result = result + text.getValue();
					if (replace)
						text.setValue("");
				}
				return new ResultPlaceholder(associated.size() - 1, result
						.replace(tag, "").replace(END_TAG, "").trim());
			}
		}
		return null;
	}

	class ResultPlaceholder {
		int count;
		String result;

		public ResultPlaceholder(int count, String result) {
			this.count = count;
			this.result = result;
		}
	}
	
	String getValue(String placeHolder, Map<String,Object> context)
	{
		int indexOf = placeHolder.indexOf(".");
		if  (indexOf>0)
		{
	        String prefix = placeHolder.substring(0,indexOf);
	        String sufix = placeHolder.substring(indexOf+1);
			Object object = context.get(prefix);
			if (object!=null) return getStringValue(sufix, object);
		}		
		else if (context.get(placeHolder)!= null)
		{
			return formatValue(context.get(placeHolder));
		}	
		
		return "";
	}
	
	 String getStringValue(String sufix, Object object) {
		Object value;
		try {
			if (object instanceof Map)
			{
				value =   formatValue(((Map)object).get(sufix));
			}
			else
			{	
			    value = BeanUtils.getProperty(object, sufix);
			}
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return formatValue(value);
		
	}

	private String formatValue(Object value) {
		if (value ==null) return "";
		if (value instanceof Date)
		{
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			
			return formatter.format((Date)value);
			
		}
		return value.toString();
	}

}
