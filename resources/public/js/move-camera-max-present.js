AFRAME.registerComponent("move-camera", {
    init : function() {
        const camera = this.el.object3D.children[0];
        this.data.camera = camera;
        camera.setFocalLength(21);
        // console.log("camera focal length", camera.getFocalLength());
    },
    tick : function() {
        // this.data.camera.position.setX(Math.sin(performance.time % 5));
    }
});


/*

AFRAME.registerComponent("offset-plane", {
    schema : {
        n : {default : 0}
    },
    init : function() {

        // var mat = this.el.components['material'].shader.material;
        this.data.mat = this.el.components['material'];
        this.data.matOffset = this.el.components['material'].data.offset;
        console.log("material?", this.data.mat);
        console.log("shader?", this.data.mat.shader);

        // TODO : Give up on texture wrapping

        // TODO TODO : Maybe wrap this in a shader

        // console.log(".color?", mat.color);
        // console.log(".map?", mat.defines);
        // Object.keys(mat).forEach(key => console.log(key + ":" + mat[key]));
            // .material.map.offset, this.data.n);
    },
    tick : function() {
        // console.log(this.data.mat.data.offset);
        this.data.mat.needsUpdate = true;
    }
});

*/
