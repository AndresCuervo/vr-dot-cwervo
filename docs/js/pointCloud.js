// Global points so you can inspect 😬
var points;
var flashPoints = false;
AFRAME.registerComponent('make-point-cloud', {
    init: function () {
        if ( ! Detector.webgl ) Detector.addGetWebGLMessage();

        var container, stats;

        var camera, renderer;

        var scene = document.querySelector('a-scene').object3D

        init();
        animate();

        function init() {

            // container = document.getElementById( 'container' );

            //

            // camera = new THREE.PerspectiveCamera( 27, window.innerWidth / window.innerHeight, 5, 3500 );
            // camera.position.z = 2750;
            //
            // scene = new THREE.Scene();
            // scene.fog = new THREE.Fog( 0x050505, 2000, 3500 );

            //
            var g;
            var loader = new THREE.PLYLoader();
            loader.load( '/assets/models/kinectCloud.ply', function ( geometry ) {

                geometry.computeVertexNormals();

                console.log("geometry is:");
                g = geometry.attributes.position.array;
                console.log("g: ", g.length);
                // for ()

                // var material = new THREE.MeshStandardMaterial( { color: 0x0055ff, shading: THREE.FlatShading } );
                // var mesh = new THREE.Mesh( geometry, material );
                //
                // mesh.position.y = - 0.2;
                // mesh.position.z =   0.3;
                // mesh.rotation.x = - Math.PI / 2;
                // // mesh.scale.multiplyScalar( 0.001 );
                //
                // mesh.castShadow = true;
                // mesh.receiveShadow = true;
                //
                // scene.add( mesh );
                var particles = g.length / 3;
                console.log("loading particles ....")

                var geometry = new THREE.BufferGeometry();

                var positions = new Float32Array( particles * 3 );
                var colors = new Float32Array( particles * 3 );

                var color = new THREE.Color();

                // var n = 1000, n2 = n / 2; // particles spread in the cube

                var scale = 300;
                for ( var i = 0; i < positions.length; i += 3 ) {

                    // positions
                    var x = g[i];
                    var y = g[i+1];
                    var z = g[i+2];

                    positions[ i ]     = x * scale;
                    positions[ i + 1 ] = y * scale;
                    positions[ i + 2 ] = z * scale;

                    // colors

                    // var vx = ( x / n ) + 0.5;
                    // var vy = ( y / n ) + 0.5;
                    // var vz = ( z / n ) + 0.5;

                    color.setRGB( 1, 1, 1 );

                    colors[ i ]     = color.r;
                    colors[ i + 1 ] = color.g;
                    colors[ i + 2 ] = color.b;

                }

                geometry.addAttribute( 'position', new THREE.BufferAttribute( positions, 3 ) );
                geometry.addAttribute( 'color', new THREE.BufferAttribute( colors, 3 ) );

                geometry.computeBoundingSphere();

                var material = new THREE.PointsMaterial( { size: 0.5, vertexColors: THREE.VertexColors } );

                points = new THREE.Points( geometry, material );

                scene.add( points );

                points.rotation.z = 3.2;

                container = document.querySelector('body');
                stats = new Stats();
                container.appendChild( stats.dom );

                var checkbox = document.createElement('input');
                var checkboxID = "flashPoints"
                checkbox.type = "checkbox";
                checkbox.name = "flash-points-checkbox";
                checkbox.value = flashPoints;
                checkbox.id = checkboxID;
                checkbox.onclick = function () {
                    checkbox.value = !flashPoints;
                    flashPoints = !flashPoints;
                    points.material.size = 0.5;
                };

                var label = document.createElement('label')
                label.htmlFor = checkboxID;
                label.appendChild(document.createTextNode('flash the size of the points? (warning: flashes v fast, seizure warning!)'));

                checkbox.className += "generated-checkbox";
                label.className += "generated-label";

                container.appendChild(checkbox);
                container.appendChild(label);
            } );

        }


        function animate() {
            requestAnimationFrame( animate );
            if (!!points) {
                render();
                stats.update();
            }
        }

        function render() {
            if (!!points) {
                var time = document.querySelector('a-scene').time * 0.001;
                // points.rotation.x = time * 0.025;
                points.rotation.y = time * 0.05;
                if (flashPoints) {
                    points.material.size = time % 0.5;
                }
                // points.position.x = time * 0.5;

                // renderer.render( scene, camera );
            }
        }


    }
});
