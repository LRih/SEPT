$(document).ready(function ()
{
    var AFFIX_TOP_LIMIT = 300;
    var AFFIX_OFFSET = 49;

    var $menu = $("#menu");
    var $btn = $("#menu-toggle");

    $("#menu-toggle").on("click", function ()
    {
        $menu.toggleClass("open");
        return false;
    });


    $(".docs-nav").each(function ()
    {
        var $affixNav = $(this);
        var $container = $affixNav.parent();
        var affixNavfixed = false;
        var originalClassName = this.className;
        var current = null;
        var $links = $affixNav.find("a");

        function getClosestHeader(top)
        {
            var last = $links.first();

            if (top < AFFIX_TOP_LIMIT)
                return last;

            for (var i = 0; i < $links.length; i++)
            {
                var $link = $links.eq(i);
                var href = $link.attr("href");

                if (href.charAt(0) === "#" && href.length > 1)
                {
                    var $anchor = $(href).first();

                    if ($anchor.length > 0)
                    {
                        var offset = $anchor.offset();

                        if (top < offset.top - AFFIX_OFFSET)
                            return last;

                        last = $link;
                    }
                }
            }

            return last;
        }


        $(window).on("scroll", function (evt)
        {
            var top = window.scrollY;
            var height = $affixNav.outerHeight();
            var max_bottom = $container.offset().top + $container.outerHeight();
            var bottom = top + height + AFFIX_OFFSET;

            if (affixNavfixed)
            {
                if (top <= AFFIX_TOP_LIMIT)
                {
                    $affixNav.removeClass("fixed");
                    $affixNav.css("top", 0);
                    affixNavfixed = false;
                }
                else if (bottom > max_bottom)
                    $affixNav.css("top", (max_bottom - height) - top);
                else
                    $affixNav.css("top", AFFIX_OFFSET);
            }
            else if (top > AFFIX_TOP_LIMIT)
            {
                $affixNav.addClass("fixed");
                affixNavfixed = true;
            }

            var $current = getClosestHeader(top);

            if (current !== $current)
            {
                $affixNav.find(".active").removeClass("active");
                $current.addClass("active");
                current = $current;
            }
        });
    });
});
