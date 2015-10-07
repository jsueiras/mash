/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.explorer.ui.login;

import org.activiti.engine.impl.identity.Authentication;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.identity.LoggedInUser;
import org.springframework.web.util.HtmlUtils;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * @author Joram Barrez
 */
public class ExplorerLoginForm extends VerticalLayout implements ClickListener {
  
  private static final long serialVersionUID = 1L;
  
  protected String usernameCaption;
  protected String passwordCaption;
  protected String submitCaption;
  
	private final TextField user;

	private final PasswordField password;

	private final Button loginButton;

	
	protected transient LoginHandler loginHandler;
	

  
  protected static final String STYLE_LOGIN_FIELD = "login-field";
  protected static final String STYLE_LOGIN_FIELD_CAPTION = "login-field-caption";
  protected static final String STYLE_LOGIN_BUTTON = "login-button";
  
  public ExplorerLoginForm() {
	 // setMargin(true);
	   this.setSizeFull();
	  
	  I18nManager i18nManager = ExplorerApp.get().getI18nManager();
	  loginHandler = ExplorerApp.get().getLoginHandler();
      usernameCaption = HtmlUtils.htmlEscape(i18nManager.getMessage(Messages.LOGIN_USERNAME));
       passwordCaption = HtmlUtils.htmlEscape(i18nManager.getMessage(Messages.LOGIN_PASSWORD));
      submitCaption = HtmlUtils.htmlEscape(i18nManager.getMessage(Messages.LOGIN_BUTTON));
   
      
      Label title = new Label( i18nManager.getMessage(Messages.LOGIN_TITLE));
      title.addStyleName( Reindeer.LABEL_H1 );
       
      
	  
		// Create the user input field
		user = new TextField(usernameCaption);
		user.setWidth("300px");
		user.setRequired(true);
		user.setInputPrompt("Your username (eg. joe@email.com)");


		// Create the password input field
		password = new PasswordField(passwordCaption);
		password.setWidth("300px");
		password.setRequired(true);
		password.setValue("");
		password.setNullRepresentation("");

		// Create login button
		loginButton = new Button(submitCaption,this );
    
		  VerticalLayout fields = new VerticalLayout();
		 
	   fields.addComponent(title);
       fields.addComponent(user);
       fields.addComponent(password);
       fields.addComponent(loginButton);
   	   fields.setSpacing(true);
	   fields.setMargin(new MarginInfo(true, true, true, false));
	    fields.setSizeUndefined();
       	addComponent(fields);
       setComponentAlignment(fields, Alignment.MIDDLE_CENTER);

      
  }

@Override
public void buttonClick(ClickEvent event) {

	//
	// Validate the fields using the navigator. By using validors for the
	// fields we reduce the amount of queries we have to use to the database
	// for wrongly entered passwords
	//
	if (!user.isValid() || !password.isValid()) {
		return;
	}

	String username = user.getValue();
	String password = this.password.getValue();

	
	LoggedInUser user =loginHandler.authenticate(username, password);

	if (user!=null) {
        ExplorerApp.get().setUser(user);
        ExplorerApp.get().getViewManager().showDefaultPage();
        Authentication.setAuthenticatedUserId(user.getId());
		
	} else {

		// Wrong password clear the password field and refocuses it
		this.password.setValue(null);
		this.password.focus();

	}
}
  


}
