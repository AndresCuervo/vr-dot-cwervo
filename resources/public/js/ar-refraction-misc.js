var guiVars = {
    refIndex : 0.9,
    distance : 1
}


window.onload = function() {
    var gui = new dat.GUI()
    var torus = document.getElementById('clearTorus')
    var refractionIndexController = gui.add(guiVars, 'refIndex', 0, 1, 0.01)

    refractionIndexController.onChange(function (value) {
        torus.components['refraction-shader'].data.refractionIndex = value
    });

    var distController= gui.add(guiVars, 'distance', 0, 4, 0.05)

    distController.onChange(function (value) {
        torus.components['refraction-shader'].data.distance = value
    });
};
