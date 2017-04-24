AFRAME.registerComponent('glitch-image', {
    // Could use a schem to preserve the color! then simply change it on update
    // if clicked?
    init: function () {
        const IMAGES = [
            "7up.gif", "ACani.gif", "ANCani.gif", "Bart_Simpson_2.gif",
            "Eemail.gif", "GlitcherWeavelAnimation.gif", "arrwrule2.gif",
            "backlight.gif", "bgcolors.gif", "dollarspindownd.gif",
            "enter.gif", "enterblood.gif", "eyeballs.gif", "fire.gif",
            "flashing_icon.gif", "forward212.gif", "glitch_banner1.gif",
            "glitchgirlmicro.gif", "goback.gif", "goldskull.gif",
            "macaulay.gif", "people5.gif", "redskull.gif", "rotpentag.gif",
            "smileylink.gif", "welcome.gif", "wow.gif", "xara3dbanner.gif"
        ];
        // Attempted to load the assets dynamically, but evidently that didn't
        // work :(

        // var assets_list = document.querySelector('a-assets');
        //
        // var sample_image = document.createElement("img");
        // sample_image.setAttribute('id', IMAGES[i]);
        // sample_image.setAttribute('src', "/images/glitch-ia-images/" + IMAGES[0]);
        // console.log(sample_image);
        //
        // if (!assets_list.contains(sample_image)) {
        //     for (var i; i < IMAGES.length; i++) {
        //         var new_image = document.createElement("img");
        //         new_image.setAttribute('id', IMAGES[i]);
        //         var source_path = "/images/glitch-ia-images/" + IMAGES[i];
        //         new_image.setAttribute('src', source_path);
        //         assets_list.appendChild(new_image);
        //     }
        // }
        var randomIndex = Math.floor(Math.random() * IMAGES.length);
        // var source_mat = "#" + IMAGES[randomIndex];
        var source_path = "/images/glitch-ia-images/" + IMAGES[randomIndex];

        // Example:
        // shader:gif;src:url(nyancat.gif)
        this.el.setAttribute('material', "shader", "gif", );
        this.el.setAttribute('material', "src", "url("+source_path+")", );
        // this.el.setAttribute('material', "src", "url("+source_mat+")", );
        console.log("set " + this.el + "to have a material of: \n" + source_path);
        // console.log("set " + this.el + "to have a material of: \n" + source_mat);

        // this.el.addEventListener('click', function (evt) {
        //     var randomIndex = Math.floor(Math.random() * COLORS.length);
        //     var newColor = COLORS[randomIndex];
        //     this.setAttribute('material', 'color', newColor);
        //     // console.log('I was clicked at: ', evt.detail.intersection.point, "and my new color is: ", newColor);
        // });
    }
});

