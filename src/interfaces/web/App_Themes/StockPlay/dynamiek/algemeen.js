/*
 * Copyright (c) 2010 StockPlay development team
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

// Variabelen

var searchBox = $('#ctl00_search');

// Vereiste: taalbestand is geladen, alsook jQuery

// Standaard tekst in de zoekbalk

searchBox.focus(function () {
    if ($(this).attr('value') == messages.searchDefault)
        $(this).attr('value', '');
}).blur(function () {
    if ($(this).attr('value') == '')
        $(this).attr("value", messages.searchDefault);
}).bind("keydown", function (e) {
    if (e.keyCode == 13) {
        location.href = "SecuritiesOverview.aspx?search=" + searchBox.val();
        e.preventDefault();
    }
});