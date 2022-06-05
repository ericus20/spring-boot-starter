(function ($) {
  $.toggleShowPassword = function (options) {
    const settings = $.extend({
      field: "#password",
      control: "#toggle_show_password",
    }, options);

    const control = $(settings.control);
    const field = $(settings.field);

    control.bind('click', function () {
      if (control.is(':checked')) {
        field.attr('type', 'text');
      } else {
        field.attr('type', 'password');
      }
    })
  };
}(jQuery));

$(document).ready(function () {
  $.toggleShowPassword({
    field: '#password',
    control: "#showPassword"
  });

  $('#locale').change(function () {
    window.location.search = '?locale=' + $(this).val();
  });

});
