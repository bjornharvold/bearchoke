fs = require('fs')
path = require('path')
decompress = require('decompress')
tmp = require('./tmp')

# Options:
#  - url - the url from which we'll look for an extension
#  - archiveType - either "zip" or "tar.gz"
module.exports =
  unpack: (compressedArchivePath, options, cb) ->
    ext = determineExtractor(options)
    return cb(new Error("Archive type '#{options.archiveType}' is not supported")) unless ext?
    path = tmp.mkpath("archives")
    strip = if options.stripRootDir then 1 else 0
    fs.createReadStream(compressedArchivePath).pipe(decompress({ext, path, strip})).on 'close', (er, result) ->
      return cb(er) if er?
      cb(null, path)

# Defaults to '.zip', returns null if an unsupported
# archiveType is explicitly set.
determineExtractor = (options) ->
  if options.archiveType?
    if options.archiveType == "zip"
      ".zip"
    else if options.archiveType == "tar.gz"
      ".tar.gz"
  else if options.url?
    if matches = options.url.match(/(.tar.gz|.zip)$/)
      matches[0]
    else
      ".zip"
  else
    ".zip"
