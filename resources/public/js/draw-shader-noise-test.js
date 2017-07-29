/**
 * noise component example
 */
AFRAME.registerComponent('noise', {
    dependencies: [ ],
    schema: { },
    init () {
        this.el.addEventListener('draw-render', this.render.bind(this))
    },
    update () { },
    remove () { },
    pause () { },
    play () { },
    render (e) {
        var ctx = e.detail.ctx,
            texture = e.detail.texture,
            w = ctx.canvas.width,
            h = ctx.canvas.height,
            idata = ctx.createImageData(w, h),
            buffer32 = new Uint32Array(idata.data.buffer),
            len = buffer32.length,
            i = 0
        for(; i < len;)
            buffer32[i++] = ((255 * Math.random())|0) << 24
        ctx.putImageData(idata, 0, 0)
        // texture upate
        texture.needsUpdate = true
    }
})

// This goes along with
AFRAME.registerComponent("noise-effect", {
    init: function () {
        // We define it here and not the prototype
        // so as to have a set of uniforms per instance

        const noiseFragmentShader = `
                uniform vec2 dim;
                uniform float time;

                varying vec2 vUv;

                out vec4 fragColor;

                const float permTexUnit = 1.0/256.0;    // Perm texture texel-size
                const float permTexUnitHalf = 0.5/256.0;  // Half perm texture texel-size

                float width = dim.x;
                float height = dim.y;

                const float grainamount = 0.1; //grain amount
                bool colored = true; //colored noise?
                float coloramount = 0.3;
                float grainsize = 4.0; //grain particle size (1.5 - 2.5)
                float lumamount = 0.7; //

                //a random texture generator, but you can also use a pre-computed perturbation texture
                vec4 rnm(in vec2 tc) {
                    float noise =  sin(dot(tc + vec2(time,time),vec2(12.9898,78.233))) * 43758.5453;

                    float noiseR =  fract(noise)*2.0-1.0;
                    float noiseG =  fract(noise*1.2154)*2.0-1.0;
                    float noiseB =  fract(noise*1.3453)*2.0-1.0;
                    float noiseA =  fract(noise*1.3647)*2.0-1.0;

                    return vec4(noiseR,noiseG,noiseB,noiseA);
                }

                float fade(in float t) {
                    return t*t*t*(t*(t*6.0-15.0)+10.0);
                }

                float pnoise3D(in vec3 p) {
                    vec3 pi = permTexUnit*floor(p)+permTexUnitHalf; // Integer part, scaled so +1 moves permTexUnit texel
                    // and offset 1/2 texel to sample texel centers
                    vec3 pf = fract(p);     // Fractional part for interpolation

                    // Noise contributions from (x=0, y=0), z=0 and z=1
                    float perm00 = rnm(pi.xy).a ;
                    vec3  grad000 = rnm(vec2(perm00, pi.z)).rgb * 4.0 - 1.0;
                    float n000 = dot(grad000, pf);
                    vec3  grad001 = rnm(vec2(perm00, pi.z + permTexUnit)).rgb * 4.0 - 1.0;
                    float n001 = dot(grad001, pf - vec3(0.0, 0.0, 1.0));

                    // Noise contributions from (x=0, y=1), z=0 and z=1
                    float perm01 = rnm(pi.xy + vec2(0.0, permTexUnit)).a ;
                    vec3  grad010 = rnm(vec2(perm01, pi.z)).rgb * 4.0 - 1.0;
                    float n010 = dot(grad010, pf - vec3(0.0, 1.0, 0.0));
                    vec3  grad011 = rnm(vec2(perm01, pi.z + permTexUnit)).rgb * 4.0 - 1.0;
                    float n011 = dot(grad011, pf - vec3(0.0, 1.0, 1.0));

                    // Noise contributions from (x=1, y=0), z=0 and z=1
                    float perm10 = rnm(pi.xy + vec2(permTexUnit, 0.0)).a ;
                    vec3  grad100 = rnm(vec2(perm10, pi.z)).rgb * 4.0 - 1.0;
                    float n100 = dot(grad100, pf - vec3(1.0, 0.0, 0.0));
                    vec3  grad101 = rnm(vec2(perm10, pi.z + permTexUnit)).rgb * 4.0 - 1.0;
                    float n101 = dot(grad101, pf - vec3(1.0, 0.0, 1.0));

                    // Noise contributions from (x=1, y=1), z=0 and z=1
                    float perm11 = rnm(pi.xy + vec2(permTexUnit, permTexUnit)).a ;
                    vec3  grad110 = rnm(vec2(perm11, pi.z)).rgb * 4.0 - 1.0;
                    float n110 = dot(grad110, pf - vec3(1.0, 1.0, 0.0));
                    vec3  grad111 = rnm(vec2(perm11, pi.z + permTexUnit)).rgb * 4.0 - 1.0;
                    float n111 = dot(grad111, pf - vec3(1.0, 1.0, 1.0));

                    // Blend contributions along x
                    vec4 n_x = mix(vec4(n000, n001, n010, n011), vec4(n100, n101, n110, n111), fade(pf.x));

                    // Blend contributions along y
                    vec2 n_xy = mix(n_x.xy, n_x.zw, fade(pf.y));

                    // Blend contributions along z
                    float n_xyz = mix(n_xy.x, n_xy.y, fade(pf.z));

                    // We're done, return the final noise value.
                    return n_xyz;
                }

                //2d coordinate orientation thing
                vec2 coordRot(in vec2 tc, in float angle) {
                    float aspect = width/height;
                    float rotX = ((tc.x*2.0-1.0)*aspect*cos(angle)) - ((tc.y*2.0-1.0)*sin(angle));
                    float rotY = ((tc.y*2.0-1.0)*cos(angle)) + ((tc.x*2.0-1.0)*aspect*sin(angle));
                    rotX = ((rotX/aspect)*0.5+0.5);
                    rotY = rotY*0.5+0.5;
                    return vec2(rotX,rotY);
                }

                void main() {
                    // vec2 st = the coords of the tex!//tex_coords;
                    vec2 st = vUv;

                    vec3 rotOffset = vec3(1.425,3.892,5.835); //rotation offset values
                    vec2 rotCoordsR = coordRot(st, time + rotOffset.x);
                    vec3 noise = vec3(pnoise3D(vec3(rotCoordsR*vec2(width/grainsize,height/grainsize),0.0)));

                    if (colored)
                    {
                        vec2 rotCoordsG = coordRot(st, time + rotOffset.y);
                        vec2 rotCoordsB = coordRot(st, time + rotOffset.z);
                        noise.g = mix(noise.r,pnoise3D(vec3(rotCoordsG*vec2(width/grainsize,height/grainsize),1.0)),coloramount);
                        noise.b = mix(noise.r,pnoise3D(vec3(rotCoordsB*vec2(width/grainsize,height/grainsize),2.0)),coloramount);
                    }



                    //noisiness response curve based on scene luminance
                    vec3 lumcoeff = vec3(0.299,0.587,0.114);
                    float luminance = mix(0.0,dot(col, lumcoeff),lumamount);
                    float lum = smoothstep(0.2,0.0,luminance);
                    lum += luminance;

                    noise = mix(noise,vec3(0.0),pow(lum,4.0));
                    col = col+noise*grainamount;

                    // gl_FragColor =  vec4(col,1.0);
                    gl_FragColor =  vec4(1.0, 0.0, 0.0, 1.0);
                }
        `

        this.exports = {
            filter: {
                uniforms: {
                    time : {value : 0.0},
                    dim : {value : 0.0}
                },
                fragment: noiseFragmentShader
            }
        }
    },
    tick : function (t) {
        this.exports.filter.uniforms.time = t / 1000;
    }
});

