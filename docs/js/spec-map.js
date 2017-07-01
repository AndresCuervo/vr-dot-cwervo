// material-grid-glitch.js
const vertexShader = `
// vertex.glsl
varying vec2 vUv;
void main() {
  vUv = uv;
  gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );
}
`;
const fragmentShader = `
// fragment.glsl
varying vec2 vUv;
uniform vec3 color;
uniform float time;
void main() {
  // Use sin(time), which curves between 0 and 1 over time,
  // to determine the mix of two colors:
  //    (a) Dynamic color where 'R' and 'B' channels come
  //        from a modulus of the UV coordinates.
  //    (b) Base color.
  //
  // The color itself is a vec4 containing RGBA values 0-1.
  gl_FragColor = mix(
    vec4(mod(vUv , 0.05) * 20.0, 1.0, 1.0),
    vec4(color, 1.0),
    sin(time)
  );
}
`;

AFRAME.registerComponent("spec-map", {
    schema : {
        src : {default : ""},
        color : {default : "blue"},
        shaderOn: {default : false}
    },
    init : function () {
        if (this.data.src !== "") {
            var obj3D = this.el.object3D;
            // this.material = this.el.getOrCreateObject3D('mesh').material = new THREE.ShaderMaterial();

            var mat  = new THREE.MeshStandardMaterial();

            // var mat = this.el.components.material.material;

            console.log("material :::", mat);
            console.log("src:::",this.data.src);

            mat.specularMap = new THREE.TextureLoader(this.data.src);
            mat.specular = new THREE.Color(0xff0000);
            mat.shininess = 2;

            mat.aoMap = new THREE.TextureLoader(this.data.src);
            mat.envMap = new THREE.TextureLoader("/assets/textures/Arches_E_PineTree_3k.jpg");

        } else {
            console.warn("Blank spec-map src, pls add one.")
        }
    }
})

AFRAME.registerShader('light-shader', {
  schema: {
    emissive: {default: '#0F0'},
    wireframe: {default: false}
  }
});
