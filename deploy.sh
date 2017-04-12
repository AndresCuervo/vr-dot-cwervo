#!/bin/sh
out="dist"
if [ -z "$1" ]; then
    echo "Assuming we're deploying to the dist folder, then"
else
    out=$1
    # echo "Run again, specifying the folder you want to deploy to GitHub Pages."
    # exit 1
fi

boot build
cp -rf target/public "$out"
cp -rf resources/public/images/ "$out"/images
touch "$out"/CNAME
echo "vrr.cwervo.com" > "$out"/CNAME
git add "$out"
git commit -m "Deploy commit `date`"
# Oh, need a new commit before subtree command to get it to sync!
git subtree push --prefix "$out" origin gh-pages
