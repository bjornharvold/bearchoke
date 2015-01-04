"use strict"

fs     = require "fs"
path   = require "path"
crypto = require "crypto"
_      = require "lodash"

module.exports = (grunt) ->
  stripDestPath = (file, files) ->
    file.replace("#{files.orig.dest}/", "")

  contentWithHashSubstitutions = (file, hashMap, cdnPrefixForRootPaths) ->
    originalContent = grunt.file.read(file)
    result = _(hashMap).reduce (memo, hashedName, originalName) ->
      memo.replace(///
        \/#{originalName}
      ///g, "#{cdnPrefixForRootPaths}/#{hashedName}"
      ).replace(///
        #{originalName}
      ///g, hashedName)
    , originalContent
    return result: result, madeAnyDifference: result != originalContent

  containsAFingerprint = (fileName) ->
    fileName.match(/\-\w{32}\./)

  grunt.registerMultiTask "assetFingerprint", "Generates asset fingerprints and appends to a rails manifest", ->
    manifestPath          = @options(manifestPath: "dist/assets.json").manifestPath
    algorithm             = @options(algorithm: "md5").algorithm
    findAndReplaceFiles   = grunt.file.expand(@options(findAndReplaceFiles: []).findAndReplaceFiles)
    keepOriginalFiles     = @options(keepOriginalFiles: true).keepOriginalFiles
    cdnPrefixForRootPaths = @options(cdnPrefixForRootPaths: "").cdnPrefixForRootPaths

    filesToHashed = {}

    _(@files).each (files) ->
      src = files.src[0]
      dest = files.dest
      return if containsAFingerprint(src)

      return grunt.log.debug("Source file `#{src}` was a directory. Skipping.") if grunt.file.isDir(src)
      grunt.log.warn("Source file `#{src}` not found.") unless grunt.file.exists(src)

      algorithmHash = crypto.createHash(algorithm)
      extension     = path.extname(dest)
      content = grunt.file.read(src)

      if _(findAndReplaceFiles).contains(src)
        findAndReplaceFiles = _(findAndReplaceFiles).without(src)
        substitution = contentWithHashSubstitutions(src, filesToHashed, cdnPrefixForRootPaths)
        if substitution.madeAnyDifference
          content = substitution.result
          grunt.file.write(src, content)
          grunt.log.writeln("Applied fingerprinted paths to: #{src}")

      destWithHash = "#{path.dirname(dest)}/#{path.basename(dest, extension)}-#{algorithmHash.update(content).digest("hex")}#{extension}"
      filesToHashed[stripDestPath(dest, files)] = stripDestPath(destWithHash, files)

      if keepOriginalFiles
        grunt.file.copy(src, destWithHash)
        grunt.log.writeln("Copied: '#{src}' to '#{destWithHash}'")
      else
        fs.renameSync(src, destWithHash)
        grunt.log.writeln("Moved: '#{src}' to '#{destWithHash}'")

    _(findAndReplaceFiles).each (file) ->
      return unless fs.existsSync(file)
      substitution = contentWithHashSubstitutions(file, filesToHashed, cdnPrefixForRootPaths)

      if substitution.madeAnyDifference
        grunt.file.write(file, substitution.result)
        grunt.log.writeln("Fingerprinted paths: #{file}")

    fs.writeFileSync(manifestPath, JSON.stringify(filesToHashed, null, "  "))
    grunt.log.writeln "Recorded #{_(filesToHashed).size()} asset mapping(s) to #{manifestPath}"


