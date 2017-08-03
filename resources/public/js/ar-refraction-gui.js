AFRAME.registerComponent('add-refraction-shader-gui', {
    init : function () {

            var gui = new dat.GUI()
            var torus = document.getElementById('clearTorus')

            gui.add(torus.components['refraction-shader'].data, 'refractionIndex', 0, 1, 0.01)
            gui.add(torus.components['refraction-shader'].data, 'distance', 0, 4, 0.05)
            gui.add(torus.object3D.rotation, 'x', 0, 90)
            // gui.addColor(torus.components['refraction-shader'].data, 'tintColor')
            gui.add(torus.components['refraction-shader'].data, 'opacity', 0, 1, 0.01)
    }
})
