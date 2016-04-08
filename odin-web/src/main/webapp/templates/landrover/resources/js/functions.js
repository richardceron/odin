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

function showCollToAction(object) {
    var parentId = String(object.parentNode.id);
    var id = $(object).attr("coll-des");

    if (parentId.indexOf("index") > -1) {
        $(".call-to-action-description-index").css("display", "none");
    } else if (parentId.indexOf("template") > -1) {
        $(".call-to-action-description-template").css("display", "none");
    }
    $("#" + id).show(100);
}

function hideCollToAction(object) {
    var parentId = String(object.parentNode.id);
    var id = $(object).attr("coll-des");
    $("#" + id).hide(100);
}
