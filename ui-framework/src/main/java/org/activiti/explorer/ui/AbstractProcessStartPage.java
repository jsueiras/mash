package org.activiti.explorer.ui;

import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.explorer.ui.custom.ToolBar;

import com.vaadin.ui.Table;

public abstract class AbstractProcessStartPage extends AbstractTablePage {

	public abstract void showStartForm(ProcessDefinition processDefinition,
			StartFormData startFormData) ;

}
