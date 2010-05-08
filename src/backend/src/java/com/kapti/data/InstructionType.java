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
 *
 */

package com.kapti.data;

import java.io.Serializable;

/**
 * \brief   Enumeratietype dat het soort instructie voorstelt
 */
public enum InstructionType implements Serializable {
    BUY,
    SELL,
    CANCEL,
    BUY_IMMEDIATE,
    SELL_IMMEDIATE,
    STOP_LOSS_BUY,
    STOP_LOSS_SELL,
    TRAILING_STOP_BUY,
    TRAILING_STOP_SELL,
    BRACKET_LIMIT_BUY,
    BRACKET_LIMIT_SELL,
    MANUAL
}