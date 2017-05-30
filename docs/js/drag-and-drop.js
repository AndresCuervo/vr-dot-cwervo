// "myAwesomeDropzone" is the camelized version of the HTML element's ID
Dropzone.options.myAwesomeDropzone = {
    paramName: "file", // The name that will be used to transfer the file
    //maxFilesize: 2, // MB
    accept: function(file, done) {
        var reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById("bgImage").setAttribute('src', 'url(' + e.target.result + ')');
        }
        reader.readAsDataURL(file);
        done();
    },
    thumbnail: function(file, dataUrl) {
        // Just don't display a thumbnail
    },
};



/// ---- vue.js stuff ----

function changeFOV(newFOV) {
    // console.log("setting cam FOV to: " + newFOV);
    document.getElementById("camera").setAttribute("fov", newFOV);
    document.getElementById("fovNum").innerHTML = newFOV;
}

function changeRotation(axis, newRot) {
    document.getElementById("bgImage").setAttribute('rotation', axis, newRot);
    document.getElementById("xNum").innerHTML = newRot;
}

function makeSlider(rootID, elToChange, title, initialValue, inputType, range, attribute, attributeArg) {
    var newSlider = document.createElement("div");
    var titleEl = document.createElement("div");
    titleEl.innerText = title;
    var  value = document.createElement("p");
    value.innerText = initialValue;
    var newID = "title-" + title.replace(/\W/g, '')
    value.id = newID;
    var input = document.createElement("input");
    input.type = inputType;
    input.value = initialValue;
    input.min = range.min;
    input.max = range.max;
    if (!attributeArg) {
        console.log("there's no arg2 for ", title);
        input.oninput = function () {
            document.getElementById(elToChange).setAttribute(attribute, this.value);
            document.getElementById(newID).innerHTML = this.value;

        }
    } else {
        input.oninput = function () {
            document.getElementById(elToChange).setAttribute(attribute, attributeArg, this.value);
            document.getElementById(newID).innerHTML = this.value;
        }
    }
    newSlider.appendChild(titleEl);
    newSlider.appendChild(value);
    newSlider.appendChild(input);
    // console.log("making: " + newSlider.innerHTML);
    document.getElementById(rootID).appendChild(newSlider);
}

/*
;; common params for a function to automate making this shit:
      ;; param 1: base id: #hud
      ;; param 2 : title
      ;; param 3: initialValue (got to both title with its own class and as value of input)
      ;; param 4: inputType (usually range, but you can experiment!)
      ;; param 5: range (an array with 'min' and 'max' attributes! e.g. {'min' : -360, 'max' : 360})
      ;; param 6: oninput (text value of function to call to maniupulate stuff!)
      [:div
       [:div "rotation Y"]
       ;; Set to initial FOV number!
       ;; Don't forget to change if initial changes, or wire that up ... see, this is why using Vue.js would have been so much easier
       [:p#xNum "-130"]
       ;; Ugh, putting a-scene into the Vue element duplicates it or something, causing everything to break :(
       [:input {:type "range"
                :value -130
                :min -360
                :max 360
                :oninput "changeRotation('y', this.value)"
                ;; :v-model "zRotation"
                }]]
*/

// Vue.js screws up any a-frames inside of them ://///
window.onload = function () {

    makeSlider("hud", "camera", "FOV", 80, "range", {'min' : 1, 'max' : 200}, "fov");
    makeSlider("hud", "bgImage", "Rotation X", 0, "range", {'min' : -360, 'max' : 360}, "rotation", "x");
    makeSlider("hud", "bgImage", "Rotation Y", -130, "range", {'min' : -360, 'max' : 360}, "rotation", "y");
    makeSlider("hud", "bgImage", "Rotation Z", 0, "range", {'min' : -360, 'max' : 360}, "rotation", "z");
    /*
    var app = new Vue({
        el: '#appContainer',
        data: {
            zRotation: '50'
        }
    })
    */
}
