/**
 * Created by lenovo on 2017-06-16.
 */
(function () {
    czy.validate = {
        validate: function (prop, ruleNames, trigger) {
            var temp = [];
            if(rules) {
                rules.forEach(function (ruleName) {
                    var rule =  { validator: validatePass, trigger: 'blur' }
                    temp.push(rule)
                })
            }
        }
    };
});