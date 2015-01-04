_ = require('underscore')
fetcher = require('./../fetcher')

# Required properties:
#  * name - the name of the recipe to install
module.exports =
  install: (options, recipeStep, cb) ->
    fetcher(recipeStep.name, _({}).extend(options, cleanTmpDirBeforeFetching: false), cb)
