_ = require('underscore')
async = require('async')

module.exports =
  install: (options, recipe, cb) ->
    async.series installStepsFor(options, recipe.steps), (er, results) ->
      return cb(er) if er?
      cb(null, results)

installStepsFor = (options, steps) ->
  _(steps).map (step) ->
    (cb) ->
      installer = installerFor(step.type)
      if !installer? then return cb(new Error("This version of fetcher does not know how to install '#{step.type}' recipes."))
      installer.install(options, step, cb)

installerFor = (type) ->
  switch (type || "file")
    when "file"
      require('./installers/installs-files')
    when "recipe"
      require('./installers/installs-recipes')
    when "archive"
      require('./installers/installs-archives')
