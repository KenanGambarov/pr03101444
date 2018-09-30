// version: beta
// created: 2005-08-30
// updated: 2005-08-31
// mredkj.com
function extractNumber(obj, decimalPlaces, allowNegative) {
    var temp = obj.value;
    if (obj.value.length == 0) {
        obj.value = obj.defaultValue;
    }
    // avoid changing things if already formatted correctly
    var reg0Str = '[0-9]*';
    if (decimalPlaces > 0) {
        reg0Str += '\\.?[0-9]{0,' + decimalPlaces + '}';
    } else if (decimalPlaces < 0) {
        reg0Str += '\\.?[0-9]*';
    }
    reg0Str = allowNegative ? '^-?' + reg0Str : '^' + reg0Str;
    reg0Str = reg0Str + '$';
    var reg0 = new RegExp(reg0Str);
    if (reg0.test(temp))
        return true;

    // first replace all non numbers
    var reg1Str = '[^0-9' + (decimalPlaces != 0 ? '.' : '') + (allowNegative ? '-' : '') + ']';
    var reg1 = new RegExp(reg1Str, 'g');
    temp = temp.replace(reg1, '');

    if (allowNegative) {
        // replace extra negative
        var hasNegative = temp.length > 0 && temp.charAt(0) == '-';
        var reg2 = /-/g;
        temp = temp.replace(reg2, '');
        if (hasNegative)
            temp = '-' + temp;
    }
    //alert(temp);
    if (decimalPlaces != 0) {
        var reg3 = /\./g;
        var reg3Array = reg3.exec(temp);
        if (reg3Array != null) {
            // keep only first occurrence of .
            //  and the number of places specified by decimalPlaces or the entire string if decimalPlaces < 0
            var reg3Right = temp.substring(reg3Array.index + reg3Array[0].length);
            reg3Right = reg3Right.replace(reg3, '');
            reg3Right = decimalPlaces > 0 ? reg3Right.substring(0, decimalPlaces) : reg3Right;
            temp = temp.substring(0, reg3Array.index) + '.' + reg3Right;
        }
    }
    obj.value = temp;
}

function blockNonNumbers(obj, e, allowDecimal, allowNegative)
{
    var key;
    var isCtrl = false;
    var keychar;
    var reg;

    if (window.event) {
        key = e.keyCode;
        isCtrl = window.event.ctrlKey
    } else if (e.which) {
        key = e.which;
        isCtrl = e.ctrlKey;
    }

    if (isNaN(key))
        return true;

    keychar = String.fromCharCode(key);

    // check for backspace or delete, or if Ctrl was pressed
    if (key == 8 || isCtrl)
    {
        return true;
    }

    reg = /\d/;
    var isFirstN = allowNegative ? keychar == '-' && obj.value.indexOf('-') == -1 : false;
    var isFirstD = allowDecimal ? keychar == '.' && obj.value.indexOf('.') == -1 : false;

    return isFirstN || isFirstD || reg.test(keychar);
}

function clear0(thefield) {
    if (thefield.value == 0)
        thefield.value = "";
}

function clear0d0(thefield) {
    if (thefield.value == 0 || thefield.value == 0.0)
        thefield.value = "";
}




function change1() {
    var x = document.getElementById("Bolme1:36:change1_input").selectedIndex;
    var y = document.getElementById("Bolme1:36:change1_input").options;
    document.getElementById("Bolme1:36:kod1").value = y[x].value;
}


function text1() {
    var x = document.getElementById("Bolme1:36:change1_input").selectedIndex;
    var y = document.getElementById("Bolme1:36:change1_input").options;
    document.getElementById("Bolme1:36:ad1").value = y[x].text.substring(8);
}

function change2() {
    var x = document.getElementById("Bolme1:37:change2_input").selectedIndex;
    var y = document.getElementById("Bolme1:37:change2_input").options;
    document.getElementById("Bolme1:37:kod2").value = y[x].value;
}

function text2() {
    var x = document.getElementById("Bolme1:37:change2_input").selectedIndex;
    var y = document.getElementById("Bolme1:37:change2_input").options;
    document.getElementById("Bolme1:37:ad2").value = y[x].text.substring(8);
}

