fs = require('fs')
cpr = require('cpr')
path = require('path')
mkdirp = require('mkdirp')

module.exports =
  copy: (src, dest, cb) ->
    if fs.statSync(src).isDirectory()
      cpr src, dest, {deleteFirst: false, overwrite: true}, (er) ->
        return cb(er) if er?
        cb(null)
    else
      if fs.existsSync(dest) && fs.statSync(dest).isDirectory()
        dest = path.resolve(dest, path.basename(src))
      mkdirp.sync(path.dirname(dest))
      fs.createReadStream(src).pipe(fs.createWriteStream(dest)).on 'finish', (er) ->
        return cb(er) if er?
        cb(null)
