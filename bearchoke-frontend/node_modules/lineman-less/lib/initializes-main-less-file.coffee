fs = require('fs')
path = require('path')
findsRoot = require('find-root-package')

module.exports =
  initialize: (dir = process.cwd()) ->
    topDir = findsRoot.findTopPackageJson(dir)
    return unless isInstalledAsDependency(dir, topDir)
    ensureMainLessFileExistsRelativeTo(topDir)


isInstalledAsDependency = (dir, topDir) ->
  topDir? && topDir != dir

ensureMainLessFileExistsRelativeTo = (dir) ->
  mainLessPath = path.join(dir, "app/css/main.less")
  return if fs.existsSync(mainLessPath)
  fs.writeFileSync mainLessPath,  """
    // Main less file for your application.
    // Use `@import` to use other less files
    // relative to 'app/css' or 'vendor/css'.

    """
  console.log """
    Thank you for installing lineman-less!

    We've added a main less file for you to help you get started, here:

    #{mainLessPath}

    """

