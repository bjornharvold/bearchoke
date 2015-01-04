# Give it a git repo, get back an install recipe for fetcher.
#
# The repo needs to look like this:
#  .
#  └── recipes
#      └── backbone.cson
#
# .download method will invoke callback with an object
# representing the requested recipe
#
# e.g. calling `download("git@github.com:linemanjs/fetcher-recipes.git", "backbone", function(recipe){ console.log(recipe)})`
# on this object would fetch the repo, print the backbone recipe out, and delete the fetched repo.
#
path = require('path')
fs = require('fs')
tmp = require('./tmp')

module.exports =
  download: (gitRepo, recipeName, cb) ->
    repoPath = path.resolve(tmp.path(), "recipe-repo")
    if fs.existsSync(repoPath)
      readRecipe(repoPath, recipeName, cb)
    else
      cloneGitRepo gitRepo, tmp.path(), (er) ->
        return cb(er) if er?
        readRecipe(repoPath, recipeName, cb)

which = require('which')
exec = require("child_process").execFile
cloneGitRepo = (gitRepo, cwd, cb) ->
  console.log("Fetching recipes from '#{gitRepo}'...")
  exec which.sync('git'), ["clone", gitRepo, "recipe-repo"], {cwd}, (er, stdout, stderr) ->
    return cb(er) if er?
    cb(null)

CSON = require('cson-safe')
readRecipe = (repoPath, recipeName, cb) ->
  recipePath = path.resolve(repoPath, "recipes", "#{recipeName}.cson")
  return cb(new Error("Recipe '#{recipeName}' was not found.")) unless fs.existsSync(recipePath)
  content = fs.readFileSync(recipePath).toString()
  cb(null, CSON.parse(content))
