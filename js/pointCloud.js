// AFRAME.registerComponent('pointlight', {
//     init: function () {
//         // this.el.setObject3D('light', new THREE.PointLight());
//         // Taken from:
//         // https://codepen.io/seanseansean/pen/EaBZEY
//         geometry = new THREE.Geometry(); #<{(|	NO ONE SAID ANYTHING ABOUT MATH! UGH!	|)}>#
//
//         particleCount = 20000; #<{(| Leagues under the sea |)}>#
//
//         #<{(|	Hope you took your motion sickness pills;
//     We're about to get loopy.	|)}>#
//
//         for (i = 0; i < particleCount; i++) {
//
//             var vertex = new THREE.Vector3();
//             vertex.x = Math.random() * 2000 - 1000;
//             vertex.y = Math.random() * 2000 - 1000;
//             vertex.z = Math.random() * 2000 - 1000;
//
//             geometry.vertices.push(vertex);
//         }
//
//         #<{(|	We can't stop here, this is bat country!	|)}>#
//
//         parameters = [
//             [
//                 [1, 1, 0.5], 5
//             ],
//             [
//                 [0.95, 1, 0.5], 4
//             ],
//             [
//                 [0.90, 1, 0.5], 3
//             ],
//             [
//                 [0.85, 1, 0.5], 2
//             ],
//             [
//                 [0.80, 1, 0.5], 1
//             ]
//         ];
//         parameterCount = parameters.length;
//
//         #<{(|	I told you to take those motion sickness pills.
//     Clean that vommit up, we're going again!	|)}>#
//         materials = [];
//
//         for (i = 0; i < parameterCount; i++) {
//
//             color = parameters[i][0];
//             size = parameters[i][1];
//
//             materials[i] = new THREE.PointCloudMaterial({
//                 size: size
//             });
//
//             particles = new THREE.PointCloud(geometry, materials[i]);
//
//             particles.rotation.x = Math.random() * 6;
//             particles.rotation.y = Math.random() * 6;
//             particles.rotation.z = Math.random() * 6;
//         }
//         console.log({particles});
//         document.querySelector('a-scene').object3D.add(particles);
//     }});
//
// for (i = 0; i < scene.children.length; i++) {
//     var object = scene.children[i];
//     if (object instanceof THREE.PointCloud) {
//         object.rotation.y = time * (i < 4 ? i + 1 : -(i + 1));
//     }
// }
// for (i = 0; i < materials.length; i++) {
//
//     color = parameters[i][0];
//
//     h = (360 * (color[0] + time) % 360) / 360;
//     materials[i].color.setHSL(h, color[1], color[2]);
// }


// AFRAME.registerComponent('pointlight', {
//     init: function () {
//
//         geometry = new THREE.Geometry();
//         var loader = new THREE.PLYLoader();
//         var group = new THREE.Object3D();
//         loader.load("/assets/cloud.ply", function (geometry) {
//             var material = new THREE.PointCloudMaterial({
//                 color: 0xffffff,
//                 size: 0.4,
//                 opacity: 0.6,
//                 transparent: true,
//                 blending: THREE.AdditiveBlending
//             });
//             group = new THREE.PointCloud(geometry, material);
//             group.sortParticles = true;
//             document.querySelector('a-scene').object3D.add(group);
//         });
//     }
// });


