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
package org.activiti.explorer.ui.search;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.data.LazyLoadingContainer;
import org.activiti.explorer.data.LazyLoadingQuery;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.custom.DetailPanel;
import org.activiti.explorer.ui.custom.PopupWindow;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.activiti.explorer.ui.search.SearchFormEvent;
import org.activiti.explorer.ui.search.util.Decorator;
import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;

import com.mash.data.service.Query;
import com.mash.data.service.Repository;
import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Person;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * @author Joram Barrez
 */
public class SearchDetailPanel extends DetailPanel {

	private static final long serialVersionUID = 1L;

	protected ProcessDefinition processDefinition;
	protected I18nManager i18nManager;

	protected VerticalLayout detailContainer;
	protected VerticalLayout processDefinitionStartForm;

	protected Map<String, String> savedFormProperties;

	private LazyLoadingQuery lazyLoadingQuery;
	private LazyLoadingContainer taskListContainer;
	private Repository repository;
	private Table personTable;
	private Table locationTable;
	private boolean isLocation;

	private HorizontalLayout resultsContainer;
	private VerticalLayout treeContainer;
	private SearchTabEventListener tabEventListener;

	private PopupWindow parent;

	public SearchDetailPanel(PopupWindow parent) {
		this.parent = parent;
		this.i18nManager = ExplorerApp.get().getI18nManager();
		this.repository = ExplorerApp.get().getMashRepository();
		initUi();
	}


	protected void initUi() {
		addStyleName("search-detail-panel");

		detailContainer = new VerticalLayout();
		detailContainer.setId("detail-container");
		detailContainer.setMargin(true);
		detailContainer.setSizeFull();
		setDetailContainer(detailContainer);

		initForm();
		initResults();
	}

	private void initResults() {
		resultsContainer = new HorizontalLayout();
		resultsContainer.setSizeFull();
		resultsContainer.setSpacing(true);

		personTable = createPersonTable();
		locationTable = createLocationTable();
		locationTable.addValueChangeListener(getListSelectionListener());
		personTable.addValueChangeListener(getListSelectionListener());

		resultsContainer.addComponent(locationTable);
		locationTable.setSizeFull();
		resultsContainer.addComponent(personTable);
		personTable.setSizeFull();

		resultsContainer.setVisible(false);
		detailContainer.addComponent(resultsContainer);
		detailContainer.setExpandRatio(resultsContainer, 1);
	}

	protected void initForm() {
		SearchForm search = new SearchForm();
		search.addListener(new SearchRequestEventListener());

		search.setWidth(100, Unit.PERCENTAGE);
		search.setHeightUndefined();
		detailContainer.addComponent(search);
		detailContainer.setExpandRatio(search, 0);
	}


	public class SearchRequestEventListener extends SearchFormEventListener {

		@Override
		protected void handleFormSubmit(SearchFormEvent event) {
			Query query = event.getQuery();
			if (!isLocationQuery(query)) {
				locationTable.setVisible(false);
				List<Person> results;

				try {
					results = repository.findPersons(query, null);
					appendResults(results, personTable);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				fireEvent(new SearchTabEvent(SearchDetailPanel.this, SearchTabEvent.TYPE_CLEAR, isLocation, null));

			} else {
				personTable.setVisible(false);
				List<Location> results;
				try {
					results = repository.findLocations(query.getSampleLocation(), null);
					appendResults(results, locationTable);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}


		private boolean isLocationQuery(Query query) {
			isLocation = (query.getLastName() == null || query.getLastName().length() == 0) &&
					(query.getFirstName() == null || query.getFirstName().length() == 0) &&
					query.getDateOfBirthFrom() == null && query.getDateOfBirthTo() == null && query.getGender() ==
					null;
			return isLocation;

		}

		@Override
		protected void handleFormCancel(SearchFormEvent event) {
			ExplorerApp.get().showNotification("Cancel", "Cancel");

		}


	}

	protected Table createPersonTable() {
		Table personTable = new Table();
		personTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		personTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
		personTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_EXPLICIT_DEFAULTS_ID);

		// Create column header
		personTable.addGeneratedColumn("", new ThemeImageColumnGenerator(Images.TASK_22));

		personTable.addContainerProperty("Name", String.class, null);
		personTable.addContainerProperty("Address", String.class, null);
		personTable.addContainerProperty("DOB", String.class, null);

		return personTable;
	}

	protected Table createLocationTable() {
		Table locationTable = new Table();
		locationTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		locationTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
		locationTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_EXPLICIT_DEFAULTS_ID);

		// Create column header
		locationTable.addGeneratedColumn("", new ThemeImageColumnGenerator(Images.TASK_22));

		locationTable.addContainerProperty("Address", String.class, null);
		locationTable.addContainerProperty("Postcode", String.class, null);

		return locationTable;
	}

	private void appendResults(List<? extends Entity> results, Table table) {

		table.removeAllItems();
		for (Entity entity : results) {
			table.addItem(Decorator.getTableRow(entity), entity.getId());
		}
		resultsContainer.setVisible(true);
		table.setVisible(true);
		if (results.size() < 10) {
			table.setPageLength(results.size());
		}
	}

	private LazyLoadingQuery createLazyLoadingQuery() {
		// TODO Auto-generated method stub
		return null;
	}


	protected ValueChangeListener getListSelectionListener() {
		return new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
				String id = (String) event.getProperty().getValue();
				if (id != null) {
					fireEvent(new SearchTabEvent(SearchDetailPanel.this, SearchTabEvent.TYPE_SELECT, isLocation, id));
					parent.close();
				}

			}
		};
	}


}
