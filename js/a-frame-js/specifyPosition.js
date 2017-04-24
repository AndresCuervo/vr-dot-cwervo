AFRAME.registerComponent('specify-position', {
    // Could use a schem to preserve the color! then simply change it on update
    // if clicked?
    init: function () {
        var currentPosition = this.el.object3D.position;

        this.el.setAttribute('position', {
            x: currentPosition.x,
            y: 0,
            z: currentPosition.z
        });
        // TODO add attributes to specify from component syntax,
        // and whether or not to clear the current position or not!
    }
});

