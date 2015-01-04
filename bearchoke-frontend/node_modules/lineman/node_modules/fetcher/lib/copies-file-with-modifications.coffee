fs = require('fs')
path = require('path')
mkdirp = require('mkdirp')

module.exports =
  copy: (src, dest, transformerFunction, cb) ->
      if fs.existsSync(dest) && fs.statSync(dest).isDirectory()
        dest = path.resolve(dest, path.basename(src))
      mkdirp.sync(path.dirname(dest))

      fs.writeFile dest, transformerFunction(fs.readFileSync(src)), {encoding: 'utf8'}, (er) ->
        return cb(er) if er?
        cb(null)
