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
package org.activiti.explorer;

import com.google.common.io.Closeables;
import com.mash.data.service.Repository;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.explorer.identity.LoggedInUser;
import org.activiti.explorer.ui.ComponentFactory;
import org.activiti.explorer.ui.content.AttachmentRendererManager;
import org.activiti.explorer.ui.form.FormPropertyRendererManager;
import org.activiti.explorer.ui.login.ExplorerLoginForm;
import org.activiti.explorer.ui.login.LoginHandler;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.activiti.explorer.ui.mainlayout.MainLayout;
import org.activiti.explorer.ui.variable.VariableRendererManager;
import org.activiti.workflow.simple.converter.WorkflowDefinitionConversionFactory;
import org.activiti.workflow.simple.converter.json.SimpleWorkflowJsonConverter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;


/**
 * @author Joram Barrez
 */
@Theme("codeGraph")
@SpringUI(path = "/home")
@Widgetset("com.mash.ui_framework.MyAppWidgetset")
public class ExplorerApp extends UI {

  private static final long serialVersionUID = -1L;

	protected String environment;
  protected boolean useJavascriptDiagram;

  @Autowired
  protected ViewManager viewManager;
  @Autowired
  protected NotificationManager notificationManager;
  @Autowired
  protected I18nManager i18nManager;
  @Autowired
  protected AttachmentRendererManager attachmentRendererManager;
  @Autowired
  protected FormPropertyRendererManager formPropertyRendererManager;
  @Autowired
  protected VariableRendererManager variableRendererManager;
  @Autowired
  protected LoginHandler loginHandler;
  @Autowired
  protected ComponentFactories componentFactories;
  @Autowired
  protected WorkflowDefinitionConversionFactory workflowDefinitionConversionFactory;
  @Autowired
  protected SimpleWorkflowJsonConverter simpleWorkflowJsonConverter;
  @Autowired
  protected Repository mashRepository;


  // Flag to see if the session has been invalidated, when the application was closed
  protected boolean invalidatedSession = false;

  protected List<String> adminGroups;
  protected List<String> userGroups;

  protected String crystalBallCurrentDefinitionId = null;
  protected String crystalBallCurrentInstanceId = null;

  private boolean showingLoginPage;

  private MainLayout mainLayout;
  private ExplorerLoginForm loginForm;


      @Override
      protected void init(VaadinRequest vaadinRequest) {

    	  // Set current application object as thread-local to make it easy accessible
    	    viewManager = new DefaultViewManager();
    	    //i18nManager.setLocale(this.getLocale());
    	    notificationManager.setMainWindow(this);

    	    loginForm = new ExplorerLoginForm();
    	    // Authentication: check if user is found, otherwise send to login page
    	    LoggedInUser user =  getUser();


    	    if (user == null) {
    	       viewManager.showLoginPage();
    	    }

    	    if(user != null) {
    	    	 Authentication.setAuthenticatedUserId(user.getId());
    	       viewManager.showDefaultPage();
    	    }



      }

      public void showLoginPage() {
  	    showingLoginPage = true;
  	    addStyleName(ExplorerLayout.STYLE_LOGIN_PAGE);

		setContent(loginForm);
  	  }

      public void showDefaultContent() {
    	   showingLoginPage = false;
    	    mainLayout = new MainLayout();
    	    removeStyleName(ExplorerLayout.STYLE_LOGIN_PAGE);
    	    addStyleName("Default style"); // Vaadin bug: must set something or old style (eg. login page style) is not overwritten

    	    // init general look and feel

    	    setContent(mainLayout);

    	    // init hidden components
    	    //initHiddenComponents();
    	  }


  /**
   *  Required to support multiple browser windows/tabs,
   *  see http://vaadin.com/web/joonas/wiki/-/wiki/Main/Supporting%20Multible%20Tabs
   */
//  public Window getWindow(String name) {
//    Window window = super.getWindow(name);
//    if (window == null) {
//      window = new Window("Activiti Explorer");
//      window.setName(name);
//      addWindow(window);
//      window.open(new ExternalResource(window.getURL()));
//    }
//
//    return window;
//  }

