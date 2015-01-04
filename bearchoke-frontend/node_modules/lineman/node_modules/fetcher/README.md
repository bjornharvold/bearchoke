# fetcher

`fetcher(recipeNameOrNames, [options], [callback])`

Fetcher is a library the downloads assets and then places them, relative to some `cwd`, wherever a recipe specifies.

``` javascript
var fetcher = require('fetcher');
fetcher('backbone', function(er){
  if(er) { throw er; }
});
```

The above will:

1. Clone the [linemanjs/fetcher-recipes](https://github.com/linemanjs/fetcher-recipes) repo
2. Load "recipes/backbone.cson".
3. Install each step of the recipe in order, which in this case means:
4. Install the "underscore" recipe, which is the first step to the "backbone" recipe (`{type: "recipe", name: "underscore"}`)
5. Install "backbone" by downloading the source file and placing it in `vendor/js/backbone.js`

## Options

An optional options argument can be passed as a second parameter of `fetcher()`. The defaults worth concerning yourself with follow:

``` javascript
{
  recipeRepo: "git@github.com:linemanjs/fetcher-recipes.git",
  cwd: process.cwd()
}
```

That means you can use Fetcher with your own custom tool ecosystem by defining your own recipe repo. You can also define `cwd` to whatever you like in order to install assets relative to whatever path you're interested in.

## Testing fetcher

fetcher currently has no automated tests. To get any feedback, I recommend cloning or starting a recipes repo to work against, then
running fetcher out of its own directory and manually inspecting results:

```
$ node
> require('./index')('google-analytics', {recipeRepo: "/Users/justin/code/linemanjs/fetcher-recipes"}, function(err) { console.log("errors!", err); })
```

And then inspecting that `vendor/js/google-analytics.js` is how it should be, given that recipe.