AFRAME.registerComponent("noise-effect-2", {
    schema: { default: true },

    init: function () {
        this.system = this.el.sceneEl.systems.effects;

        this.uniforms = {
            tDisp:    { type: "t", value: this.generateHeightmap( 64 ) },
            amount:    { type: "f", value: 0.08 },
            angle:    { type: "f", value: 0.02 },
            seed:      { type: "f", value: 0.02 },
            //Between -1,1
            seed_x:    { type: "f", value: 0.02 },
            //Between -1,1
            seed_y:    { type: "f", value: 0.02 },
            distortion_x:  { type: "f", value: 0.5 },
            distortion_y:  { type: "f", value: 0.6 },
            col_s:    { type: "f", value: 0.05 },
            width:    { type: "f", value: window.screen.width },
            height:    { type: "f", value: window.screen.width }
        };

        this.exports = {
            glitch: {
                fragment: this.fragment,
                uniforms: this.uniforms
            }
        }
        // by declaring a .material property we set this component to take a whole pass of it's own
        this.material = this.system.fuse([this.exports.glitch]);

        this.system.register(this);
    },

    vr: true,

    update: function () {
        this.bypass = !this.data;
        this.curF = 0;
        this.generateTrigger();
    },

    remove: function () {
        this.system.unregister(this);
    },

    tock: function () {
        this.uniforms[ 'seed' ].value = Math.random();//default seeding
        // this.uniforms[ 'width' ].value = window.screen.width;
        // this.uniforms[ 'height' ].value = window.screen.height;

        if ( this.curF % this.randX == 0) {

            this.uniforms[ 'amount' ].value = Math.random() / 30;
            this.uniforms[ 'angle' ].value = THREE.Math.randFloat( - Math.PI, Math.PI );
            this.uniforms[ 'seed_x' ].value = THREE.Math.randFloat( - 1, 1 );
            this.uniforms[ 'seed_y' ].value = THREE.Math.randFloat( - 1, 1 );
            this.uniforms[ 'distortion_x' ].value = THREE.Math.randFloat( 0, 1 );
            this.uniforms[ 'distortion_y' ].value = THREE.Math.randFloat( 0, 1 );
            this.curF = 0;
            this.generateTrigger();

        } else if ( this.curF % this.randX < this.randX / 5 ) {

            this.uniforms[ 'amount' ].value = Math.random() / 90;
            this.uniforms[ 'angle' ].value = THREE.Math.randFloat( - Math.PI, Math.PI );
            this.uniforms[ 'distortion_x' ].value = THREE.Math.randFloat( 0, 1 );
            this.uniforms[ 'distortion_y' ].value = THREE.Math.randFloat( 0, 1 );
            this.uniforms[ 'seed_x' ].value = THREE.Math.randFloat( - 0.3, 0.3 );
            this.uniforms[ 'seed_y' ].value = THREE.Math.randFloat( - 0.3, 0.3 );

        }

        this.curF ++;
    },

    generateTrigger: function() {

        this.randX = THREE.Math.randInt( 120, 240 );

    },

    generateHeightmap: function( dt_size ) {

        var data_arr = new Float32Array( dt_size * dt_size * 3 );
        var length = dt_size * dt_size;

        for ( var i = 0; i < length; i ++ ) {

            var val = THREE.Math.randFloat( 0, 1 );
            data_arr[ i * 3 + 0 ] = val;
            data_arr[ i * 3 + 1 ] = val;
            data_arr[ i * 3 + 2 ] = val;

        }

        var texture = new THREE.DataTexture( data_arr, dt_size, dt_size, THREE.RGBFormat, THREE.FloatType );
        texture.needsUpdate = true;
        return texture;

    },

    // fragment: [
    //     "void $main(inout vec4 c, vec4 o, vec2 uv, float d){",
    //         "c.rgb = sin(o.gbr + time) * o.bgr;",
    //     "}"
    // ].join("\n")

    fragment: [

"const float permTexUnit = 1.0/256.0;    // Perm texture texel-size",
"const float permTexUnitHalf = 0.5/256.0;  // Half perm texture texel-size",
"",
"const float grainamount = 0.1; //grain amount",
"bool colored = true; //colored noise?",
"float coloramount = 0.3;",
"float grainsize = 4.0; //grain particle size (1.5 - 2.5)",
"float lumamount = 0.7; //",
"",
"//a random texture generator, but you can also use a pre-computed perturbation texture",
"vec4 rnm(in vec2 tc) {",
"    float noise =  sin(dot(tc + vec2(time,time),vec2(12.9898,78.233))) * 43758.5453;",
"",
"    float noiseR =  fract(noise)*2.0-1.0;",
"    float noiseG =  fract(noise*1.2154)*2.0-1.0;",
"    float noiseB =  fract(noise*1.3453)*2.0-1.0;",
"    float noiseA =  fract(noise*1.3647)*2.0-1.0;",
"",
"    return vec4(noiseR,noiseG,noiseB,noiseA);",
"}",
"",
"float fade(in float t) {",
"    return t*t*t*(t*(t*6.0-15.0)+10.0);",
"}",
"",
"float pnoise3D(in vec3 p) {",
"    vec3 pi = permTexUnit*floor(p)+permTexUnitHalf; // Integer part, scaled so +1 moves permTexUnit texel",
"    // and offset 1/2 texel to sample texel centers",
"    vec3 pf = fract(p);     // Fractional part for interpolation",
"",
"    // Noise contributions from (x=0, y=0), z=0 and z=1",
"    float perm00 = rnm(pi.xy).a ;",
"    vec3  grad000 = rnm(vec2(perm00, pi.z)).rgb * 4.0 - 1.0;",
"    float n000 = dot(grad000, pf);",
"    vec3  grad001 = rnm(vec2(perm00, pi.z + permTexUnit)).rgb * 4.0 - 1.0;",
"    float n001 = dot(grad001, pf - vec3(0.0, 0.0, 1.0));",
"",
"    // Noise contributions from (x=0, y=1), z=0 and z=1",
"    float perm01 = rnm(pi.xy + vec2(0.0, permTexUnit)).a ;",
"    vec3  grad010 = rnm(vec2(perm01, pi.z)).rgb * 4.0 - 1.0;",
"    float n010 = dot(grad010, pf - vec3(0.0, 1.0, 0.0));",
"    vec3  grad011 = rnm(vec2(perm01, pi.z + permTexUnit)).rgb * 4.0 - 1.0;",
"    float n011 = dot(grad011, pf - vec3(0.0, 1.0, 1.0));",
"",
"    // Noise contributions from (x=1, y=0), z=0 and z=1",
"    float perm10 = rnm(pi.xy + vec2(permTexUnit, 0.0)).a ;",
"    vec3  grad100 = rnm(vec2(perm10, pi.z)).rgb * 4.0 - 1.0;",
"    float n100 = dot(grad100, pf - vec3(1.0, 0.0, 0.0));",
"    vec3  grad101 = rnm(vec2(perm10, pi.z + permTexUnit)).rgb * 4.0 - 1.0;",
"    float n101 = dot(grad101, pf - vec3(1.0, 0.0, 1.0));",
"",
"    // Noise contributions from (x=1, y=1), z=0 and z=1",
"    float perm11 = rnm(pi.xy + vec2(permTexUnit, permTexUnit)).a ;",
"    vec3  grad110 = rnm(vec2(perm11, pi.z)).rgb * 4.0 - 1.0;",
"    float n110 = dot(grad110, pf - vec3(1.0, 1.0, 0.0));",
"    vec3  grad111 = rnm(vec2(perm11, pi.z + permTexUnit)).rgb * 4.0 - 1.0;",
"    float n111 = dot(grad111, pf - vec3(1.0, 1.0, 1.0));",
"",
"    // Blend contributions along x",
"    vec4 n_x = mix(vec4(n000, n001, n010, n011), vec4(n100, n101, n110, n111), fade(pf.x));",
"",
"    // Blend contributions along y",
"    vec2 n_xy = mix(n_x.xy, n_x.zw, fade(pf.y));",
"",
"    // Blend contributions along z",
"    float n_xyz = mix(n_xy.x, n_xy.y, fade(pf.z));",
"",
"    // We're done, return the final noise value.",
"    return n_xyz;",
"}",
"",
"//2d coordinate orientation thing",
"vec2 coordRot(in vec2 tc, in float angle) {",
"    float aspect = $width/$height;",
"    float rotX = ((tc.x*2.0-1.0)*aspect*cos(angle)) - ((tc.y*2.0-1.0)*sin(angle));",
"    float rotY = ((tc.y*2.0-1.0)*cos(angle)) + ((tc.x*2.0-1.0)*aspect*sin(angle));",
"    rotX = ((rotX/aspect)*0.5+0.5);",
"    rotY = rotY*0.5+0.5;",
"    return vec2(rotX,rotY);",
"}",
"",
"void $main(inout vec4 color, vec4 origColor, vec2 uv, float depth) {",
"    // vec2 st = the coords of the tex!//tex_coords;",
"    vec2 st = uv;",
"",
"    vec3 rotOffset = vec3(1.425,3.892,5.835); //rotation offset values",
"    vec2 rotCoordsR = coordRot(st, time + rotOffset.x);",
"    vec3 noise = vec3(pnoise3D(vec3(rotCoordsR*vec2($width/grainsize,$height/grainsize),0.0)));",
"",
"    if (colored)",
"    {",
"        vec2 rotCoordsG = coordRot(st, time + rotOffset.y);",
"        vec2 rotCoordsB = coordRot(st, time + rotOffset.z);",
"        noise.g = mix(noise.r,pnoise3D(vec3(rotCoordsG*vec2($width/grainsize,$height/grainsize),1.0)),coloramount);",
"        noise.b = mix(noise.r,pnoise3D(vec3(rotCoordsB*vec2($width/grainsize,$height/grainsize),2.0)),coloramount);",
"    }",
"",
"",
"",
        "vec4 col = textureVR(tDiffuse, st);",
"    //noisiness response curve based on scene luminance",
"    vec3 lumcoeff = vec3(0.299,0.587,0.114);",
"    float luminance = mix(0.0,dot(col.rgb, lumcoeff),lumamount);",
"    float lum = smoothstep(0.2,0.0,luminance);",
"    lum += luminance;",
"",
"    noise = mix(noise,vec3(0.0),pow(lum,4.0));",
"    col.rgb = col.rgb+noise*grainamount;",
"",
"    color =  vec4(1.0, 0.0, 0.0, 1.0);",
"    color =  col;",
"}"

        // "float $rand(vec2 co){",
        // "return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);",
        // "}",
        // "void $main(inout vec4 color, vec4 origColor, vec2 uv, float depth) {",
        // "vec2 p = uv;",
        // "vec2 p_cuervo = uv;",
        // "vec2 p2 = vec2( smoothstep(uvClamp.x, uvClamp.y, p.x),p.y);",
        // "float xs = floor(gl_FragCoord.x / 0.5);",
        // "float ys = floor(gl_FragCoord.y / 0.5);",
        // //based on staffantans glitch shader for unity https://github.com/staffantan/unityglitch
        // "vec4 normal = texture2D ($tDisp, p2 * $seed * $seed);",
        // "if(p2.y < $distortion_x + $col_s && p2.y > $distortion_x - $col_s * $seed) {",
        // "if($seed_x>0.){",
        // "p.y = 1. - (p.y + $distortion_y);",
        // "}",
        // "else {",
        // "p.y = $distortion_y;",
        // "}",
        // "}",
        // "if(p2.x < $distortion_y + $col_s && p2.x > $distortion_y - $col_s * $seed) {",
        // "if( $seed_y > 0.){",
        // "p.x = $distortion_x;",
        // "}",
        // "else {",
        // "p.x = 1. - (p.x + $distortion_x);",
        // "}",
        // "}",
        // "p.x+=normal.x* $seed_x * ($seed/5.);",
        // "p.y+=normal.y* $seed_y * ($seed/5.);",
        // //base from RGB shift shader
        // "vec2 offset = $amount * vec2( cos($angle), sin($angle));",
        // "vec4 cr = textureVR(tDiffuse, p + offset);",
        // "vec4 cga = textureVR(tDiffuse, p);",
        // "vec4 cb = textureVR(tDiffuse, p - offset);",
        // "cr = textureVR(tDiffuse, p_cuervo);",
        // "cga = textureVR(tDiffuse, p_cuervo);",
        // "cb = textureVR(tDiffuse, p_cuervo);",
        // "color = vec4(cr.r, cga.g, cb.b, cga.a);",
        // //add noise
        // "vec4 snow = 200.*$amount*vec4($rand(vec2(xs * $seed,ys * $seed*50.))*0.2);",
        // // "color = color+ snow;",
        // "color = color;",
        // // "color.r *= time;",
        // "}"
    ].join( "\n" )
});
