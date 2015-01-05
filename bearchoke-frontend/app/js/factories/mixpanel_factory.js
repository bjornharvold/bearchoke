angular.module("app").factory('MixPanelFactory', function ($log) {

    // This is an AngularJS wrapper to MixPanel with some high level functions added for simplification
    // API can be found here: https://mixpanel.com/help/reference/javascript-full-api-reference

    var self = {
        //
        // High Level functions
        //
        signup: function (data, type) {
            $log.debug("MixPanel.signup()");
            // Alias user id with MixPanel
            self.alias(data.username);

            // register people properties with mixpanel
            var properties = {'$username': data.username, '$created': new Date()};
            self.peopleSet(properties);
            self.register(properties);

            self.track('Registered', {type: type});
        },

        login: function (data, loginType) {
            $log.debug("MixPanel.login()");
            // Identify user id with MixPanel
            self.identify(data.username);

            // register people properties with mixpanel
            var properties = {'$username': data.username, '$lastLogin': new Date()};
            self.peopleSet(properties);
            self.register(properties);

            // fire off successful login event
            self.track('Login', {type: loginType});
        },

        ignoreUser: function () {
            $log.debug("Mixpanel ignoring user");
            mixpanel.register({"$ignore": "true"});
        },

        clearIgnoreUser: function () {
            $log.debug("Mixpanel enabled");
            mixpanel.unregister("$ignore");
        },

        //
        // Low Level direct MixPanel functions
        // 
        disable: function (eventArray) {
            $log.debug("MixPanel.disable()");
            mixpanel.disable(eventArray);
        },

        // regular event track
        track: function (eventName) {
            $log.debug("MixPanel.track(eventName)");
            mixpanel.track(eventName);
        },

        // regular event track
        trackWithPayload: function (eventName, jsonPayload) {
            $log.debug("MixPanel.track(eventName, payload)");
            mixpanel.track(eventName, jsonPayload);
        },

        // tracking links
        trackLinks: function (domQuery, eventName, jsonPayload) {
            $log.debug("MixPanel.track_links()");

            mixpanel.track_links(domQuery, eventName, jsonPayload);
        },

        // tracking form submission
        trackForms: function (domQuery, eventName) {
            $log.debug("MixPanel.track_forms()");
            mixpanel.track_forms(domQuery, eventName);
        },

        // register super properties
        register: function (jsonPayload) {
            $log.debug("MixPanel.register()");
            mixpanel.register(jsonPayload);
        },

        // register properties once
        registerOnce: function (properties) {
            $log.debug("MixPanel.register_once()");
            mixpanel.register_once(properties);
        },

        // unregister super property
        unregister: function (property) {
            $log.debug("MixPanel.unregister()");
            mixpanel.unregister(property);
        },

        // identify user
        identify: function (id) {
            $log.debug("MixPanel.track_identify()");
            mixpanel.identify(id);
        },

        // Alias a newly registered user to a prior tracking id
        alias: function (id) {
            $log.debug("MixPanel.alias()");
            mixpanel.alias(id);
        },

        // Update the configuration of a mixpanel library instance
        setConfig: function (configDictionary) {
            $log.debug("MixPanel.set_config()");
            mixpanel.set_config(configDictionary);
        },

        // retrieve current config
        getConfig: function () {
            $log.debug("MixPanel.get_config()");
            return mixpanel.get_config();
        },

        // retrieve a super property
        getProperty: function (propName) {
            $log.debug("MixPanel.get_property()");
            return mixpanel.get_property(propName);
        },

        // retrieve current user id
        getDistinctId: function () {
            $log.debug("MixPanel.get_distinct_id()");
            return mixpanel.get_distinct_id();
        },

        // set user property
        peopleSet: function (key, value) {
            $log.debug("MixPanel.people_set()");
            mixpanel.people.set(key, value);
        },

        // set multiple user properties
        peopleSetWithPayload: function (jsonPayload) {
            $log.debug("MixPanel.people_set(payload)");
            mixpanel.people.set(jsonPayload);
        },

        // set user property once
        peopleSetOnce: function (key, value) {
            $log.debug("MixPanel.people.set_once()");
            mixpanel.people.set_once(key, value);
        },

        // set multiple user properties once
        peopleSetOnceWithPayload: function (jsonPayload) {
            $log.debug("MixPanel.people.set_once(payload)");
            mixpanel.people.set_once(jsonPayload);
        },

        // Increment/decrement numeric people analytics properties
        peopleIncrement: function (property, amount) {
            $log.debug("MixPanel.people.increment()");
            mixpanel.people.increment(property, amount);
        },

        // Increment/decrement multiple numeric people analytics properties
        peopleIncrementWithPayload: function (jsonPayload) {
            $log.debug("MixPanel.people_increment(payload)");
            mixpanel.people.increment(jsonPayload);
        },

        // Append a value to a list-valued people analytics property.
        peopleAppend: function (property, value) {
            $log.debug("MixPanel.people.append()");
            mixpanel.people.append(property, value);
        },

        // Append multiple values to a list-valued people analytics property.
        peopleAppendWithPayload: function (jsonPayload) {
            $log.debug("MixPanel.people.append(payload)");
            mixpanel.people.append(jsonPayload);
        },

        // Permanently deletes the current people analytics profile from Mixpanel (using the current distinct_id).
        deleteUser: function () {
            $log.debug("MixPanel.delete_user()");
            mixpanel.delete_user();
        }
    };


    return self;
});
