package com.vaadin.graph.demo.codegraph;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.graph.GraphExplorer;
import com.vaadin.graph.layout.JungCircleLayoutEngine;
import com.vaadin.graph.layout.JungFRLayoutEngine;
import com.vaadin.graph.layout.JungISOMLayoutEngine;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("codeGraph")
@SpringUI(path = "/graph")
@Widgetset("com.mash.ui_framework.MyAppWidgetset")
public class CodeGraphDemo extends UI {
    private static final long serialVersionUID = 1L;

    private SimpleGraphRepositoryImpl graphRepo;
    private GraphExplorer<?, ?> graph;
    private CssLayout layout;
    
    @Override
    public void init(VaadinRequest request) {
    	  	
    	VerticalLayout content = createContent();
        setContent(content);
        Page.getCurrent().setTitle("Graph Explorer demo");        
    }

	public VerticalLayout createContent() {
		graphRepo = createGraphRepository();  
		VerticalLayout content = new VerticalLayout();
    	layout = new CssLayout();
    	layout.setSizeFull();
    	ComboBox select = createLayoutSelect();
    	content.addComponent(select);
    	content.addComponent(layout);
    	content.setExpandRatio(layout, 1);
    	content.setSizeFull();
    	refreshGraph();
		return content;
	}
    
    private SimpleGraphRepositoryImpl createGraphRepository() {
    	SimpleGraphRepositoryImpl repo = new SimpleGraphRepositoryImpl();
    	repo.addNode("node1", "Node 1").setStyle("root");;
    	repo.setHomeNodeId("node1");
    	
    	repo.addNode("node2", "Node 2").setStyle("blue");
    	repo.addNode("node3", "Node 3");
    	repo.addNode("node4", "Node 4").setIcon(new ThemeResource("icons/48x48/cat_1.png"));

    	repo.addNode("node10", "Node 10");
    	repo.addNode("node11", "Node 11");
    	repo.addNode("node12", "Node 12").setIcon(new ThemeResource("icons/48x48/cat_2.png"));
    	repo.addNode("node13", "Node 13");
    	repo.addNode("node14", "Node 14").setIcon(new ThemeResource("icons/48x48/cat_3.png"));
    	repo.addNode("node15", "Node 15");
    	repo.addNode("node16", "Node 16").setIcon(new ThemeResource("icons/64x64/cat_4.png"));
    	repo.addNode("node17", "Node 17");
    	repo.addNode("node18", "Node 18").setIcon(new ThemeResource("icons/64x64/cat_5.png"));
    	repo.addNode("node19", "Node 19");
    	repo.addNode("node20", "Node 20");
    	repo.addNode("node21", "Node 21");
    	repo.addNode("node22", "Node 22");
    	repo.addNode("node23", "Node 23");
    	repo.addNode("node24", "Node 24");
    	repo.addNode("node25", "Node 25");

    	repo.joinNodes("node1", "node2", "edge12", "Edge 1-2").setStyle("thick-blue");
    	repo.joinNodes("node1", "node3", "edge13", "Edge 1-3").setStyle("thin-red");
    	repo.joinNodes("node3", "node4", "edge34", "Edge 3-4");

    	repo.joinNodes("node2", "node10", "edge210", "Edge type A");
    	repo.joinNodes("node2", "node11", "edge211", "Edge type A");
    	repo.joinNodes("node2", "node12", "edge212", "Edge type A");
    	repo.joinNodes("node2", "node13", "edge213", "Edge type A");
    	repo.joinNodes("node2", "node14", "edge214", "Edge type A");
    	repo.joinNodes("node2", "node15", "edge215", "Edge type A");
    	repo.joinNodes("node2", "node16", "edge216", "Edge type A");
    	repo.joinNodes("node2", "node17", "edge217", "Edge type A");
    	repo.joinNodes("node2", "node18", "edge218", "Edge type A");
    	repo.joinNodes("node2", "node19", "edge219", "Edge type A");
    	repo.joinNodes("node2", "node20", "edge220", "Edge type A");
    	repo.joinNodes("node2", "node21", "edge221", "Edge type A");
    	repo.joinNodes("node2", "node22", "edge222", "Edge type B");
    	repo.joinNodes("node2", "node23", "edge223", "Edge type B");
    	repo.joinNodes("node2", "node24", "edge224", "Edge type B");
    	repo.joinNodes("node2", "node25", "edge225", "Edge type C");
    	
    	return repo;
    }

    private ComboBox createLayoutSelect() {
    	final ComboBox select = new ComboBox("Select layout algorithm");
    	select.addItem("FR");
    	select.addItem("Circle");
    	select.addItem("ISOM");
    	select.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if ("FR".equals(select.getValue())) {
					graph.setLayoutEngine(new JungFRLayoutEngine());
				} else if ("Circle".equals(select.getValue())) {
					graph.setLayoutEngine(new JungCircleLayoutEngine());					
				} if ("ISOM".equals(select.getValue())) {
					graph.setLayoutEngine(new JungISOMLayoutEngine());
				}
				refreshGraph();
			}
		});
    	return select;
    }
    
    private void refreshGraph() {
    	layout.removeAllComponents();
        graph = new GraphExplorer<NodeImpl, ArcImpl>(graphRepo);
        graph.setSizeFull();
        layout.addComponent(graph);
    }

}
