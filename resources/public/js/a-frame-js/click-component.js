AFRAME.registerComponent('change-color-on-click', {
    // Could use a schem to preserve the color! then simply change it on update
    // if clicked?
    init: function () {
        var COLORS = [
            'pink',
            'blue',
            'yellow',
            'red',
            'peachpuff',
            '#2EAFAC',
            '#BAE'];
        this.el.addEventListener('mouseenter', function (evt) {
            var randomIndex = Math.floor(Math.random() * COLORS.length);
            var newColor = COLORS[randomIndex];
            this.setAttribute('material', 'color', newColor);
            console.log('Boop: I was clicked at: ', evt.detail.intersection.point, "and my new color is: ", newColor);
        });
    }

    // Maybe do a like .tick thing
    // https://aframe.io/docs/0.5.0/core/component.html#tick-time-timedelta
    // where after a certain number of ticks (e.g. amount of time), you have an event progress state?
});
