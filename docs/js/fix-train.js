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


var globalGui = {
    lineColor : new THREE.Color(0,0,1),
    tubeRadius : 0.0125,
    craneWidth : 0.5
}

function connect_tube (geometry, delta, maxSize, referenceVertices, sceneEl, position) {
    var container = document.createElement('a-entity');
    container.className += " tubeContainer";
    for (var i = 0; i < maxSize - delta; i++) {
        var tube = document.createElement('a-tube');
        tube.className += " tube";
        var pathString =  Object.values(referenceVertices[i]).join(" ") + ", " + Object.values(referenceVertices[i + delta]).join(" ");
        tube.setAttribute('path', pathString);
        tube.setAttribute('crane-shader', 'color: blue;');
        // console.log(pathString);
        tube.setAttribute('radius', globalGui.tubeRadius);
        // tube.setAttribute('position', Math.floor(Math.random(3) * 5) + 0 + " -4");
        // tube.object3D.position.set(this.data.position.x, 0, this.data.position.z)
        container.appendChild(tube);
    }
    container.setAttribute('position', position.x + " 0 "+ position.z);
    sceneEl.appendChild(container);

    // make little part of the crane

    // TODO should definitely make this for loop a helper function ...
    var c2 = document.createElement('a-entity');
    c2.className += " tubec2";
    for (var i = 0; i < maxSize - delta; i++) {
        var tube = document.createElement('a-tube');
        tube.className += " tube";
        var pathString =  Object.values(referenceVertices[i]).join(" ") + ", " + Object.values(referenceVertices[i + delta]).join(" ");
        tube.setAttribute('path', pathString);
        tube.setAttribute('crane-shader', 'color: blue;');
        // console.log(pathString);
        tube.setAttribute('radius', globalGui.tubeRadius);
        // tube.setAttribute('position', Math.floor(Math.random(3) * 5) + 0 + " -4");
        // tube.object3D.position.set(this.data.position.x, 0, this.data.position.z)
        c2.appendChild(tube);
    }
    // c2.setAttribute('position', position.x + " " + maxSize + " "+ position.z);
    c2.setAttribute('position', position.x + " " + (0.5 + referenceVertices[maxSize - delta].y) + " "+ position.z);
    c2.setAttribute('rotation', 90 + " " + 0 + " "+ position.z);
    sceneEl.appendChild(c2);
}

AFRAME.registerComponent("make-crane", {
    multiple : true,
    schema : {
        n : {default : undefined},
        position : {
            type : 'vec3',
            default : undefined
        },
        color: { default : '' },
    },
    init : function () {
        if (!this.data.position) {
            this.data.position =  new THREE.Vector3(
                -7 + Math.floor(Math.random() * 10),
                Math.floor(Math.random() * 10),
                -12 + Math.floor(Math.random() * 10)
            );
        }

        if (!this.data.n) {
            this.data.n = 8 + Math.floor(Math.random() * 40);
        }
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

        var numSides = 4;
        for (var i = 0; i < this.data.n; i++) {
            var scaleY = 0.2;
            var y;

            if (i < 4){                             // we are in the base
                y = 0;
            } else if (i < this.data.n - 4) {       // in the middle
                y = (i - 4) * scaleY;
            } else {                                // we are in the top
                y = (this.data.n - 7) * scaleY;
            }

            if (i % numSides == 0) {
                dotGeometry.vertices.push(new THREE.Vector3( 0, y, 0));
            } else if (i % numSides == 1) {
                dotGeometry.vertices.push(new THREE.Vector3( globalGui.craneWidth, y, 0));
            } else if (i % numSides == 2) {
                dotGeometry.vertices.push(new THREE.Vector3( globalGui.craneWidth, y, globalGui.craneWidth));
            } else if (i % numSides == 3) {
                dotGeometry.vertices.push(new THREE.Vector3( 0, y, globalGui.craneWidth));
            }
        }

        // var dot = new THREE.Points( dotGeometry, dotMaterial );
        // dot.name = "DOT"
        // dot.position.set( this.data.position.x, 0, this.data.position.z);
        // scene.add( dot );

        var scene = this.el.sceneEl.object3D;
        var geometry = new THREE.Geometry();

        connect_tube(geometry, 1, this.data.n, dotGeometry.vertices, this.el.sceneEl, this.data.position);
        connect_tube(geometry, 3, this.data.n, dotGeometry.vertices, this.el.sceneEl, this.data.position);
        connect_tube(geometry, 4, this.data.n, dotGeometry.vertices, this.el.sceneEl, this.data.position);
    },
    tick : function () {
        // this.obj.rotation.z += 0.05;
    }
});


// Based off of: https://aframe.io/docs/0.5.0/components/material.html#registershader

