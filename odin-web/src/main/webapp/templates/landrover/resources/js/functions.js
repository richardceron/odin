/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function adjustMenu() {
    var w = window.innerWidth;
    var parentDiv = document.getElementById("menu");
    var childrens = parentDiv.getElementsByTagName("a");
    var size;

    if (w > 840) {
        size = 100 / childrens.length;
    } else {
        size = 100;
    }

    for (var i = 0; i < childrens.length; i++) {
        var children = childrens[i];
        children.style.width = size + "%";
    }
}

function showModelos() {
    if ($("#contentmodelos").css("display") === "none") {
        $("#contentmodelos").show("slow", function () {
            var delay = 0;
            $('.modelo').each(function () {
                $(this).delay(delay).animate({
                    opacity: 1
                }, 500);
                delay += 500;
            });
        });
        document.body.style.overflowY = "hidden";
    } else {
        $("#contentmodelos").hide("slow", function () {
            $(".modelo").css("opacity", "0");
        });
        document.body.style.overflowY = "auto";
    }
}
