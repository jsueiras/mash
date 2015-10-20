window.mash_graph_Network = function () {

    var container = this.getElement();
    var options = {
        width: container.offsetWidth,
        height: container.offsetHeight,
        nodes: {
            shape: 'icon',
        },
        groups: {
            PERSONS: {
                icon: {
                    code: '\uf007',
                },
            },
            LOCATIONS: {
                icon: {
                    code: '\uf041',
                },
            },
        },
        layout: {
            hierarchical: {
                enabled: false,
            },
        },
        physics: {
            forceAtlas2Based: {
                gravitationalConstant: -26,
                centralGravity: 0.005,
                springLength: 230,
                springConstant: 0.18
            },
            maxVelocity: 146,
            solver: 'forceAtlas2Based',
            timestep: 0.35,
            stabilization: {iterations: 150}
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
