window.mash_graph_Network = function() {

    // create an array with nodes
    var nodes = new vis.DataSet([]);

    // create an array with edges
    var edges = new vis.DataSet([]);

    var container = this.getElement();

    // provide the data in the vis format
    var data = {
        nodes: nodes,
        edges: edges
    };

    var options = {
        autoResize: true,
        height: '100%',
        width: '100%',
        clickToUse: false,
        configure: {
            enabled: false,
            showButton: true,
        },
        groups: {
            PERSONS: {
                shape: 'icon',
                icon: {
                    face: 'FontAwesome',
                    code: '\uf007',
                    size: 50,
                    color: 'rgb(106,116,124)',
                },
            },
            LOCATIONS: {
                shape: 'icon',
                icon: {
                    face: 'FontAwesome',
                    code: '\uf041',
                    size: 50,
                    color:'rgb(106,116,124)',
                },
            },
        },
    }

    // initialize your network!
    var network = new vis.Network(container, data, options);

    // Handle changes from the server-side
    this.onStateChange = function() {
        data = {
            nodes: new vis.DataSet(this.getState().nodes),
            edges: new vis.DataSet([{}]),
        };
        network.setData(data);
    }
}
