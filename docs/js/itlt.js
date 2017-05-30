var params = {
    soundOn : true,
    background: false,

    'print' : function() {
        console.log(group);
    },
};

var gui;

window.onload = function() {
    gui = new dat.GUI();
    var soundController = gui.add( params, 'soundOn');

    soundController.onFinishChange(function(newValue) {
        // Fires on every change, drag, keypress, etc.
        // alert("The new value is " + value);

        if (newValue === false) {
            // Stop all sounds when someone wants to stop listening to them, just good UX
            var allSoundEls = document.querySelectorAll('[sound]')
            for (var i = 0; i < allSoundEls.length; i++) {
                allSoundEls[i].components.sound.stopSound();
            }
        }
    });


    // gui.add( params, 'print');

    // Hide the gui by default
    // dat.GUI.toggleHide();
    // close instead:
    gui.close();
}

AFRAME.registerComponent('loading-bar', {
    init: function () {

        var manager = document.querySelector('a-assets').fileLoader.manager;

        var progress = document.createElement('div');
        var progressBar = document.createElement('div');

        var note = document.createElement('div');

        var barHeight = 5;

        note.innerText = "Loading ..."
        note.classNames += "bounce";

        progress.appendChild(note);

        progress.appendChild(progressBar);

        progress.id = "progress";
        progressBar.id = "progressBar";
        note.id = "progressNote";

        document.body.appendChild(progress);

        manager.onProgress = function ( item, loaded, total ) {
            var prog = (loaded / total * 100);
            progressBar.style.width = prog + '%';
            // console.log(prog);
        };

        manager.onLoad = function () {
            progress.parentElement.removeChild(progress);
        }
    }
});


// Make a custom mouseenter component that plays a sound lol

// Registering component in foo-component.js
AFRAME.registerComponent('enter-sound', {
    schema: {
        sound: {default: ''}
    },

    init: function () {
        this.el.addEventListener('mouseenter', function (evt) {
            // console.log('I was clicked at: ', evt.detail.intersection.point);
            // console.log(this.el);
            if (params.soundOn) {
                this.components.sound.playSound();
                console.log("playing ...");
            }
        });
    },
  update: function () {},
  tick: function () {},
  remove: function () {},
  pause: function () {},
  play: function () {}
});
