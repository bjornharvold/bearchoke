# grunt-watch-nospawn

The [Grunt](https://github.com/gruntjs/grunt) 0.4 release was a major release. One of its plugins, [grunt-contrib-watch](https://github.com/gruntjs/grunt-contrib-watch), was updated for that release but incidentally changed significantly. This module is a drop-in replacement for grunt-contrib-watch designed to retain the same behavior as the branch that was compatible with Grunt 0.3.

It's really only intended to be used by [Lineman](https://github.com/testdouble/lineman), but if you've experienced a performance regression with the Grunt 0.4-compatible versions of grunt-contrib-watch, you might consider experimenting with this module.

## Why?

We experienced significant performance regressions when upgrading grunt-contrib-watch along with the rest of our grunt plugins. Because our web project tool [Lineman](https://github.com/testdouble/lineman) relies on rapid feedback upon file change, this regression was a significant impediment to our productivity. The biggest cause performance regression was caused by a change in which the watch task would spawn a new Grunt process for every matched file change. That change's laudable intention was to sandbox tasks from another to prevent any pollution of state in the process. While it succeeds to sandbox the task runs, we haven't experienced related pain in Lineman's workflow and don't think the productivity disruption is worth it.
