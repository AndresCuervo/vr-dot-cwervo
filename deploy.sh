#!/bin/sh

# Inspired by:
# https://gist.github.com/cobyism/4730490

out="dist"
if [ -z "$1" ]; then
    echo "Assuming we're deploying to the dist folder, then"
else
    out=$1
    # echo "Run again, specifying the folder you want to deploy to GitHub Pages."
    # exit 1
fi

boot build
cp -rf target/public/ "$out"
cp -rf resources/public/images "$out"
cp -rf resources/public/js "$out"
rm -f "$out"/CNAME # Remove old CNAME, -f to quiet the warnings
echo "vr.cwervo.com" > "$out"/CNAME
git add "$out"
git commit -m "Deploy commit $(date)"
# Oh, need a new commit before subtree command to get it to sync!
git subtree push --prefix "$out" origin gh-pages
