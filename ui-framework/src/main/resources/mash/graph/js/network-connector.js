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

    var container = this.getElement();
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
                size: 12,
                strokeColor: white,
                strokeWidth: 2,
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
        layout: {
            randomSeed: 0,
            hierarchical: false,
        },
        physics: {
            solver: 'forceAtlas2Based',
        },
    }

    var nodes = new vis.DataSet([]);
    var edges = new vis.DataSet([]);
    var data = {
        nodes: nodes,
        edges: edges
    };

    var network = null;

    // Handle changes from the server-side
    this.onStateChange = function () {
        if (network != null) {
            network.destroy();
        }

        nodes = new vis.DataSet(this.getState().nodes);
        edges = new vis.DataSet(this.getState().edges);
        data = {
            nodes: nodes,
            edges: edges,
        };
        network = new vis.Network(container, data, options);

        network.redraw();
    }
}
