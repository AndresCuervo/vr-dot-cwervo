const bubbleClass = "temp-bubble"

AFRAME.registerComponent('make-bubbles', {
    schema : {
        n : {default : 1},
        tag : {default : 'a-box'},
        color : {default : 'blue'},
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
        var newEl = document.createElement("a-sphere")
        var target = document.getElementById('bubbleFeedback')

        newEl.setAttribute('data-live', 5 + Math.random(10) * 20)
        newEl.setAttribute('dynamic-body', "")
        newEl.setAttribute('refraction-shader', "refractionIndex: " + Math.random())
        newEl.classList.add(bubbleClass)
        var scale = 0.8 * (Math.random())
        newEl.setAttribute('scale', `${scale} ${scale} ${scale}`)
        newEl.setAttribute('position', `${scale * Math.random(3)} ${0.25 + (scale * Math.random(3))} ${scale * Math.random(3)}`)
        AFRAME.scenes[0].appendChild(newEl)

        console.log('%c ' +'Bubble is: ' + scale, 'color: #2EAFAC');

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
