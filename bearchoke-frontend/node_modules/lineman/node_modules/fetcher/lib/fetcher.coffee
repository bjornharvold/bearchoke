_ = require('underscore')
downloadsRecipes = require('./downloads-recipes')
installsRecipes = require('./installs-recipes')
async = require('async')
tmp = require('./tmp')

module.exports = (recipe, options, cb = ->) ->
  if _(options).isFunction()
    cb = options
    options = null
  recipes = if _(recipe).isArray() then recipe else [recipe]
  options = _({}).extend(defaultOptions(), options)
  tmp.clean() if options.cleanTmpDirBeforeFetching

  async.series fetchesFor(options, recipes), (er, results) ->
    tmp.clean()
    cb(if er? then er else null)

  return undefined

defaultOptions = ->
  recipeRepo: "git@github.com:linemanjs/fetcher-recipes.git"
  cwd: process.cwd()
  cleanTmpDirBeforeFetching: true
  alreadyInstalledRecipes: []

fetchesFor = (options, recipes) ->
  _(recipes).map (recipeName) ->
    (cb) ->
      return cb(null) if _(options.alreadyInstalledRecipes).include(recipeName)
      options.alreadyInstalledRecipes.push(recipeName)
      downloadsRecipes.download options.recipeRepo, recipeName, (er, recipe) ->
        return cb(er) if er?
        installsRecipes.install options, recipe, (er) ->
          return cb(er) if er?
          console.log("Successfully installed '#{recipeName}'.")
          if recipe.message?
            console.log """
                        The '#{recipeName}' recipe left you this message:

                        ---
                        #{recipe.message}
                        ---
                        """
          cb(null)
