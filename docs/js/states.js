AFRAME.registerComponent('fsm-event-trigger', {
    schema: {
        event: {type: 'string'}
    },

    init: function () {
        var currentPosition = this.el.object3D.position;
        console.log("adding click handler ...");
        var fsmEvent = this.data.event;
        this.el.addEventListener('click', function (evt) {
            // var randomIndex = Math.floor(Math.random() * COLORS.length);
            // var newColor = COLORS[randomIndex];
            // this.setAttribute('material', 'color', newColor);
            fsm[fsmEvent]();
            console.log('Fuse: I was clicked at: ', evt.detail.intersection.point);
        });
    }
});


/*
 * ---- Description of states -----
 * Scenes:
 *    - initial: "black"
 *    - removetitle | "black "-> "green"
 *      - Event: Removes the starting sphere and title cards from the scene
 *      - "green" : Trees are gently rotating, in their circles, and after 5 seconds
 *        a sphere appears on one of the trees, text pops up all around you saying you
 *        have to stare at the spheres until they go away (you have to find and stare at
 *        3 spheres moving alongside trees) (the spheres have numbers, that always face
 *        you using the look-at component!)
 *    - worry | "green"-> "yellow"
 *      - Event: animates the trees slowly coming to a freeze, then all turning yellow and red
 *        and slowly sinking into the ground, meanwhile as these set animations finish (~10s)
 *        these black sad trees rise, audio of a bird chirping comes into the scene, and the
 *        a charcoal gray floor rises into the scene. wow
 *      - "yellow" : The charcoal trees (there are only 4 of them), come up to the player (who's
 *        still at [0 1.6 0]) and you have to follow dots up and down each tree until they "pop" back to
 *        life (original clone-like tree lol)
 *    - ending | "yellow"-> "gray"
 *      - Event: the trees, once they're all actually
*/

function fadeOutElements(selectorString, time) {
    for (el of document.querySelectorAll(selectorString)) {
        // This should be doable with adding
        // startEvents: onremovetitle
        // to the anim on the sphere I thought, but idk it's not, so w/e,
        // we'll just do it here.
        var anim_time = 1000 + time;
        el.setAttribute("animation__fade",
            "property: material.opacity; from: 1.0; to: 0.0; dur:" + anim_time+ ";");

        setTimeout(function(){
            // Make sure we don't remove it if the parent is already removed!
            if (el.parentNode) {
                el.parentNode.removeChild(el);
            }
            console.log(el);
            console.log("removed?");
        }, anim_time);
    }
}

var fsm = StateMachine.create({
    initial: 'black',
    events: [
        { name: 'removetitle',  from: 'black',  to: 'green' },
        { name: 'warn',  from: 'green',  to: 'yellow' },
        { name: 'panic', from: ['green', 'yellow'], to: 'red'    },
        { name: 'calm',  from: 'red',    to: 'yellow' },
        { name: 'clear', from: ['yellow', 'red'], to: 'green'  }
    ],
    callbacks: {
        onremovetitle:  function(event, from, to, msg) {
            console.log("event title: " + event);
            console.log("removed title!!");
            fadeOutElements('.title_plane_text', 0);
            fadeOutElements('.title_plane', 10);
            fadeOutElements('.title_text', 20);
            fadeOutElements('#intro_mask', 30);
        },
        onpanic:  function(event, from, to, msg) { alert('panic! ' + msg);               },
        onclear:  function(event, from, to, msg) { alert('thanks to ' + msg);            },
        ongreen:  function(event, from, to)      { document.querySelector('#debug').className = 'green';    },
        onyellow: function(event, from, to)      { document.querySelector('#debug').className = 'yellow';   },
        onred:    function(event, from, to)      { document.querySelector('#debug').className = 'red';      },
    }
});


console.log("FSM ran");

/* --- saving a version of this for later idk ---
AFRAME.registerComponent('fsm-event-trigger', {
    schema: {
        event: {type: 'string'},
        wait: {default: 0},
        timeClicked: {default: 0},
        staring : {default: false}
    },

    init: function () {
        var currentPosition = this.el.object3D.position;
        console.log("adding click handler ...");
        var fsmEvent = this.data.event;
        var waitTime = this.data.wait;
        var timeClicked = this.data.timeClicked;
        var staring = this.data.staring;
        console.log("waiting for " + this.el + "to be pressed " + waitTime + "seconds");
        this.el.addEventListener('click', function (evt) {
            // var randomIndex = Math.floor(Math.random() * COLORS.length);
            // var newColor = COLORS[randomIndex];
            // this.setAttribute('material', 'color', newColor);
            if (waitTime >= timeClicked) {
                fsm[fsmEvent]();
                console.log('Fuse: I was clicked at: ', evt.detail.intersection.point);
            }
        });

        this.el.addEventListener('mouseenter', function (evt) {
            staring = true;
        });
        this.el.addEventListener('mouseleave', function (evt) {
            staring = false;
            timeClicked = 0;
        });
    },

    // tick: function () {
        // if (this.data.staring) {
        //     this.data.timeClicked++;
        // }
        // console.log("this.data.timeClicked: " + this.data.timeClicked);
    // }
});
 * */
