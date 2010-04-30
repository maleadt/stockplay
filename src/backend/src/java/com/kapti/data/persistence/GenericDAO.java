/*
 * GenericDAO.java
 * StockPlay - Abastracte Data access object laag
 *
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

package com.kapti.data.persistence;

import com.kapti.exceptions.FilterException;
import com.kapti.exceptions.StockPlayException;
import com.kapti.filter.Filter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

public interface GenericDAO<T, ID> {
    //
    // Methods
    //

    @Cachable T findById(ID id) throws StockPlayException;
    @Cachable Collection<T> findByFilter(Filter iFilter) throws StockPlayException, FilterException;
    @Cachable Collection<T> findAll() throws StockPlayException;

    @Invalidates boolean update(T entity) throws StockPlayException;
    @Invalidates int create(T entity) throws StockPlayException;
    @Invalidates boolean delete(T entity) throws StockPlayException;


    //
    // Annotations
    //

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Cachable {

    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Invalidates {
    }

}