  @Override
  public void close() {
    final LoggedInUser theUser = getLoggedInUser();

    // Clear the logged in user
    setUser(null);

    // Call loginhandler
    getLoginHandler().logout(theUser);

    invalidatedSession = true;

    super.close();
  }

  public void setUser(LoggedInUser user) {
	getSession().setAttribute(LoggedInUser.class, user);

}

public static ExplorerApp get() {
     return (ExplorerApp) UI.getCurrent();
  }

  public LoggedInUser getLoggedInUser() {
    return (LoggedInUser) getUser();
  }

  private LoggedInUser getUser() {
	return getSession().getAttribute(LoggedInUser.class);
}

public String getEnvironment() {
    return environment;
  }

  // Managers (session scoped)

  public ViewManager getViewManager() {
    return viewManager;
  }

  public I18nManager getI18nManager() {
    return i18nManager;
  }

  public NotificationManager getNotificationManager() {
    return notificationManager;
  }

  // Application-wide services

  public AttachmentRendererManager getAttachmentRendererManager() {
    return attachmentRendererManager;
  }

  public FormPropertyRendererManager getFormPropertyRendererManager() {
    return formPropertyRendererManager;
  }

  public void setFormPropertyRendererManager(FormPropertyRendererManager formPropertyRendererManager) {
    this.formPropertyRendererManager = formPropertyRendererManager;
  }

  public <T> ComponentFactory<T> getComponentFactory(Class<? extends ComponentFactory<T>> clazz) {
    return componentFactories.get(clazz);
  }

  public LoginHandler getLoginHandler() {
    return loginHandler;
  }

  public void setVariableRendererManager(VariableRendererManager variableRendererManager) {
    this.variableRendererManager = variableRendererManager;
  }

  public VariableRendererManager getVariableRendererManager() {
    return variableRendererManager;
  }

  public WorkflowDefinitionConversionFactory getWorkflowDefinitionConversionFactory() {
    return workflowDefinitionConversionFactory;
  }

  public void setLocale(Locale locale) {
    super.setLocale(locale);
    if(i18nManager != null) {
      i18nManager.setLocale(locale);
    }
  }





  // Error handling ---------------------------------------------------------------------------------

  public void setMashRepository(Repository mashRepository) {
	this.mashRepository = mashRepository;
}

private boolean isRunning() {
	// TODO Auto-generated method stub
	return false;
}

public void terminalError(ErrorEvent event) {
    //super.terminalError(event);

    // Look for an Activiti Exception, as it'll probably be more meaningful.
    // If not found, just show default
    Throwable exception = null; //event.getThrowable().getCause();
    int depth = 0; // To avoid going too deep in the stack
    while (exception != null && depth<20 && !(exception instanceof ActivitiException)) {
      exception = exception.getCause();
      depth++;
    }

    if (exception == null) {
      //exception = event.getThrowable().getCause();
    }
    notificationManager.showErrorNotification(Messages.UNCAUGHT_EXCEPTION, exception.getMessage());
  }



  // Injection setters

  public void setEnvironment(String environment) {
    this.environment = environment;
  }
  public boolean isUseJavascriptDiagram() {
    return useJavascriptDiagram;
  }
  public void setUseJavascriptDiagram(boolean useJavascriptDiagram) {
    this.useJavascriptDiagram = useJavascriptDiagram;
  }

