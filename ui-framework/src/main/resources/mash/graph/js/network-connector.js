window.mash_graph_Network = function () {
    var white = 'rgb(255,255,255)';

    var gray = 'rgb(105,116,123)';
    var lightGray = 'rgb(215,220,220)';

    var red = 'rgb(248,58,58)';
    var lightRed = 'rgb(255,214,202)';

    var green = 'rgb(55,185,90)';
    var lightGreen = 'rgb(218,237,214)';

    var blue = 'rgb(0,86,159)';
    var lightBlue = 'rgb(211,216,235)';

    var orange = 'rgb(253,141,58)';
    var lightOrange = 'rgb(255,229,205)';

    var edgeFontSize = 12;
    var badgeSize = 14;
    var fontStrokeWidth = 2;

    var options = {
        nodes: {
            color: gray,
            font: {
                color: gray,
                face: 'Open Sans',
                strokeColor: white,
                strokeWidth: fontStrokeWidth,
            },
            shadow: {
                enabled: true,
                size: 4,
                x: 0,
                y: 1,
            },
            shape: 'icon',
        },
        edges: {
            arrows: {
                to: true,
            },
            color: {
                inherit: 'both',
            },
            font: {
                color: gray,
                size: 0,
                strokeColor: white,
                strokeWidth: fontStrokeWidth,
            },
        },
        groups: {
            FEMALES: {
                color: {
                    border: lightOrange,
                    highlight: {
                        border: orange,
                    },
                },
                font: {
                    color: orange,
                },
                icon: {
                    code: '\uf182',
                    color: orange,
                },
            },
            FEMALES_UNEXPLORED: {
                color: {
                    border: lightGray,
                    highlight: {
                        border: gray,
                    },
                },
                font: {
                    color: gray,
                },
                icon: {
                    code: '\uf182',
                    color: lightGray,
                },
                shadow: false,
            },
            MALES: {
                color: {
                    border: lightOrange,
                    highlight: {
                        border: orange,
                    },
                },
                font: {
                    color: orange,
                },
                icon: {
                    code: '\uf183',
                    color: orange,
                },
            },
            MALES_UNEXPLORED: {
                color: {
                    border: lightGray,
                    highlight: {
                        border: gray,
                    },
                },
                font: {
                    color: gray,
                },
                icon: {
                    code: '\uf183',
                    color: lightGray,
                },
                shadow: false,
            },
            LOCATIONS: {
                color: {
                    border: lightGreen,
                    highlight: {
                        border: green,
                    },
                },
                font: {
                    color: green,
                },
                icon: {
                    color: green,
                    code: '\uf015',
                },
            },
            LOCATIONS_UNEXPLORED: {
                color: {
                    border: lightGray,
                    highlight: {
                        border: gray,
                    },
                },
                font: {
                    color: gray,
                },
                icon: {
                    color: lightGray,
                    code: '\uf015',
                },
                shadow: false,
            },
        },
        interaction: {
            hover: true
        },
        layout: {
            randomSeed: 0,
            hierarchical: false,
        },
        physics: {
            solver: 'forceAtlas2Based',
            forceAtlas2Based: {
                damping: 1,
            },
            minVelocity: .5,
        },
    }

    var self = this;

    var nodes = new vis.DataSet([]);
    var edges = new vis.DataSet([]);
    var data = {
        nodes: nodes,
        edges: edges,
    };

    var container = self.getElement();
    var network = null;
    var hoverEdgeId = null;
    var hoverNodeId = null;
    var selectionEdgeIds = [];

    function showLabels(nodeIDs, edgeIDs) {
        edgeIDs.forEach(function (edgeID) {
            showEdgeLabel(edgeID);
        });
    }

    function blurLastHover() {
        if (hoverEdgeId != null) {
            client_onBlurEdge(hoverEdgeId);
        }
        if (hoverNodeId != null) {
            client_onBlurNode(hoverNodeId);
        }
    }

    function client_onHoverEdge(edgeId) {
        blurLastHover();

        hoverEdgeId = edgeId;
        showEdgeLabel(edgeId);
    }

    function client_onBlurEdge(edgeId) {
        hideEdgeLabel(edgeId);
        showLabels(network.getSelectedNodes(), network.getSelectedEdges());
    }

    function client_onHoverNode(nodeId) {
        blurLastHover();

        hoverNodeId = nodeId;
        network.getConnectedEdges(nodeId).forEach(function (edgeId) {
            showEdgeLabel(edgeId);
        });
    }

    function client_onBlurNode(nodeId) {
        network.getConnectedEdges(nodeId).forEach(function (edgeId) {
            hideEdgeLabel(edgeId);
        });
        showLabels(network.getSelectedNodes(), network.getSelectedEdges());
    }

    function showEdgeLabel(edgeID) {
        edges.update({
            id: edgeID,
            font: {
                size: edgeFontSize,
            },
        });
    }

    function hideEdgeLabel(edgeID) {
        edges.update({
            id: edgeID,
            font: {
                size: 0,
            },
        });
    }

    function client_onSelect(nodeIDs, edgeIDs) {
        blurLastHover();
        hoverEdgeId = null;

        selectionEdgeIds.forEach(function (edgeID) {
            hideEdgeLabel(edgeID);
        });
        showLabels(nodeIDs, edgeIDs)
        selectionEdgeIds = edgeIDs;
    }

    // draw before vis.js finishes drawing
    function client_onBeforeDrawing(canvasContext) {
    }

    // draw after vis.js finishes drawing
    function client_onAfterDrawing(canvasContext) {
        nodes.forEach(function (node) {
            var nodePosition = network.getPositions([node.id]);
            var offset = 20;

            if (node.age > -1) {
                var nodePosition = network.getPositions([node.id]);

                canvasContext.lineWidth = fontStrokeWidth;
                canvasContext.fillStyle = white;
                canvasContext.font = "" + edgeFontSize + "px 'Open Sans'";
                canvasContext.fillText("" + node.age, nodePosition[node.id].x, nodePosition[node.id].y);
            }

            if (node.primary) {
                canvasContext.lineWidth = fontStrokeWidth;
                canvasContext.fillStyle = blue;
                canvasContext.font = "" + badgeSize + "px 'FontAwesome'";
                canvasContext.fillText("\uf132", nodePosition[node.id].x - offset, nodePosition[node.id].y - offset);
            }

            if (node.warning) {
                canvasContext.lineWidth = fontStrokeWidth;
                canvasContext.fillStyle = red;
                canvasContext.font = "" + badgeSize + "px 'FontAwesome'";
                canvasContext.fillText("\uf071", nodePosition[node.id].x + offset, nodePosition[node.id].y - offset);
            }
        });
    }

    self.selectNodes = function (nodeIds) {
        network.selectNodes(JSON.parse(nodeIds));

        // programmatic selection does not fire an event
        client_onSelect(network.getSelectedNodes(), network.getSelectedEdges());
    };

    function filterNullTitles() {
        var filteredNodes = [];
        nodes.forEach(function (node) {
            if ("title" in node && node.title == null) {
                delete node.title;
            }
            filteredNodes.push(node);
        });
        nodes.update(filteredNodes);
    }

    self.add = function (newNodes, newEdges) {
        nodes.add(JSON.parse(newNodes));
        edges.add(JSON.parse(newEdges));
        filterNullTitles();
        network.redraw();
    };

    self.updateNodes = function (newNodes) {
        nodes.update(JSON.parse(newNodes));
        filterNullTitles();
        network.redraw();
    };

    // Handle changes in Vaadin layout
    self.addResizeListener(container, function () {
        network.redraw();
    });

    // Handle initialization from the server-side
    self.onStateChange = function () {
        if (network != null) {
            return;
        }

        var state = self.getState();
        nodes = new vis.DataSet(state.nodes);
        edges = new vis.DataSet(state.edges);
        filterNullTitles();
        data = {
            nodes: nodes,
            edges: edges,
        };
        network = new vis.Network(container, data, options);

        network.on("hoverEdge", function (params) {
            client_onHoverEdge(params.edge);
        });
        network.on("blurEdge", function (params) {
            client_onBlurEdge(params.edge);
        });
        network.on("hoverNode", function (params) {
            client_onHoverNode(params.node);
        });
        network.on("blurNode", function (params) {
            client_onBlurNode(params.node);
        });
        network.on("select", function (params) {
            client_onSelect(params.nodes, params.edges);
        });
        network.on("dragStart", function (params) {
            client_onSelect(params.nodes, params.edges);
        });

        network.on("beforeDrawing", function (canvasContext) {
            client_onBeforeDrawing(canvasContext);
        });
        network.on("afterDrawing", function (canvasContext) {
            client_onAfterDrawing(canvasContext);
        });

        ["click", "doubleClick", "oncontext", "hold", "release", "select", "selectNode", "selectEdge", "dragStart", "dragging", "dragEnd"].forEach(function (name) {
            network.on(name, function (properties) {
                var params = {
                    name: name,
                    atNodeId: network.getNodeAt({
                        x: properties.pointer.DOM.x,
                        y: properties.pointer.DOM.y,
                    }),
                    atEdgeId: network.getEdgeAt({
                        x: properties.pointer.DOM.x,
                        y: properties.pointer.DOM.y,
                    }),
                    selectedNodeIds: properties.nodes,
                    selectedEdgeIds: properties.edges,
                };
                self.server_onClick(params);
            });
        });

        network.redraw();
    };
}
