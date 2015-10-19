window.mash_graph_Network = function () {

    // create an array with nodes
    //var nodes = new vis.DataSet([]);
    var nodes = [{
        id: 1,
        label: 'User 1',
        group: 'PERSONS'
        //}, {
        //    id: 2,
        //    label: 'User 2',
        //    group: 'PERSONS'
    }, {
        id: 3,
        label: 'Usergroup 1',
        group: 'LOCATIONS'
        //}, {
        //    id: 4,
        //    label: 'Usergroup 2',
        //    group: 'LOCATIONS'
        //}, {
        //    id: 5,
        //    label: 'Organisation 1',
        //    shape: 'LOCATIONS',
    }];

    // create an array with edges
    //var edges = new vis.DataSet([]);
    var edges = [{
        from: 1,
        to: 3
        //}, {
        //    from: 1,
        //    to: 4
        //}, {
        //    from: 2,
        //    to: 4
        //}, {
        //    from: 3,
        //    to: 5
        //}, {
        //    from: 4,
        //    to: 5
    }];

    var container = this.getElement();

    // provide the data in the vis format
    var data = {
        nodes: nodes,
        edges: edges
    };

    var element = this.getElement();
    var options = {
        width: element.offsetWidth,
        height: element.offsetHeight,
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

    var network = new vis.Network(container, data, options);

    // Handle changes from the server-side
    this.onStateChange = function () {

        nodes = new vis.DataSet(this.getState().nodes);
        edges = new vis.DataSet(this.getState().edges);
        data = {
            nodes: nodes,
            edges: edges,
        };
        network.setData(data);

        network.setOptions({
            width: element.offsetWidth,
            height: element.offsetHeight,
        });

        network.redraw();
    }
}
