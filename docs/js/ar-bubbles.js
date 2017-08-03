const bubbleClass = "temp-bubble"

function getRandomArbitrary(min, max) {
    return Math.random() * (max - min) + min;
}


AFRAME.registerComponent('make-bubbles', {
    schema : {
        n : {default : 1},
        tag : {default : 'a-box'},
        wiggleAmount : {default : 1.25},
        rowN : {default : 2},
        maxBubbles : {default : 4},
        feedbackTarget: {type : 'selector', default : ""}
    },
    init: function () {
        document.addEventListener('touchstart', this.makeBubble)
        document.addEventListener('click', this.makeBubble)
    },
    makeBubble : function () {
        var target = document.getElementById('bubbleFeedback')

        var bubbleAmount = 3

        for (var i = 0; i < bubbleAmount; i++) {
            var newEl = document.createElement("a-sphere")
            newEl.setAttribute('data-live', 5 + Math.random(10) * 20)
            newEl.setAttribute('dynamic-body', 'shape: sphere;')
            // newEl.setAttribute('refraction-shader', `refractionIndex: ${0.15 + Math.random()}; distance: ${0.55};`)
            newEl.setAttribute('refraction-shader', `refractionIndex: ${0.15 + Math.random()}; distance: ${0.33}; opacity: 0.65`)
            // newEl.setAttribute('reflection-shader', '')
            newEl.classList.add(bubbleClass)

            // var scale = 0.8 * (Math.random())
            // newEl.setAttribute('scale', `${scale} ${scale} ${scale}`)

            var scale = getRandomArbitrary(1, 2.5)
            var randomPos = getRandomArbitrary(scale, scale*2)
            newEl.setAttribute('position', `${-1 + randomPos} 0 0`)

            // Resize scale for the actualy scale to grow to lol
            scale *= 0.55

            newEl.setAttribute('animation__grow', `property: scale;
                                                  from: 0 0 0;
                                                  to: ${scale} ${scale} ${scale};
                                                  elasticity: 1000;
                                                  easing: easeInOutElastic;
                                                  dur: ${Math.random() * 1200}`)
            AFRAME.scenes[0].appendChild(newEl)
        }

        // console.log('%c ' +'Bubble is: ' + scale, 'color: #2EAFAC');

        target.setAttribute('color', '#BEE')
        setTimeout( function () {
            target.setAttribute('color', '#2EAFAC')
        }
            , 250)

        // TODO make the plane have an animation-start trigger that flashes its opacity real quick :)
        // Then take a video of it from the iPhone and post to FB & Twitter & Slack! :)
    },
    update: function () {
    },
    tick: function (t) {
        // Turn this tick function into a self-destruct component so you can set the seconds that way!
        [...document.getElementsByClassName(bubbleClass)].forEach(function (bubbleEl) {
            var bubbleLiveTime = bubbleEl.getAttribute('data-live')
            if (bubbleLiveTime <= 0) {
                bubbleEl.parentNode.removeChild(bubbleEl);
            } else {
                bubbleEl.setAttribute('data-live', bubbleLiveTime - .05)
                bubbleEl.setAttribute('opacity', bubbleLiveTime - .05)
            }
        })
    }

});
