# vr-dot-cwervo

This repo is meant to replace both:

- https://github.com/andrescuervo/vr.cwervo.com (Ruby middleman implementation)
- https://github.com/andrescuervo/vr.cwervo (buggy Reagent implementation)

This site instead runs on [Perun](https://github.com/hashobject/perun),
a  [Boot](https://github.com/boot-clj/boot)-based static site generator.

You can start up a server using:

```
boot dev
```

Make a build with:

```
boot build
```

----

~~One current annoyance is that you have to copy `resources/public/images/` over
to `target/public/`~~ Nope! Solved by adding the "resources" to `build.boot`'s
`:source-paths` vector :).

---

# TODOS:

- (new as of 2017-04-22)
- [ ] Add a title screen, fade from black with (4 things of) text to the opening scene
- [ ] Make a progression between at least 3 scenes, with (glowing?) red spheres as callouts
- [ ] `idk, other stuff 😬`

------

- (old as of 2017-04-22)
- [x] port over the homepage
- [x] port over SASS
- [x] add a couple of the panorama scenes, etc.
- [x] and then, finally add the vr-capstone scene so you can iterate on it using Clojure forms 😍

