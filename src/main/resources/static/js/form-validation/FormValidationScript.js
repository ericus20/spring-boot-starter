/**
 * This configures the defined lambda function to listen to input events to validate.
 * Check the link below for more details about form validations.
 *
 * @see https://formvalidation.io/guide/getting-started/usage
 */
const main = () => {

  const loginForm = document.getElementById('login-form');

  if (loginForm) {
    /* Login form validation starts */
    FormValidation.formValidation(
        loginForm,
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
                  message: 'The username must be more than 3 and less than 50 characters'
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
                  message: 'The password must be more than 3 characters'
                }
              }
            },
            terms: {
              validators: {
                notEmpty: {
                  message: 'You must agree before submitting.'
                }
              }
            }
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
        },
    ); /* Login form validation ends */
  }

  const signUpForm = document.getElementById('sign-up-form');
  if (signUpForm) {
    /* Sign up form validation starts */
    FormValidation.formValidation(
        signUpForm,
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
                  message: 'The username must be more than 3 and less than 50 characters'
                }
              }
            },
            email: {
              validators: {
                notEmpty: {
                  message: 'The email is required'
                },
                emailAddress: {
                  message: 'The input is not a valid email address'
                },
                different: {
                  field: 'username',
                  message: "The username and email must be different"
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
                  message: 'The password must be more than 3 characters'
                }
              }
            },
            terms: {
              validators: {
                notEmpty: {
                  message: 'Please agree to the terms and conditions to continue'
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
    ); /* Sign up form validation ends */
  }

  const passwordResetStartForm = document.getElementById(
      'password-reset-start-form');
  if (passwordResetStartForm) {
    /* Sign up form validation starts */
    FormValidation.formValidation(
        passwordResetStartForm,
        {
          fields: {
            email: {
              validators: {
                notEmpty: {
                  message: 'The email is required'
                },
                emailAddress: {
                  message: 'The input is not a valid email address'
                },
                different: {
                  field: 'username',
                  message: "The username and email must be different"
                }
              }
            }
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
    ); /* Sign up form validation ends */
  }

  const passwordResetCompleteForm = document.getElementById(
      'password-reset-complete-form');
  if (passwordResetCompleteForm) {
    /* Sign up form validation starts */
    const fv = FormValidation.formValidation(
        passwordResetCompleteForm,
        {
          fields: {
            password: {
              validators: {
                notEmpty: {
                  message: 'The password is required',
                },
                stringLength: {
                  message: 'Password must be at least 4 characters long',
                  min: 4
                }
              },
            },
            confirmPassword: {
              validators: {
                identical: {
                  compare: function () {
                    return passwordResetCompleteForm.querySelector(
                        '[name="password"]').value;
                  },
                  message: 'The password and its confirm are not the same',
                },
              },
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
    ); /* Sign up form validation ends */

    // Revalidate the confirmation password when changing the password
    passwordResetCompleteForm.querySelector(
        '[name="password"]').addEventListener('input', function () {
      fv.revalidateField('confirmPassword');
    });
  }

};

$(document).ready(main);
