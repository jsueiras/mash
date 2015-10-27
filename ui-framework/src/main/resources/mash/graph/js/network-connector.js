window.mash_graph_Network = function () {
    var gray = 'rgb(106,116,124)';
    var white = 'rgb(255,255,255)';
    var blue = 'rgb(11,87,163)';
    var green = 'rgb(84,183,78)';
    var red = 'rgb(236,66,60)';
    var yellow = 'rgb(245,143,49)';
    var lightBlue = 'rgb(211,216,236)';
    var lightGreen = 'rgb(220,237,213)';
    var lightRed = 'rgb(251,215,201)';
    var lightYellow = 'rgb(253,229,204)';

    var edgeFontSize = 12;
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
                    border: lightRed,
                    highlight: {
                        border: red,
                    },
                },
                font: {
                    color: red,
                },
                icon: {
                    code: '\uf182',
                    color: red,
                },
            },
            FEMALES_UNEXPLORED: {
                color: {
                    border: lightRed,
                    highlight: {
                        border: red,
                    },
                },
                font: {
                    color: red,
                },
                icon: {
                    code: '\uf182',
                    color: lightRed,
                },
                shadow: false,
            },
            MALES: {
                color: {
                    border: lightBlue,
                    highlight: {
                        border: blue,
                    },
                },
                font: {
                    color: blue,
                },
                icon: {
                    code: '\uf183',
                    color: blue,
                },
            },
            MALES_UNEXPLORED: {
                color: {
                    border: lightBlue,
                    highlight: {
                        border: blue,
                    },
                },
                font: {
                    color: blue,
                },
                icon: {
                    code: '\uf183',
                    color: lightBlue,
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
                    border: lightGreen,
                    highlight: {
                        border: green,
                    },
                },
                font: {
                    color: green,
                },
                icon: {
                    color: lightGreen,
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
        if (selectionEdgeIds.indexOf(edgeId) < 0) {
            hideEdgeLabel(edgeId);
        }
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
            if (selectionEdgeIds.indexOf(edgeId) < 0) {
                hideEdgeLabel(edgeId);
            }
        });
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
        hoverEdgeId = null;
        selectionEdgeIds.forEach(function (edgeID) {
            hideEdgeLabel(edgeID);
        });
        console.log(edgeIDs);
        edgeIDs.forEach(function (edgeID) {
            console.log(edgeID);
            showEdgeLabel(edgeID);
        });
        selectionEdgeIds = edgeIDs;
    }

    // draw badges after vis.js finishes drawing
    function onAfterDrawing(canvasContext) {
        nodes.forEach(function (node) {
            console.log(node);
            if (node.age > -1) {
                var nodePosition = network.getPositions([node.id]);
                var x = nodePosition[node.id].x + 20;
                var y = nodePosition[node.id].y - 20;

                canvasContext.lineWidth = fontStrokeWidth;
                //canvasContext.strokeStyle = gray;
                //canvasContext.fillStyle = gray;
                //canvasContext.circle(x, y, 10);
                //canvasContext.fill();
                //canvasContext.stroke();

                canvasContext.fillStyle = white;
                canvasContext.font = "" + edgeFontSize + "px 'Open Sans'";
                canvasContext.fillText("" + node.age, nodePosition[node.id].x, nodePosition[node.id].y);
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
