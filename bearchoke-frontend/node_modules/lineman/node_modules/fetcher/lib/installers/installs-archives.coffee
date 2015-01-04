path = require('path')
mkdirp = require('mkdirp')
downloadsFiles = require('./../downloads-files')
unpacksArchives = require('./../unpacks-archives')
copiesStuff = require('./../copies-stuff')
async = require('async')
_ = require('underscore')

# Required properties:
#  * url - the url of the archive (tar-balls & zip files are supported)
#  * files - an array of {src: [], dest: ""} objects specifying where files
#            should be copied. No wildcard support yet. Just blind `cp -r`'ing
#
# Optional properties:
#  * archiveType - (zip|tar.gz) - force fetcher to treat the archive as this type
#  * stripRootDir - (true|false) - delete the top level directory in the archive
#
module.exports =
  install: (options, recipeStep, cb) ->
    downloadsFiles.download recipeStep.url, (er, compressedArchivePath) ->
      return cb(er) if er?
      unpacksArchives.unpack compressedArchivePath, recipeStep, (er, uncompressedArchivePath)->
        return cb(er) if er?
        copyMultipleSetsOfFiles(recipeStep.files, uncompressedArchivePath, options.cwd, cb)

copyMultipleSetsOfFiles = (files, archivePath, cwd, cb) ->
  async.series copyFunctionsFor(files, archivePath, cwd), (er, results) ->
    return cb(er) if er?
    cb(null)

copyFunctionsFor = (files, archivePath, cwd) ->
  _(files).map (file) ->
    (cb) ->
      if _(file.src).isArray()
        subFiles = _(file.src).map (src) -> {src: src, dest: file.dest}
        copyMultipleSetsOfFiles(subFiles, archivePath, cwd, cb)
      else
        src = path.resolve(archivePath, file.src)
        dest = path.resolve(cwd, file.dest)
        mkdirp.sync(path.dirname(dest))
        copiesStuff.copy src, dest, (er) ->
          return cb(er) if er?
          console.log("Copied '#{file.src}' to '#{file.dest}'")
          cb(null)
