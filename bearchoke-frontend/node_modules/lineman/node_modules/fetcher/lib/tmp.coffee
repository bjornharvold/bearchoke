path = require('path')
os = require('os')
mkdirp = require('mkdirp')
fs = require('fs')
rimraf = require('rimraf')

p = path.resolve(os.tmpdir(), "com.testdouble.npm.fetcher", String(new Date().getTime()))

module.exports = tmp =
  path: ->
    mkdirp.sync(p) unless fs.existsSync(p)
    p

  mkpath: (name) ->
    parent = path.resolve(tmp.path(), name)
    mkdirp.sync(parent)
    path.resolve(parent, String(new Date().getTime()))

  clean: ->
    rimraf.sync(p)
