AFRAME.registerComponent('rand-data-pos', {
    // Could use a schem to preserve the color! then simply change it on update
    // if clicked?
    init: function () {
        var currentPosition = this.el.object3D.position;

        // Adapted from:
        // http://stackoverflow.com/a/32642491/4230661

        var radius = 51; // abstract this out to a variable to pass in!

        // This middle number should be set globally or something, idk, decide on a floor??? Shared component variable?? That'd be best
        this.el.setAttribute('data-pos', generateRandomPosString(radius));
        // TODO add attributes to specify from component syntax,
        // and whether or not to clear the current position or not!
    }
});

function generateRandomPosString (radius) {
    var pt_angle = Math.random() * 2 * Math.PI;
    var pt_radius_sq = Math.random() * radius * radius;
    var pt_x = Math.sqrt(pt_radius_sq) * Math.cos(pt_angle);
    var pt_z = Math.sqrt(pt_radius_sq) * Math.sin(pt_angle);

    // Simplest interpretation of this q:
    // https://stackoverflow.com/questions/481144/equation-for-testing-if-a-point-is-inside-a-circle
    //
    // There are some great optimizations though, to be implemented in the future ¯\_(ツ)_/¯
    // Anyway, this checks to if the generated points are greater than the stayAway radius
    // But, only if stayAway < radius, because otherwise we'd be in an infinite loop
    var stayAway = 50;
    if (stayAway < radius && Math.sqrt(pt_x - 0) + Math.sqrt(pt_z - 0) < Math.sqrt(stayAway)) {
        return generateRandomPosString (radius);
    }

    return pt_x + " 3 " + pt_z;
}
