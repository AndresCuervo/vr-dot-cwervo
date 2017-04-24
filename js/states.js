AFRAME.registerComponent('fuse-trigger', {
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
            fadeOutElements('#black_mask', 30);
        },
        onpanic:  function(event, from, to, msg) { alert('panic! ' + msg);               },
        onclear:  function(event, from, to, msg) { alert('thanks to ' + msg);            },
        ongreen:  function(event, from, to)      { document.querySelector('#debug').className = 'green';    },
        onyellow: function(event, from, to)      { document.querySelector('#debug').className = 'yellow';   },
        onred:    function(event, from, to)      { document.querySelector('#debug').className = 'red';      },
    }
});


console.log("FSM ran");
