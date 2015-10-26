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

    var container = null;
    var options = {
        nodes: {
            color: gray,
            font: {
                color: gray,
                strokeColor: white,
                strokeWidth: 2,
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
                strokeWidth: 2,
            },
        },
        groups: {
            AGE: {
                shape: 'circle',
                color: {
                    background: lightYellow,
                    border: lightYellow,
                },
            },
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
            FEMALES_UNDERAGE: {
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
            },
            MALES_UNDERAGE: {
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
            },
            PERSONS: {
                icon: {
                    code: '\uf007',
                },
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
                //springConstant: 0.01,
                //springLength: 100,
                //damping: 0.4,
                //avoidOverlap: 0,
            },
        },
    }

    var nodes = new vis.DataSet([]);
    var edges = new vis.DataSet([]);
    var data = {
        nodes: nodes,
        edges: edges
    };

    var network = null;
    var hoverEdgeID = null
    var selectionEdgeIDs = {};

    function showHoverLabel(edgeID) {
        if (hoverEdgeID != null) {
            hideHoverLabel(hoverEdgeID);
        }
        hoverEdgeID = edgeID;
        showEdgeLabel(edgeID);
    }

    function hideHoverLabel(edgeID) {
        if (selectionEdgeIDs.indexOf(edgeID) < 0) {
            hideEdgeLabel(edgeID);
        }
    }

    function showEdgeLabel(edgeID) {
        edges.update({
            id: edgeID,
            font: {
                size: 12,
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

    function toggleSelectionLabels(nodeIDs, edgeIDs) {
        hoverEdgeID = null;
        if (selectionEdgeIDs.length > 0) {
            selectionEdgeIDs.map(function (edgeID) {
                hideEdgeLabel(edgeID);
            });
        }
        console.log(edgeIDs);
        edgeIDs.map(function (edgeID) {
            console.log(edgeID);
            showEdgeLabel(edgeID);
        });
        selectionEdgeIDs = edgeIDs;
    }

    var self = this;
    container = self.getElement();

    // Handle changes in Vaadin layout
    self.addResizeListener(self.getElement(), function () {
        network.redraw();
    });

    // Handle changes from the server-side
    self.onStateChange = function () {
        if (network != null) {
            network.destroy();
        }

        nodes = new vis.DataSet(self.getState().nodes);
        edges = new vis.DataSet(self.getState().edges);
        data = {
            nodes: nodes,
            edges: edges,
        };
        network = new vis.Network(container, data, options);

        network.on("hoverEdge", function (params) {
            showHoverLabel(params.edge);
        });
        network.on("blurEdge", function (params) {
            hideHoverLabel(params.edge);
        });
        network.on("select", function (params) {
            toggleSelectionLabels(params.nodes, params.edges);
        });
        network.on('selectNode',function(params){
            // Server callback
            self.onSelectNode(params);
        });

        network.redraw();
    }
}