  public void setViewManager(ViewManager viewManager) {
    this.viewManager = viewManager;
  }
  public void setNotificationManager(NotificationManager notificationManager) {
    this.notificationManager = notificationManager;
  }
  public void setI18nManager(I18nManager i18nManager) {
    this.i18nManager = i18nManager;
  }
  public void setAttachmentRendererManager(AttachmentRendererManager attachmentRendererManager) {
    this.attachmentRendererManager = attachmentRendererManager;
  }
  public void setComponentFactories(ComponentFactories componentFactories) {
    this.componentFactories = componentFactories;
  }
  public void setLoginHandler(LoginHandler loginHandler) {
    this.loginHandler = loginHandler;
  }
  public void setWorkflowDefinitionConversionFactory(WorkflowDefinitionConversionFactory workflowDefinitionConversionFactory) {
    this.workflowDefinitionConversionFactory = workflowDefinitionConversionFactory;
  }
  public List<String> getAdminGroups() {
    return adminGroups;
  }
  public void setAdminGroups(List<String> adminGroups) {
    this.adminGroups = adminGroups;
  }
  public List<String> getUserGroups() {
    return userGroups;
  }
  public void setUserGroups(List<String> userGroups) {
    this.userGroups = userGroups;
  }
  public SimpleWorkflowJsonConverter getSimpleWorkflowJsonConverter() {
	  return simpleWorkflowJsonConverter;
  }
  public void setSimpleWorkflowJsonConverter(SimpleWorkflowJsonConverter simpleWorkflowJsonConverter) {
	  this.simpleWorkflowJsonConverter = simpleWorkflowJsonConverter;
  }

	@WebServlet(urlPatterns = "/webjars/*")
	public static class WebjarServlet extends HttpServlet {
		private RequestDispatcher defaultDispatcher;

		@Override
		public void init() throws ServletException {
			super.init();
			defaultDispatcher = getServletContext().getNamedDispatcher("default");
		}

		@Override
		protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			defaultDispatcher.forward(request, response);
		}
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = ExplorerApp.class, productionMode = false)
	public static class MyUIServlet extends SpringVaadinServlet {
		@Override
		protected void servletInitialized() throws ServletException {
			super.servletInitialized();

			VaadinServletService.getCurrent().addSessionInitListener(new SessionInitListener() {

				@Override
				public void sessionInit(SessionInitEvent event) throws ServiceException {

					VaadinSession.getCurrent().addBootstrapListener(new BootstrapListener() {

						@Override
						public void modifyBootstrapFragment(BootstrapFragmentResponse response) {}

						@Override
						public void modifyBootstrapPage(BootstrapPageResponse response) {
							Document document = response.getDocument();
							Element head = document.getElementsByTag("head").get(0);

							Object visjsVersion = ResourceBundle.getBundle("app").getObject("visjs.version");

							addWebJarResource(document, head, String.format("visjs/%s/vis.min.css", visjsVersion));
							addWebJarResource(document, head, String.format("visjs/%s/vis.min.js", visjsVersion));
						}

						private void addWebJarResource(Document d, Element head, String id) {
							if (id.endsWith(".js")) {
								Element s = d.createElement("script");
								s.attr("src", "webjars/" + id);
								s.attr("type", "text/javascript");
								head.appendChild(s);
							} else if (id.endsWith(".css")) {
								Element s = d.createElement("link");
								s.attr("href", "webjars/" + id);
								s.attr("rel", "stylesheet");
								head.appendChild(s);
							}
						}
					});
				}
			});
		}
	}

  @WebListener
  public static class MyContextLoaderListener extends ContextLoaderListener {
  @Override
  public WebApplicationContext initWebApplicationContext(
  		ServletContext servletContext) {

  	servletContext.setInitParameter(CONFIG_LOCATION_PARAM, "/WEB-INF/root-context.xml");
  	return super.initWebApplicationContext(servletContext);
  }



  }

public boolean isShowingLoginPage() {
	// TODO Auto-generated method stub
	return showingLoginPage;
}

public void switchView(Component component) {
    mainLayout.setMainContent(component);
  }

  public void setMainNavigation(String navigation) {
    mainLayout.setMainNavigation(navigation);
  }

public Repository getMashRepository() {
	return mashRepository;
}






}
