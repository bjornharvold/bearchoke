path = require('path')
tmp = require('./tmp')
request = require('request')
fs = require('fs')

module.exports =
  download: (url, cb) ->
    dest = tmp.mkpath("downloads")
    console.log("Downloading '#{url}'...")
    request(url).pipe(fs.createWriteStream(dest)).on 'finish', ->
      cb(null, dest)
    .on 'error', (er) ->
      cb(er)
