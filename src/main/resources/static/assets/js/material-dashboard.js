/*!

 =========================================================
 * Material Dashboard - v2.1.0
 =========================================================

 * Product Page: https://www.creative-tim.com/product/material-dashboard
 * Copyright 2018 Creative Tim (http://www.creative-tim.com)

 * Designed by www.invisionapp.com Coded by www.creative-tim.com

 =========================================================

 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

 */

(function () {
    const isWindows = navigator.platform.indexOf('Win') > -1;

    if (isWindows) {
        // if we are on windows OS we activate the perfectScrollbar function
        $('.sidebar .sidebar-wrapper, .main-panel').perfectScrollbar();

        $('html').addClass('perfect-scrollbar-on');
    } else {
        $('html').addClass('perfect-scrollbar-off');
    }
})();

let mobile_menu_visible = 0,
    transparent = true,
    $body = $('body');


$(document).ready(function () {

    $body.bootstrapMaterialDesign();


    $('.form-control').on("focus", function () {
        $(this).parent('.input-group').addClass("input-group-focus");
    }).on("blur", function () {
        $(this).parent(".input-group").removeClass("input-group-focus");
    });

});

$(document).on('click', '.navbar-toggler', function () {
    const $toggle = $(this);

    if (mobile_menu_visible === 1) {
        $('html').removeClass('nav-open');

        $('.close-layer').remove();
        setTimeout(function () {
            $toggle.removeClass('toggled');
        }, 400);

        mobile_menu_visible = 0;
    } else {
        setTimeout(function () {
            $toggle.addClass('toggled');
        }, 430);

        const $layer = $('<div class="close-layer"></div>');

        if ($body.find('.main-panel').length !== 0) {
            $layer.appendTo(".main-panel");

        } else if (($('body').hasClass('off-canvas-sidebar'))) {
            $layer.appendTo(".wrapper-full-page");
        }

        setTimeout(function () {
            $layer.addClass('visible');
        }, 100);

        $layer.on('click', function () {
            $('html').removeClass('nav-open');
            mobile_menu_visible = 0;

            $layer.removeClass('visible');

            setTimeout(function () {
                $layer.remove();
                $toggle.removeClass('toggled');

            }, 400);
        });

        $('html').addClass('nav-open');
        mobile_menu_visible = 1;

    }

});
