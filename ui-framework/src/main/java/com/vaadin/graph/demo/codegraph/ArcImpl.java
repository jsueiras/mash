package com.vaadin.graph.demo.codegraph;

import com.vaadin.graph.Arc;

public class ArcImpl extends GraphElementImpl implements Arc {

    public ArcImpl(String id) {
      this(id, id);
    }

    public ArcImpl(String id, String label) {
      super(id, label);
    }
  }