const craneVertexShader = `
  // vertex.glsl
  // varying vec2 vUv;
  varying vec4 thisPosition;

  void main() {
    // vUv = uv;
    thisPosition = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );
    gl_Position = thisPosition;
  }
  `;

const craneFragmentShader = `
  // fragment.glsl
  varying vec2 vUv;
  uniform vec3 color;
  uniform float time;
  varying vec4 thisPosition;

    vec3 hsl2rgb(vec3 HSL) {
        float R = abs(HSL.x * 6.0 - 3.0) - 1.0;
        float G = 2.0 - abs(HSL.x * 6.0 - 2.0);
        float B = 2.0 - abs(HSL.x * 6.0 - 4.0);
        vec3 RGB = clamp(vec3(R,G,B), 0.0, 1.0);
        float C = (1.0 - abs(2.0 * HSL.z - 1.0)) * HSL.y;
        return (RGB - 0.5) * C + HSL.z;
    }

  void main() {
    // cameraPosition is passed from BufferGeometry by default :) --> https://threejs.org/docs/#api/renderers/webgl/WebGLProgram
    // gl_FragColor = vec4(cameraPosition, 1.0);
    float d = thisPosition.z;
    d *= .04;
    d += 0.0625;
    gl_FragColor = vec4(d,d,d,1.0);
    //gl_FragColor = vec4(hsl2rgb(vec3(abs(thisPosition / 10.0 - cameraPosition.x), 1.0, 0.8)), 1.0);

  }
  `;

AFRAME.registerComponent('crane-shader', {
    schema: {color: {type: 'color'}},
    /**
     * Creates a new THREE.ShaderMaterial using the two shaders defined
     * in vertex.glsl and fragment.glsl.
     */
    init: function () {

        const data = this.data;
        this.material  = new THREE.ShaderMaterial({
            uniforms: {
                time: { value: 0.0 },
                color: { value: globalGui.lineColor }
            },
            vertexShader : craneVertexShader,
            fragmentShader : craneFragmentShader,
        });
        this.applyToMesh();
        this.el.addEventListener('model-loaded', () => this.applyToMesh());
    },
    /**
     * Update the ShaderMaterial when component data changes.
     */
    update: function () {
        this.material.uniforms.color.value.set(this.data.color);
    },

    /**
     * Apply the material to the current entity.
     */
    applyToMesh: function() {
        const mesh = this.el.getObject3D('mesh');
        if (mesh) {
            mesh.material = this.material;
        }
    },
    /**
     * On each frame, update the 'time' uniform in the shaders.
     */
    tick: function (t) {
        this.material.uniforms.time.value = t / 1000;
    }

})

const depthVertexShader = THREE.ShaderLib['normal'].vertexShader;

const depthFragmentShader = `
    varying vec3 vNormal;
    uniform float delta;
    uniform float mNear;
    uniform float mFar;
    float PI = 3.14159265358979323846264;
    void main()
    {
        float depth = gl_FragCoord.z / gl_FragCoord.w;
        float depthColor = smoothstep( mNear, mFar, depth );
        gl_FragColor = vec4(abs(sin(delta + 0.7*PI) + cos(normalize(vNormal).x)/2.0) - depthColor,
                            abs(sin(delta + 1.0*PI) + cos(normalize(vNormal).y)/2.0) - depthColor,
                            abs(sin(delta + 1.2*PI) + cos(normalize(vNormal).z)/2.0) - depthColor, 1.0);
    }
  `;

AFRAME.registerComponent('depth-shader', {
    schema: {color: {type: 'color'}},
    /**
     * Creates a new THREE.ShaderMaterial using the two shaders defined
     * in vertex.glsl and fragment.glsl.
     */
    init: function () {

        const data = this.data;
        this.material  = new THREE.ShaderMaterial({
            uniforms: {
                delta: {value: 0.0},
                mNear: {value: 1.0},
                mFar: {value: 60.0},
                color: { value: globalGui.lineColor },
                time: { value: 0.0 },
                // near: { value: 0.5 },
                // far: { value: 1000 }
            },
            vertexShader : depthVertexShader,
            fragmentShader : depthFragmentShader,
        });
        this.applyToMesh();
        this.el.addEventListener('model-loaded', () => this.applyToMesh());
    },
    /**
     * Update the ShaderMaterial when component data changes.
     */
    update: function () {
        this.material.uniforms.color.value.set(this.data.color);
    },

    /**
     * Apply the material to the current entity.
     */
    applyToMesh: function() {
        const mesh = this.el.getObject3D('mesh');
        if (mesh) {
            mesh.material = this.material;
        }
    },
    /**
     * On each frame, update the 'time' uniform in the shaders.
     */
    tick: function (t) {
        this.material.uniforms.time.value = t / 1000;
    }

})
