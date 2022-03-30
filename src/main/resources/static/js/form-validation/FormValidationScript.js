/**
 * This configures the defined lambda function to listen to input events to validate.
 * Check the link below for more details about form validations.
 *
 * @see https://formvalidation.io/guide/getting-started/usage
 */
document.addEventListener('DOMContentLoaded', () => {

    /* Login form validation starts */
    FormValidation.formValidation(
        document.querySelector('#login-form'),
        {
            fields: {
                username: {
                    validators: {
                        notEmpty: {
                            message: 'The username is required'
                        },
                        stringLength: {
                            min: 3,
                            max: 50,
                            message: 'The username must be more than 3 and less than 50 characters long'
                        },
                        regexp: {
                            regexp: /^[a-zA-Z0-9_]+$/,
                            message: 'The username can only consist of alphabetical, number and underscore'
                        }
                    }
                },
                password: {
                    validators: {
                        notEmpty: {
                            message: 'The password is required'
                        },
                        stringLength: {
                            min: 4,
                            message: 'The password must be more than 3 characters long'
                        }
                    }
                },
            },
            plugins: {
                trigger: new FormValidation.plugins.Trigger(),
                bootstrap5: new FormValidation.plugins.Bootstrap5({
                    rowSelector: '.mb-3',
                }),
                submitButton: new FormValidation.plugins.SubmitButton(),
                defaultSubmit: new FormValidation.plugins.DefaultSubmit(),
                icon: new FormValidation.plugins.Icon({
                    valid: 'fa fa-check',
                    invalid: 'fa fa-times',
                    validating: 'fa fa-refresh'
                }),
            },
        }
    ); /* Login form validation ends */
});
