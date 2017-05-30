var scene = new THREE.Scene();
var camera = new THREE.PerspectiveCamera( 75, window.innerWidth/window.innerHeight, 0.1, 1000 );

var renderer = new THREE.WebGLRenderer( { antialias: true } );

var geometry, material, sphere;

function init() {
    renderer.setSize( window.innerWidth, window.innerHeight );
    document.getElementById('container').appendChild( renderer.domElement );

    geometry = new THREE.SphereBufferGeometry(3, 32, 32);

    material = new THREE.MeshPhongMaterial( {
        color: 0x2EAFAC,
        emissive: 0x072534,
    } );

    sphere = new THREE.Mesh( geometry, material );
    scene.add( sphere );

    camera.position.z = 5;

    // Stolen from:
    // https://threejs.org/docs/scenes/geometry-browser.html#SphereGeometry
    var lights = [];
    lights[ 0 ] = new THREE.PointLight( 0xffffff, 1, 0 );
    lights[ 1 ] = new THREE.PointLight( 0xffffff, 1, 0 );
    lights[ 2 ] = new THREE.PointLight( 0xffffff, 1, 0 );

    lights[ 0 ].position.set( 0, 200, 0 );
    lights[ 1 ].position.set( 100, 200, 100 );
    lights[ 2 ].position.set( - 100, - 200, - 100 );

    scene.add( lights[ 0 ] );
    scene.add( lights[ 1 ] );
    scene.add( lights[ 2 ] );
}

function render() {
    requestAnimationFrame( render );

    var scale = 0.001;
    // sphere.rotation.x += scale + window.scrollY / 1000;
    // sphere.rotation.y += scale + window.scrollY / 1000;

    renderer.render(scene, camera);
    sphere.position.setX(10 - window.scrollY / 100);
};

window.onload = function() {
    init();
    render();
    console.log(geometry);
    console.log(geometry.mesh);
}