function change3() {
    var x = document.getElementById("Bolme1:38:change3_input").selectedIndex;
    var y = document.getElementById("Bolme1:38:change3_input").options;
    document.getElementById("Bolme1:38:kod3").value = y[x].value;
    
}

function text3() {
    var x = document.getElementById("Bolme1:38:change3_input").selectedIndex;
    var y = document.getElementById("Bolme1:38:change3_input").options;
    document.getElementById("Bolme1:38:ad3").value = y[x].text.substring(8);
}

function change4() {
    var x = document.getElementById("Bolme1:39:change4_input").selectedIndex;
    var y = document.getElementById("Bolme1:39:change4_input").options;
    document.getElementById("Bolme1:39:kod4").value = y[x].value;
    ;
}

function text4() {
    var x = document.getElementById("Bolme1:39:change4_input").selectedIndex;
    var y = document.getElementById("Bolme1:39:change4_input").options;
    document.getElementById("Bolme1:39:ad4").value = y[x].text.substring(8);
}

function change5() {
    var x = document.getElementById("Bolme1:40:change5_input").selectedIndex;
    var y = document.getElementById("Bolme1:40:change5_input").options;
    document.getElementById("Bolme1:40:kod5").value = y[x].value;
    ;
}

function text5() {
    var x = document.getElementById("Bolme1:40:change5_input").selectedIndex;
    var y = document.getElementById("Bolme1:40:change5_input").options;
    document.getElementById("Bolme1:40:ad5").value = y[x].text.substring(8);
}

function change6() {
    var x = document.getElementById("Bolme1:41:change6_input").selectedIndex;
    var y = document.getElementById("Bolme1:41:change6_input").options;
    document.getElementById("Bolme1:41:kod6").value = y[x].value;
    ;
}

function text6() {
    var x = document.getElementById("Bolme1:41:change6_input").selectedIndex;
    var y = document.getElementById("Bolme1:41:change6_input").options;
    document.getElementById("Bolme1:41:ad6").value = y[x].text.substring(8);
}

function change7() {
    var x = document.getElementById("Bolme1:42:change7_input").selectedIndex;
    var y = document.getElementById("Bolme1:42:change7_input").options;
    document.getElementById("Bolme1:42:kod7").value = y[x].value;
    ;
}

function text7() {
    var x = document.getElementById("Bolme1:42:change7_input").selectedIndex;
    var y = document.getElementById("Bolme1:42:change7_input").options;
    document.getElementById("Bolme1:42:ad7").value = y[x].text.substring(8);
}

function change8() {
    var x = document.getElementById("Bolme1:43:change8_input").selectedIndex;
    var y = document.getElementById("Bolme1:43:change8_input").options;
    document.getElementById("Bolme1:43:kod8").value = y[x].value;
    ;
}

function text8() {
    var x = document.getElementById("Bolme1:43:change8_input").selectedIndex;
    var y = document.getElementById("Bolme1:43:change8_input").options;
    document.getElementById("Bolme1:43:ad8").value = y[x].text.substring(8);
}

function change9() {
    var x = document.getElementById("Bolme1:44:change9_input").selectedIndex;
    var y = document.getElementById("Bolme1:44:change9_input").options;
    document.getElementById("Bolme1:44:kod9").value = y[x].value;
    ;
}

function text9() {
    var x = document.getElementById("Bolme1:44:change9_input").selectedIndex;
    var y = document.getElementById("Bolme1:44:change9_input").options;
    document.getElementById("Bolme1:44:ad9").value = y[x].text.substring(8);
}

function change10() {
    var x = document.getElementById("Bolme1:45:change10_input").selectedIndex;
    var y = document.getElementById("Bolme1:45:change10_input").options;
    document.getElementById("Bolme1:45:kod10").value = y[x].value;
    ;
}

function text10() {
    var x = document.getElementById("Bolme1:45:change10_input").selectedIndex;
    var y = document.getElementById("Bolme1:45:change10_input").options;
    document.getElementById("Bolme1:45:ad10").value = y[x].text.substring(8);
}


function reply_click()
{
    alert(document.getElementById("Bolme1:36:change1_input").value);
}

function placeholder() {
    document.getElementById("Bolme1:37:change2_filter").placeholder = "Axtar";
}

function close() {
    var x = document.getElementById("form:left");
    if (x.style.display === 'none') {
        x.style.display = 'block';
    } else {
        x.style.display = 'none';
    }
}