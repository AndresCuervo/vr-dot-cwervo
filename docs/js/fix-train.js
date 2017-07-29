AFRAME.registerComponent("fix-train-rotation", {
    init : function () {
        console.log("hello");
    },
    tick : function () {
        console.log("working?!", this.el.object3D.rotation.x, this.el.object3D.rotation.y);
        this.el.object3D.rotation.x = 0;
        this.el.object3D.rotation.z = 0;
        // this.el.rotation.forEach(function (axis) {
        //     console.log(axis.toString(), axis);
        // });
    }
});

function connect (geometry, delta, maxSize, referenceVertices) {
    for (var i = 0; i < maxSize - delta; i++) {
        geometry.vertices.push(
            referenceVertices[i],
            referenceVertices[i + delta]
        )
    }
}

AFRAME.registerComponent("make-spiral", {
    multiple : true,
    schema : {
        n : {default : undefined},
        position : { type : 'vec3'},
        color: { default : '' },
    },
    init : function () {
        if (Object.values(this.data.position).every(axis => {return axis == 0})) {
            this.data.position =  new THREE.Vector3(
                -7 + Math.floor(Math.random() * 10),
                Math.floor(Math.random() * 10),
                -12 + Math.floor(Math.random() * 10)
            );
        }

        if (!this.data.n) {
            this.data.n = Math.floor(Math.random() * 100);
        }
        console.log("data-n", this.data.n);

        if (!this.data.color) {
            this.data.color = new THREE.Color("hsl(" + Math.floor(Math.random() * 360) + ", 100%, 50%)");
        }

        // console.log("color?", this.data.color);
        var dotGeometry = new THREE.Geometry();

        var dotMaterial = new THREE.PointsMaterial( {
            size: 3,
            color: this.data.color,
            sizeAttenuation: false
        });

        for (var i = 0; i < this.data.n; i++) {
            if (i % 3 == 0) {
                dotGeometry.vertices.push(new THREE.Vector3( 0, i * 0.2, 0));
            } else if (i % 3 == 1) {
                dotGeometry.vertices.push(new THREE.Vector3( 1, i * 0.2, 0));
            } else if (i % 3 == 2) {
                // Magic number explanation:
                // Math.sqrt(1.25) == 1.118033988749895
                // Pythan theorem
                dotGeometry.vertices.push(new THREE.Vector3( 0.5, i * 0.2, 1.118033));
            }
        }

        var dot = new THREE.Points( dotGeometry, dotMaterial );
        dot.name = "DOT"
        dot.position.set( this.data.position.x, 0, this.data.position.z);

        var scene = this.el.sceneEl.object3D;
        scene.add( dot );

        var lines = new THREE.Group();

        var geometry = new THREE.Geometry();
        connect(geometry, 1, this.data.n, dotGeometry.vertices);
        connect(geometry, 4, this.data.n, dotGeometry.vertices);

        var line = new MeshLine();
        line.setGeometry( geometry, function( p ) { return 0.01; } );
        // line.setGeometry( geometry, function( p ) { return 2; } ); // makes width 2 * lineWidth

        // var material = new MeshLineMaterial({color : new THREE.Color( 0xFF0000 )});
        var material = new MeshLineMaterial({color : new THREE.Color(0x2EAFAC)});
        console.log("MeshLineMaterial material", material);
        var mesh = new THREE.Mesh( line.geometry, material ); // this syntax could definitely be improved!
        lines.add( mesh );

        // lines.position.set( this.data.position.x, this.data.position.y, this.data.position.z);
        lines.position.set( this.data.position.x, 0, this.data.position.z);
        scene.add( lines );
        console.log("ugh")
    },
    tick : function () {
        // this.obj.rotation.z += 0.05;
    }
});
