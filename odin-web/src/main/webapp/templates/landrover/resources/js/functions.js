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
