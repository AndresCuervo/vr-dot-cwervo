AFRAME.registerComponent("model-texture", {
    schema : {
        src : {default : ""},
        color : {default : "blue"},
    },
    init : function () {
        if (this.data.src !== "") {
            var obj3D = this.el.object3D;
            // this.material = this.el.getOrCreateObject3D('mesh').material = new THREE.ShaderMaterial();

            var mat  = new THREE.MeshStandardMaterial();

            // var mat = this.el.components.material.material;

            // console.log("material :::", mat);
            // console.log("src:::",this.data.src);
            console.log("this material", this.el.components.material);


            var newMat = new THREE.MeshLambertMaterial({
                map: THREE.ImageUtils.loadTexture(this.data.src)
            });

            this.el.components.material.material = newMat;
            console.log("obj3D", obj3D);
            // mat.specularMap = new THREE.TextureLoader(this.data.src);
            // mat.specular = new THREE.Color(0xff0000);
            // mat.shininess = 2;

            // mat.aoMap = new THREE.TextureLoader(this.data.src);
            // mat.envMap = new THREE.TextureLoader("/assets/textures/Arches_E_PineTree_3k.jpg");

        } else {
            console.warn("Blank spec-map src, pls add one.")
        }
    }
})

AFRAME.registerComponent("load-thing", {
    schema : {
        src : {default : ""},
        color : {default : "blue"},
    },
    init : function () {
        var scene = this.el.sceneEl.object3D;
        var loader = new THREE.PLYLoader();
        loader.load( '/assets/models/miami-ply/miami.ply', function ( geometry ) {

            geometry.computeVertexNormals();

            // var material = new THREE.MeshStandardMaterial( { color: 0x0055ff, shading: THREE.FlatShading } );
            var material = new THREE.MeshPhongMaterial( { map: THREE.ImageUtils.loadTexture('http://www.cartania.com/photos/images/rainbow.jpg') } );
            var mesh = new THREE.Mesh( geometry, material );

            mesh.position.y = - 0.2;
            mesh.position.z =   0.3;
            mesh.rotation.x = - Math.PI / 2;
            mesh.scale.multiplyScalar( 1 );

            mesh.castShadow = true;
            mesh.receiveShadow = true;

            console.log("scene le?", scene);
            scene.add( mesh );

        } );
    }
})

