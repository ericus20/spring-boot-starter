/**
 * Created by z00382545 on 10/20/16.
 */

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

    $.transferDisplay = function () {
        let transferFrom = "#transferFrom";
        let transferTo = "#transferTo";
        $(transferFrom).change(function () {
            if ($(transferFrom).val() === 'Primary') {
                $(transferTo).val('Savings');
            } else if ($(transferFrom).val() === 'Savings') {
                $(transferTo).val('Primary');
            }
        });

        $(transferTo).change(function () {
            if ($(transferTo).val() === 'Primary') {
                $(transferFrom).val('Savings');
            } else if ($(transferTo).val() === 'Savings') {
                $(transferFrom).val('Primary');
            }
        });
    };


}(jQuery));

$(document).ready(function () {
    const confirm = function () {
        bootbox.confirm({
            title: "Appointment Confirmation",
            message: "Do you really want to schedule this appointment?",
            buttons: {
                cancel: {
                    label: '<i class="fa fa-times"></i> Cancel'
                },
                confirm: {
                    label: '<i class="fa fa-check"></i> Confirm'
                }
            },
            callback: function (result) {
                if (result === true) {
                    $('#appointmentForm').submit();
                } else {
                    console.log("Scheduling cancelled.");
                }
            }
        });
    };

    $.toggleShowPassword({
        field: '#password',
        control: "#showPassword"
    });

    $.transferDisplay();

    $(".form_datetime").datetimepicker({
        format: "yyyy-mm-dd hh:mm",
        autoclose: true,
        todayBtn: true,
        startDate: "2013-02-14 10:00",
        minuteStep: 10
    });

    $('#submitAppointment').click(function () {
        confirm();
    });

});
