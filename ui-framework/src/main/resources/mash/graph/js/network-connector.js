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
                //gravitationalConstant: -50,
                //centralGravity: 0.01,
                //springConstant: 0.08,
                //springLength: 100,
                //damping: 0.4,
                //avoidOverlap: 0,
            },
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
            onBlurEdge(hoverEdgeId);
        }
        if (hoverNodeId != null) {
            onBlurNode(hoverNodeId);
        }
    }

    function onHoverEdge(edgeId) {
        blurLastHover();

        hoverEdgeId = edgeId;
        showEdgeLabel(edgeId);
    }

    function onBlurEdge(edgeId) {
        hideEdgeLabel(edgeId);
        showLabels(network.getSelectedNodes(), network.getSelectedEdges());
    }

    function onHoverNode(nodeId) {
        blurLastHover();

        hoverNodeId = nodeId;
        network.getConnectedEdges(nodeId).forEach(function (edgeId) {
            showEdgeLabel(edgeId);
        });
    }

    function onBlurNode(nodeId) {
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

    function onSelect(nodeIDs, edgeIDs) {
        blurLastHover();
        hoverEdgeId = null;

        selectionEdgeIds.forEach(function (edgeID) {
            hideEdgeLabel(edgeID);
        });
        showLabels(nodeIDs, edgeIDs)
        selectionEdgeIds = edgeIDs;
    }

    // draw before vis.js finishes drawing
    function onBeforeDrawing(canvasContext) {
    }

    // draw after vis.js finishes drawing
    function onAfterDrawing(canvasContext) {
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

    // Handle changes in Vaadin layout
    self.addResizeListener(container, function () {
        network.redraw();
    });

    self.add = function (newNodes, newEdges) {
        nodes.add(newNodes);
        edges.add(newEdges);
    }

    self.selectNodes = function (nodeIds) {
        network.selectNodes(nodeIds);

        // programmatic selection does not fire an event
        onSelect(network.getSelectedNodes(), network.getSelectedEdges());
    }

    // Handle changes from the server-side
    self.onStateChange = function () {
        if (network != null) {
            network.destroy();
        }

        var state = self.getState();
        nodes = new vis.DataSet(state.nodes);
        edges = new vis.DataSet(state.edges);
        data = {
            nodes: nodes,
            edges: edges,
        };
        network = new vis.Network(container, data, options);

        network.on("hoverEdge", function (params) {
            onHoverEdge(params.edge);
        });
        network.on("blurEdge", function (params) {
            onBlurEdge(params.edge);
        });
        network.on("hoverNode", function (params) {
            onHoverNode(params.node);
        });
        network.on("blurNode", function (params) {
            onBlurNode(params.node);
        });
        network.on("select", function (params) {
            onSelect(params.nodes, params.edges);
        });
        network.on("dragStart", function (params) {
            onSelect(params.nodes, params.edges);
        });

        network.on("beforeDrawing", function (canvasContext) {
            onBeforeDrawing(canvasContext);
        });
        network.on("afterDrawing", function (canvasContext) {
            onAfterDrawing(canvasContext);
        });

        network.on('selectNode', function (params) {
            // Server callback
            self.onSelectNode(params);
        });

        network.redraw();
    };
}
