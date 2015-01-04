fs = require('fs')
path = require('path')

exports.findTopPackageJson = findTopPackageJson = (dir) ->
  current = path.resolve(dir)
  grandparent = path.resolve(dir, "../..")
  if current == grandparent || !hasPackageJson(grandparent)
    if hasPackageJson(current)
      current
    else
      null
  else
    findTopPackageJson(grandparent)

hasPackageJson = (dir) ->
  fs.existsSync(path.join(dir, "package.json"))
