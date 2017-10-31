/**
 * Created by lenovo on 2017-06-01.
 */
new Vue({
    el: '.login-box',
    data: function () {
        return {
            ruleForm: {
                username: '',
                password: ''
            },
            rules: {
                username: [
                    {required: true, message: 'Please enter the account', trigger: 'blur'},
                    {min: 3, max: 100, message: '', trigger: 'blur'}
                ],
                password: [
                    {required: true, message: 'Please enter the password', trigger: 'blur'},
                    {min: 3, max: 100, message: '', trigger: 'blur'}
                ]
            }
        };
    }
});