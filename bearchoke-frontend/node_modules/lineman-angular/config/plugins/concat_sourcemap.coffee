_ = require('underscore')

module.exports = (lineman) ->
  config:
    concat_sourcemap:
      js:
        # Removes the template and re-appends it to the end so that templates
        # are loaded *after* application code (this is necessary because Angular
        # templates require the module be defined)
        #
        # The _.shenanigans() avoid thrashing with other plugins which also want
        # to affect the concat bundle.
        src: _(lineman.config.application.concat_sourcemap.js.src).
          without("<%= files.template.generated %>").
          concat("<%= files.template.generated %>")
