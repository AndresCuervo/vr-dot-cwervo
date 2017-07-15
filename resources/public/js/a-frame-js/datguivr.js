AFRAME.registerComponent('dat-gui', {
    schema : {
        enableMouse : {default : false},
        gazeInput : {default : false},
        query : {type : 'selectorAll'},
        // An array of IDs to attach onto
        objects : {type : 'array'}
    },
    init : function() {
        function warningMessage(... args) {
            console.warn("Dat-gui component: ", ...args);
        }
        if (!this.data.query) {
            // console.warn("Dat-gui-controller: no query, please add one");
            // return
        }

        // if (!window.dat) {
        //     warningMessage("oh no");
        //     var script = document.createElement('script');
        //     script.src = 'https://cdn.rawgit.com/dataarts/dat.guiVR/master/build/datguivr.min.js';
        //     document.body.appendChild(script);
        // }

        scene = this.el.sceneEl.object3D;
        camera = this.el.camera;
        renderer = this.el.renderer;

        if (this.data.enableMouse) {
            dat.GUIVR.enableMouse( camera, renderer );
        }

        if (this.gazeInput) {
            var gazeInput = dat.GUIVR.addInputObject( camera );
            scene.add( gazeInput.cursor );
        }


        for (var i = 0, len = this.data.objects.length; i < len; i++) {
            var thisObjectId = this.data.objects[i];

            var gui = dat.GUIVR.create(thisObjectId);
            gui.position.x = -2 + (i * 1.1);
            gui.position.y = 2;
            gui.position.z = -1;

            console.log("selector ::", thisObjectId);
            var thisObject = document.getElementById(thisObjectId).object3D;

            // Try multiple : true, and give the schema a
            ["x", "y", "z"].forEach(function(axis) {
                gui.add(thisObject.scale, axis, 0, 10);
            });

            gui.name = thisObjectId + "GUI";
            scene.add(gui);
        }

        this.data.query.forEach(function (controllerEl) {
            var object3D = controllerEl.object3D;
            // https://github.com/dataarts/dat.guiVR/wiki/Input-Support-(Vive-Controllers,-Mouse,-etc)
            var vrInput = dat.GUIVR.addInputObject( object3D );

            ['trigger', 'trackpad', 'grip'].forEach(function (baseEvent) {
                ['up', 'down'].forEach(function (e) { controllerEl.addEventListener(baseEvent + e, function(){
                    var gripEvent = baseEvent === 'grip';
                    console.log((gripEvent ? 'gripped' : 'pressed') + " " + controllerEl + " " + e);
                    var value = (e === "down");
                    (gripEvent ? vrInput.gripped(value) : vrInput.pressed(value));
                })})
            });

            scene.add(vrInput);
        });
    }
});
